import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;



public class ObjectRamp {

	private ArrayList<Point2D.Float> points;
	
	public ObjectRamp(){
		
		points = new ArrayList<Point2D.Float>();
		
	}
	
	public ArrayList<Point2D.Float> getPoints(){
		return points;
	}
	
	public void Read(Scanner s){
		
		s.useDelimiter(" |; |;\r\n");
		
		for(int i = 0; i < 4; i++){
	
			Point2D.Float tempP = new Point2D.Float();
			tempP.x = s.nextFloat();
			tempP.y = s.nextFloat();
			points.add(tempP); 
			
		}
		
	}
	
}
