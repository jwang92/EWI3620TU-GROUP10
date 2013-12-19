package Maze;
import java.awt.geom.Point2D;
import java.util.Scanner;


public class LevelExit {
	
	private Point2D.Float p;
	protected double exitsize = 2;
	protected String newLoadFolder = "savefiles/testlevel9";
	
	public LevelExit(Point2D.Float point, int size, String loadfolder){
		
		p = point;
		exitsize = size;
		newLoadFolder = loadfolder;
		
	}
	
	public LevelExit(){
		
		p = new Point2D.Float(-1, -1);
		
	}
	
	public String getNewLoadFolder(){
		return newLoadFolder;
	}
	
	public Point2D.Float getPoint(){
		return p;		
	}
	
	public static LevelExit Read(Scanner s){
		
		s.useDelimiter(" | |; |;\r\n");
		
		Point2D.Float p = new Point2D.Float(s.nextFloat(), s.nextFloat());
		int size = (int) s.nextInt();
		String loadfolder = s.next();
		
		return new LevelExit(p, size, loadfolder);
	}
	
	public String toFileFormat()
	{
		String res = "Exit: ";
		res = res + (int)p.x + " " + (int)p.y + "; ";
		res = res + (int)exitsize + "; " + newLoadFolder + ";\r\n";
		return res;
	}

}
