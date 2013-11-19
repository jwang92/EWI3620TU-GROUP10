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
	private double height = 5;
	private WallList walls;
	private FloorList floors;
	private RoofList roofs;
	
	public Maze(){
		walls = new WallList();
		roofs = new RoofList();
		floors = new FloorList();
		try {
			walls.Read("testlevel2/Walls.txt");
			roofs.Read("testlevel2/Roof.txt");
			floors.Read("testlevel2/Floor.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void display(GL gl){
		ArrayList<Wall> w = walls.getWalls();
		ArrayList<Floor> f = floors.getFloors();
		ArrayList<Roof> r = roofs.getRoofs();
		gl.glDisable(GL.GL_CULL_FACE);
		for(int i = 0; i < w.size(); i++){
			drawWall(gl, w.get(i).getStartx(), w.get(i).getStarty(), w.get(i).getEndx(), w.get(i).getEndy(),w.get(i).getTexture());
		}
		for(int i = 0; i< f.size(); i++){
			drawFloor(gl,f.get(i).getPoints(),f.get(i).getTexture());
		}
		for(int i = 0; i <r.size(); i++){
			drawRoof(gl,r.get(i).getPoints(),r.get(i).getTexture());
		}
		
	}
	
	public void drawWall(GL gl, int sx, int sy, int ex, int ey,String texture){
		ArrayList<Point3D> p = new ArrayList<Point3D>();
		Point3D p1 = new Point3D(); Point3D p2 = new Point3D(); Point3D p3 = new Point3D(); Point3D p4 = new Point3D();
			p1.x = (float) (sx * SQUARE_SIZE);
			p1.y = (float) height;
			p1.z = (float) (sy * SQUARE_SIZE);
			p.add(p1);
			p2.x = (float) (ex * SQUARE_SIZE);
			p2.y = (float) height;
			p2.z = (float) (ey * SQUARE_SIZE);
			p.add(p2);
			p3.x = (float) (ex * SQUARE_SIZE);
			p3.y = (float) 0;
			p3.z = (float) (ey * SQUARE_SIZE);
			p.add(p3);
			p4.x = (float) (sx * SQUARE_SIZE);
			p4.y = (float) 0;
			p4.z = (float) (sy * SQUARE_SIZE);
			p.add(p4);
		int textureID = MainClass.textureNames.lastIndexOf(texture);
		polygonOnScreen(gl,p,textureID);
	}
	
	public void drawFloor(GL gl, ArrayList<Point2D.Float> p2D, String texture){
		ArrayList<Point3D> p3D = new ArrayList<Point3D>();
		for(int i =0; i<p2D.size();i++){
			Point3D point = new Point3D();
			point.x = (float) (p2D.get(i).x * SQUARE_SIZE);
			point.y = 0;
			point.z = (float) (p2D.get(i).y * SQUARE_SIZE);
			p3D.add(point);
		}
		int textureID = MainClass.textureNames.lastIndexOf(texture);
		polygonOnScreen(gl,p3D,textureID);		
	}
	
	public void drawRoof(GL gl, ArrayList<Point2D.Float> p2D, String texture){
		ArrayList<Point3D> p3D = new ArrayList<Point3D>();
		for(int i =0; i<p2D.size();i++){
			Point3D point = new Point3D();
			point.x = (float) (p2D.get(i).x * SQUARE_SIZE);
			point.y = 5;
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
	
	
	public boolean isWall( double x, double z )
	{
		
		ArrayList<Wall> w = walls.getWalls();
						
		double distance;
		
		for(int i = 0; i < w.size(); i++){
			
			distance = distToSegment(x / SQUARE_SIZE, z / SQUARE_SIZE, w.get(i).getStartx(), w.get(i).getStarty(), w.get(i).getEndx(), w.get(i).getEndy());
		
			if(distance < 0.1){
				System.out.println(distance);
				return true;
			}
			
		}
		
		return false;		
		
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