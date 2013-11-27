import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.media.opengl.GLJPanel;


public class CursorHandler {
	
	private GLJPanel canvas;
	private int cursorID;
	private ArrayList<Cursor> cursors;
	
	public CursorHandler(GLJPanel c){
		
		canvas = c;
		cursors = new ArrayList<Cursor>();
		cursorID = -1;
		
		loadCursors();
		
	}
	
	public void loadCursors(){
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();  
		Image image;  
		Point hotSpot;
		Cursor c;
		
		image = toolkit.getImage("cursors/drag.png");
		hotSpot = new Point(0,0);
		c =  toolkit.createCustomCursor(image, hotSpot, "Drag");
		cursors.add(c);
		
		image = toolkit.getImage("cursors/rotate.png");
		hotSpot = new Point(0,0);
		c =  toolkit.createCustomCursor(image, hotSpot, "Rotate");
		cursors.add(c);
		
	}
	
	public int getCursorID(){
		
		return cursorID;
		
	}
	
	public void setCursor(int id){
		
		cursorID = id;
		
		if(id < 0){
			
			switch(id){
				case -1:
					canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					break;
				case -2:
					canvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					break;
				case -3:
					canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					break;
			}
		}
		else{
			
			canvas.setCursor(cursors.get(id));
			
		}
		
	}
	

}
