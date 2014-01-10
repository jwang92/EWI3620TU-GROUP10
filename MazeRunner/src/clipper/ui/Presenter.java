package clipper.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.SwingUtilities;

import LevelEditor.NavMeshGeneration;
import Maze.Storey;
import clipper.ClipType;
import clipper.Clipper;
import clipper.IntPoint;
import clipper.PolyFillType;
import clipper.PolygonClipper;
import clipper.internal.PolyType;
import clipper.ui.Presenter.View.Callback;
import clipper.ui.Presenter.View.Sample;

public class Presenter {
	public interface View {
		interface Callback { void execute(); }
		enum FillType { EVENODD, NONZERO }
		enum Operation { INTERSECTION, UNION, DIFFERENCE, XOR, NONE }
		enum Sample { CIRCLES, POLYGONS }
		
		void setSize(int width, int height);
		void setSubjectPolygons(List<PolygonClipper> polygons);
		void setClipPolygons(List<PolygonClipper> polygons);
		void setSolutionPolygons(List<PolygonClipper> polygons);
		void setScale(long scale);
		void setAreas(double subjectArea, double clipArea, double intersectionArea, double unionArea, double calculatedUnion);

		void setUpdateCallback(Callback callback);
		
		Operation getOperation();
		FillType getFillType();
		int getCount();
		int getOffset();
		Sample getSample();
	}

	private static Random random = new Random();

	private int scale = 20;
	private int width = 900;
	private int height = 700;
	private View view;

	public Presenter(View view) {
		this.view = view;
	}

	public void start() {
		view.setSize(width, height);

		refresh();
		
		view.setUpdateCallback(new Callback() {
			public void execute() {
				refresh();
			}
		});
	}
	
	private void refresh() {
		List<PolygonClipper> subjects = new ArrayList<PolygonClipper>();

//		if(view.getSample() == Sample.CIRCLES) {
//			subjects = getAustraliaPolygon();
//		}
		Storey storey = new Storey();
		ArrayList<Storey> storeys;
		storeys = new ArrayList<Storey>();
		try {
		    File folder = new File("savefiles/testlevel22");
		    File[] tList = folder.listFiles();
		    int numberOfStoreys = tList.length - 1; // -1 for LevelInfo.txt
		    for(int i = 0; i<tList.length;i++){
			    if(tList[i].getName().equals("Thumbs.db")){
			    	numberOfStoreys -= 1;
			    }  
		    }
			for(int i =1;i<numberOfStoreys+1;i++){
				File f = new File("savefiles/testlevel22" + "/Floor " + i);
				if(f.exists()){
					storey = Storey.Read("savefiles/testlevel22" + "/Floor " + i);
					storeys.add(storey);
				}
				else{
					storeys = new ArrayList<Storey>();
					numberOfStoreys = 0;
				}

			}	
			
		} catch (FileNotFoundException e) {
			storeys = new ArrayList<Storey>();
		}
		NavMeshGeneration navGen = new NavMeshGeneration(storeys);
		navGen.generateWalkablePolygons();
		navGen.MergeWalkablePolygons();
		navGen.generateBlockAreas();
		navGen.increaseBlockedAreaSize();
		navGen.RemoveBlockedArea();
		subjects = navGen.resultClipper;
		
		PolyFillType fillType = getFillType();

		view.setScale(scale);
		view.setSubjectPolygons(subjects);
	}
	
	private static int readInt(InputStream inputStream) throws IOException {
		int b1 = inputStream.read();
		int b2 = inputStream.read();
		int b3 = inputStream.read();
		int b4 = inputStream.read();
		
		if(b1 == -1 || b2 == -1 || b3 == -1 || b4 == -1) throw new IOException("End of file");
		
		int r = 0;
		r += b4;
		r <<= 8;
		r += b3;
		r <<= 8;
		r += b2;
		r <<= 8;
		r += b1;
		
		return r;
	}
	
	private PolyFillType getFillType() {
		switch(view.getFillType()) {
			case EVENODD:
				return PolyFillType.EVENODD;
			case NONZERO:
				return PolyFillType.NONZERO;
		}
		return PolyFillType.EVENODD;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Presenter(new GuiTest()).start();
			}
		});
	}
}
