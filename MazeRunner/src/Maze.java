import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;


public class Maze  implements VisibleObject {
	
	public final double SQUARE_SIZE = 5;
	private int height = 5;
	private WallList walls1;
	private FloorList floors1;
	private RoofList roofs1;
	private ObjectList objects1;
	
	private WallList walls2;
	private FloorList floors2;
	private RoofList roofs2;
	
	public Maze(){
		walls1 = new WallList();
		roofs1 = new RoofList();
		floors1 = new FloorList();
		objects1 = new ObjectList();
		
		walls2 = new WallList();
		roofs2 = new RoofList();
		floors2 = new FloorList();
		try {
			walls1.Read("grootlevel/Floor 1/Walls.txt");
			roofs1.Read("grootlevel/Floor 1/Roof.txt");
			floors1.Read("grootlevel/Floor 1/Floor.txt");
			objects1.Read("grootlevel/Floor 1/Objects.txt");
			
			walls2.Read("grootlevel/Floor 2/Walls.txt");
			roofs2.Read("grootlevel/Floor 2/Roof.txt");
			floors2.Read("grootlevel/Floor 2/Floor.txt");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void display(GL gl){
		ArrayList<Wall> w1 = walls1.getWalls();
		ArrayList<Floor> f1 = floors1.getFloors();
		ArrayList<Roof> r1 = roofs1.getRoofs();
		ArrayList<Object> o1 = objects1.getObjects();
		
		ArrayList<Wall> w2 = walls2.getWalls();
		ArrayList<Floor> f2 = floors2.getFloors();
		ArrayList<Roof> r2 = roofs2.getRoofs();
		
		gl.glDisable(GL.GL_CULL_FACE);
		
		for(int i = 0; i < w1.size(); i++){
			drawWall(gl, w1.get(i).getStartx(), w1.get(i).getStarty(), w1.get(i).getEndx(), w1.get(i).getEndy(),w1.get(i).getTexture(), 0);
		}
		for(int i = 0; i< f1.size(); i++){
			drawFloor(gl,f1.get(i).getPoints(),f1.get(i).getTexture(), 0);
		}
		for(int i = 0; i <r1.size(); i++){
			drawRoof(gl,r1.get(i).getPoints(),r1.get(i).getTexture(), 0);
		}
		for(int i = 0; i < o1.size(); i++){
			
			if(o1.get(i) instanceof ObjectRamp){
				
				ObjectRamp t = (ObjectRamp) o1.get(i);
							 
				drawRamp(gl, t.getPoints(), 0);
				
			}
			
		}
		
		for(int i = 0; i < w2.size(); i++){
			drawWall(gl, w2.get(i).getStartx(), w2.get(i).getStarty(), w2.get(i).getEndx(), w2.get(i).getEndy(),w2.get(i).getTexture(), height);
		}
		for(int i = 0; i< f2.size(); i++){
			drawFloor(gl,f2.get(i).getPoints(),f2.get(i).getTexture(), height);
		}
		for(int i = 0; i <r2.size(); i++){
			drawRoof(gl,r2.get(i).getPoints(),r2.get(i).getTexture(), height);
		}
		
	}
	
	public void drawRamp(GL gl, ArrayList<Point2D.Float> points, int z){
		
		ArrayList<Point3D> p3D = new ArrayList<Point3D>();
		
		for(int i = 0; i < points.size(); i++){
			
			Point3D point = new Point3D();
			point.x = (float) (points.get(i).x * SQUARE_SIZE);
			point.z = (float) (points.get(i).y * SQUARE_SIZE);
			point.y = (float) z;
			if(i > 1)
				point.y = (float) z + 5;
			
			p3D.add(point);
			
		}
		//int textureID = MainClass.textureNames.lastIndexOf(texture);
		polygonOnScreen(gl,p3D, 1);	
		
	}
	
	public void drawWall(GL gl, int sx, int sy, int ex, int ey,String texture, int z){
		ArrayList<Point3D> p = new ArrayList<Point3D>();
		Point3D p1 = new Point3D(); Point3D p2 = new Point3D(); Point3D p3 = new Point3D(); Point3D p4 = new Point3D();
			p1.x = (float) (sx * SQUARE_SIZE);
			p1.y = (float) (z + height);
			p1.z = (float) (sy * SQUARE_SIZE);
			p.add(p1);
			p2.x = (float) (ex * SQUARE_SIZE);
			p2.y = (float) (z + height);
			p2.z = (float) (ey * SQUARE_SIZE);
			p.add(p2);
			p3.x = (float) (ex * SQUARE_SIZE);
			p3.y = (float) z;
			p3.z = (float) (ey * SQUARE_SIZE);
			p.add(p3);
			p4.x = (float) (sx * SQUARE_SIZE);
			p4.y = (float) z;
			p4.z = (float) (sy * SQUARE_SIZE);
			p.add(p4);
		int textureID = MainClass.textureNames.lastIndexOf(texture);
	
		polygonOnScreen(gl,p,textureID);
	}
	
	public void drawFloor(GL gl, ArrayList<Point2D.Float> p2D, String texture, int z){
		ArrayList<Point3D> p3D = new ArrayList<Point3D>();
		for(int i =0; i<p2D.size();i++){
			Point3D point = new Point3D();
			point.x = (float) (p2D.get(i).x * SQUARE_SIZE);
			point.y = (float) z;
			point.z = (float) (p2D.get(i).y * SQUARE_SIZE);
			p3D.add(point);
		}
		int textureID = MainClass.textureNames.lastIndexOf(texture);
		polygonOnScreen(gl,p3D,textureID);		
	}
	
	public void drawRoof(GL gl, ArrayList<Point2D.Float> p2D, String texture, int z){
		ArrayList<Point3D> p3D = new ArrayList<Point3D>();
		for(int i =0; i<p2D.size();i++){
			Point3D point = new Point3D();
			point.x = (float) (p2D.get(i).x * SQUARE_SIZE);
			point.y = (float) z + 5;
			point.z = (float) (p2D.get(i).y * SQUARE_SIZE);
			p3D.add(point);
		}
		int textureID = MainClass.textureNames.lastIndexOf(texture);
		polygonOnScreen(gl,p3D,textureID);		
	}
	
	
	
	private void polygonOnScreen(GL gl, ArrayList<Point3D> p, int textureID){
		//Set the color
		float wallColour[] = { 1.0f, 1.0f, 1.0f};				
        gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);	// Set the materials used by the floor.
		
        gl.glNormal3d(0, 1, 0);
        
		//Enable textures
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		//Apply texture
		MainClass.textures.get(textureID).getTarget();
		//brickTexture.enable();
		MainClass.textures.get(textureID).bind();		
		gl.glBegin(GL.GL_QUADS);
    		gl.glTexCoord2f(0, 0);
    		gl.glVertex3d(p.get(0).x,p.get(0).y,p.get(0).z);
    		gl.glTexCoord2f(0, 10);
    		gl.glVertex3d(p.get(1).x,p.get(1).y,p.get(1).z);
    		gl.glTexCoord2f(10, 10);
    		gl.glVertex3d(p.get(2).x,p.get(2).y,p.get(2).z);
    		gl.glTexCoord2f(10, 0);
    		gl.glVertex3d(p.get(3).x,p.get(3).y,p.get(3).z);		
		
		gl.glEnd();
		
		//Disable texture
		MainClass.textures.get(textureID).disable();
	}
	
	
	public boolean isWall( double x, double z, double y)
	{
		
		ArrayList<Wall> w;
		if(y < 5)
			w = walls1.getWalls();
		else
			w = walls2.getWalls();
						
		double distance;
		
		for(int i = 0; i < w.size(); i++){
			
			distance = distToSegment(x / SQUARE_SIZE, z / SQUARE_SIZE, w.get(i).getStartx(), w.get(i).getStarty(), w.get(i).getEndx(), w.get(i).getEndy());
		
			if(distance < 0.1){
				return true;
			}
			
		}
		
		return false;		
		
	}
	
	public void isRamp(double x, double z, double y){
		
		ArrayList<Object> o;
		o = objects1.getObjects();
				
		for(int i = 0; i < o.size(); i++){
			
			if(o.get(i) instanceof ObjectRamp){
				
				ObjectRamp tempRamp = (ObjectRamp) o.get(i);
				ArrayList<Point2D.Float> points = tempRamp.getPoints();
				
				
				
			}
			
		}
		
		
	}
		
	public double dist2(double vx, double vy, double wx, double wy) { return Math.pow(vx - wx, 2) + Math.pow(vy - wy, 2); }
	
	public double distToSegmentSquared(double px, double py, double vx, double vy, double wx, double wy) {
	  double l2 = dist2(vx, vy, wx, wy);
	  if (l2 == 0) return dist2(px, py, vx, vy);
	  double t = ((px - vx) * (wx - vx) + (py - vy) * (wy - vy)) / l2;
	  if (t < 0) return dist2(px, py, vx, vy);
	  if (t > 1) return dist2(px, py, wx, wy);
	  return dist2(px, py, vx + t * (wx - vx), vy + t * (wy - vy));
	}
	
	public double distToSegment(double px, double py, double vx, double vy, double wx, double wy) { return Math.sqrt(distToSegmentSquared(px, py, vx, vy, wx, wy)); }
	
	
}