package Maze;
import java.awt.geom.Point2D;
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
	
	/**
	 * changes string to used file format
	 * @return
	 */
	public String toFileFormat()
	{
		
		return type + ": " + (int) p.x + " " + (int) p.y + ";\r\n";
		
	}
	
	/**
	 * reads a pickup from file
	 * @param s scanner
	 * @return the pickup object that is read
	 */
	public static Pickup Read(Scanner s){
		
		s.useDelimiter(": | |; |;\r\n");
		
		int t = s.nextInt();

		
		Point2D.Float p = new Point2D.Float(s.nextInt(), s.nextInt());
		
		return new Pickup(p, t);
	}

}
