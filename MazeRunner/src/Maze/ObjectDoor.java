package Maze;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a Door in a level
 */
public class ObjectDoor {

	private ArrayList<Point2D.Float> points;
	private Point2D.Float switchLocation;
	
	public ObjectDoor(){
		
		points = new ArrayList<Point2D.Float>();
	}
	
	public ObjectDoor(ArrayList<Point2D.Float> p,Point2D.Float doorswitch){
		
		points = p;
		switchLocation = doorswitch;
		
	}
	
	public ArrayList<Point2D.Float> getPoints(){
		return points;
	}
	
	public Point2D.Float getSwitchLocation(){
		return switchLocation;
	}
	
	public static ObjectDoor Read(Scanner s){
		
		s.useDelimiter(" |; |;\r\n");
		ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
		for(int i = 0; i < 4; i++){
			Point2D.Float tempP = new Point2D.Float();
			tempP.x = s.nextFloat();
			tempP.y = s.nextFloat();
			p.add(tempP); 
		}
		Point2D.Float tempP = new Point2D.Float();
		tempP.x = s.nextFloat();
		tempP.y = s.nextFloat();
		return new ObjectDoor(p,tempP);
	}
	
	public String toFileFormat()
	{
		String res = "Door: ";
		for(int i =0; i<points.size();i++){
			res = res + (int)points.get(i).x + " " + (int)points.get(i).y + "; ";
		}
		res = res + (int)switchLocation.getX() + " " + (int)switchLocation.getY() +";\r\n";
		return res;
	}
	
}