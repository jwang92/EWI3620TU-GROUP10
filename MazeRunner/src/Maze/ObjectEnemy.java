package Maze;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;



public class ObjectEnemy {

	private ArrayList<Point2D.Float> points;
	private String model;
	
	public ObjectEnemy(){
		
		points = new ArrayList<Point2D.Float>();
		model = "";
		
	}
	
	public ObjectEnemy(ArrayList<Point2D.Float> p, String tex){
		
		points = p;
		model = tex;
		
	}
	
	public ArrayList<Point2D.Float> getPoints(){
		return points;
	}
	
	public String getModel(){
		return model;
	}
	
	/**
	 * reads a enemy from file
	 * @param s scanner
	 * @return the enemy object that is read
	 */
	public static ObjectEnemy Read(Scanner s){
		
		s.useDelimiter(" |; |;\r\n");
		ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
		for(int i = 0; i < 4; i++){
			Point2D.Float tempP = new Point2D.Float();
			tempP.x = s.nextFloat();
			tempP.y = s.nextFloat();
			p.add(tempP); 
		}
		String model = s.next();
		return new ObjectEnemy(p,model);
	}
	
	/**
	 * changes string to used file format
	 * @return
	 */
	public String toFileFormat()
	{
		String res = "Enemy: ";
		for(int i =0; i<points.size();i++){
			res = res + (int)points.get(i).x + " " + (int)points.get(i).y + "; ";
		}
		res = res + model + ";\r\n";
		return res;
	}
	
}