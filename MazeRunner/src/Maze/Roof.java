package Maze;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;


public class Roof {

	private ArrayList<Point2D.Float> points;
	private String texture;
	
	/**
	 * Creates a new Roof with the given coordinates
	 * @param xx1 x of coordinate 1
	 * @param yy1 y of coordinate 1
	 * @param xx2 x of coordinate 2
	 * @param yy2 y of coordinate 2
	 * @param xx3 x of coordinate 3
	 * @param yy3 y of coordinate 3
	 * @param xx4 x of coordinate 4
	 * @param yy4 y of coordinate 4
	 * @param tex Name of the texture
	 */
	public Roof(ArrayList<Point2D.Float> p, String tex)
	{
		if(p.size() >=4){
			points = p;
			texture = tex;
		}	
	}
	
	public Roof(){
		points = new ArrayList<Point2D.Float>();
		texture = "";
	}
	
	public ArrayList<Point2D.Float> getPoints(){
		return points;
	}
	
	public String getTexture(){
		return texture;
	}
	
	/**
	 * Creates format for filewriting
	 * @return String with correct format
	 */
	public String toFileFormat()
	{
		String res = "";
		for(int i =0; i<points.size();i++){
			res = res + (int)points.get(i).x + " " + (int)points.get(i).y + "; ";
		}
		res = res + texture + ";\r\n";
		return res;
	}
	
	
	/**
	 * Reads a roof from the scanner
	 * @param s The Scanner with start roof
	 * @return The roof object that is read
	 */
	public static Roof Read(Scanner s)
	{
		s.useDelimiter(" |; |;\r\n");
		ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
		for(int i =0; i<4; i++){
			Point2D.Float p1 = new Point2D.Float();
			p1.x = (float)s.nextInt();
			p1.y = (float)s.nextInt();
			p.add(p1);
		}
		String tex = s.next();

		return new Roof(p, tex);	
	}
	
}
