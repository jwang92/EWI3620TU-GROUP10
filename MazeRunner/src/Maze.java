import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;


public class Maze  implements VisibleObject {
	
	public final double SQUARE_SIZE = 5;

	private WallList walls;
	
	public void display(GL gl){
		
		walls = new WallList();
		try {
			walls.Read("testlevel2/Walls.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<Wall> w = walls.getWalls();
		
		for(int i = 0; i < w.size(); i++){
			
			drawWall(gl, w.get(i).getStartx(), w.get(i).getStarty(), w.get(i).getEndx(), w.get(i).getEndy());
			
			
		}
		
		drawFloor(gl);
		
	}
	
	public void drawWall(GL gl, int sx, int sy, int ex, int ey){
		
		float wallColour[] = { (float) Math.random(), (float) Math.random(), (float) Math.random() };				// The floor is blue.
        gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, wallColour, 0);	// Set the materials used by the floor.
        gl.glDisable(GL.GL_CULL_FACE);
        gl.glNormal3d(0, 1, 0);
		gl.glBegin(GL.GL_QUADS);
	        gl.glVertex3d(sx * SQUARE_SIZE, 5, sy * SQUARE_SIZE);
	        gl.glVertex3d(ex * SQUARE_SIZE, 5, ey * SQUARE_SIZE);
	        gl.glVertex3d(ex * SQUARE_SIZE, 0, ey * SQUARE_SIZE);
	        gl.glVertex3d(sx * SQUARE_SIZE, 0, sy * SQUARE_SIZE);		
		gl.glEnd();	
		
	}
	
	public void drawFloor(GL gl){
		
		File file = new File("textures/wood.png");
		TextureData data = null;
		try {
			data = TextureIO.newTextureData(file, false, "png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Texture woodTexture = TextureIO.newTexture(data);
		woodTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		woodTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		woodTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		woodTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		TextureCoords coords = woodTexture.getImageTexCoords();
		
		
		
		float wallColour[] = { 1.0f, 1.0f, 1.0f};				
        gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);	// Set the materials used by the floor.
        
        gl.glNormal3d(0, 1, 0);
        gl.glEnable(GL.GL_TEXTURE_2D);
        
        woodTexture.bind();
        
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        

        
		gl.glBegin(GL.GL_QUADS);
        	gl.glTexCoord2f(0, 0);
	        gl.glVertex3d(0, 0, 0);
	        gl.glTexCoord2f(0, 10);
	        gl.glVertex3d(0, 0, 50);
	        gl.glTexCoord2f(10, 10);
	        gl.glVertex3d(50, 0, 50);
	        gl.glTexCoord2f(10, 0);
	        gl.glVertex3d(50, 0, 0);
		gl.glEnd();	
		
		woodTexture.disable();
		
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