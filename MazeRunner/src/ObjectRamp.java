import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;



public class ObjectRamp {

	private ArrayList<Point2D.Float> points;
	private String texture;
	
	public ObjectRamp(){
		
		points = new ArrayList<Point2D.Float>();
		texture = "";
		
	}
	
	public ObjectRamp(ArrayList<Point2D.Float> p, String tex){
		
		points = p;
		texture = tex;
		
	}
	
	public ArrayList<Point2D.Float> getPoints(){
		return points;
	}
	
	public String getTexture(){
		return texture;
	}
	
	public static ObjectRamp Read(Scanner s){
		
		s.useDelimiter(" |; |;\r\n");
		ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
		for(int i = 0; i < 4; i++){
			Point2D.Float tempP = new Point2D.Float();
			tempP.x = s.nextFloat();
			tempP.y = s.nextFloat();
			p.add(tempP); 
		}
		String tex = s.next();
		return new ObjectRamp(p,tex);
	}
	
	public String toFileFormat()
	{
		String res = "Ramp: ";
		for(int i =0; i<points.size();i++){
			res = res + (int)points.get(i).x + " " + (int)points.get(i).y + "; ";
		}
		res = res + texture + ";\r\n";
		return res;
	}
	
}
