import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.Scanner;


public class Pickup {
	
	private Point2D.Float p;
	private int type;
	
	public Pickup(Point2D.Float point, int t){
		
		p = point;
		type = t;
		
	}
	
	public Pickup(){
		
		p = new Point2D.Float(-1, -1);
		type = -1;
		
	}
	
	public Point2D.Float getPoint(){
		return p;		
	}
	
	public int getType(){
		return type;
	}
	
	public static Pickup Read(Scanner s){
		
		s.useDelimiter(": | |; |;\r\n");
		
		int t = s.nextInt();

		Point2D.Float p = new Point2D.Float(s.nextFloat(), s.nextFloat());
		
		return new Pickup(p, t);
	}

}
