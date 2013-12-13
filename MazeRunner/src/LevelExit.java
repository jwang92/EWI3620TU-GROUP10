import java.awt.geom.Point2D;
import java.util.Scanner;


public class LevelExit {
	
	private Point2D.Float p;
	protected double exitsize = 2;
	protected String newloadfolder = "savefiles/kasteel";
	
	public LevelExit(Point2D.Float point){
		
		p = point;
		
	}
	
	public LevelExit(){
		
		p = new Point2D.Float(-1, -1);
		
	}
	
	public Point2D.Float getPoint(){
		return p;		
	}
	
	public static LevelExit Read(Scanner s){
//		
//		s.useDelimiter(": | |; |;\r\n");
//		
//		int t = s.nextInt();
//
//		Point2D.Float p = new Point2D.Float(s.nextFloat(), s.nextFloat());
//		
//		return new Pickup(p, t);
		return null;
	}

}
