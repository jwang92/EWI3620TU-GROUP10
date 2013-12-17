import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;

public class Door extends GameObject implements VisibleObject {
	
	private double scale=1;
	public Player player;
	public boolean inArea= false;
	
	public Door(double x, double y, double z){
		super(x,y,z);
	}
	
	public void display(GL gl) {
		checkPlayer();
		gl.glPushMatrix();
		gl.glTranslated(locationX, locationY, locationZ);
		if(!(scale<=0.01)  && inArea){
			scale -=0.01;
		}
		drawDoor(gl,scale);
		gl.glPopMatrix();
		
	}
	
	public void drawDoor(GL gl, double c){
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glBegin(GL.GL_QUADS);
			//onderkant
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 0, 0);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 0, 0.2);
			gl.glVertex3d(0, 0, 0.2);
			
			//bovenkant
			gl.glVertex3d(0, 5.0, 0);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 5.0, 0);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 5.0, 0.2);
			gl.glVertex3d(0, 5.0, 0.2);
			
			//voorkant
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 0, 0);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 5.0, 0);
			gl.glVertex3d(0, 5.0, 0);
			
			//achterkant
			gl.glVertex3d(0, 0, 0.2);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 0, 0.2);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 5.0, 0.2);
			gl.glVertex3d(0, 5.0, 0.2);
			
			
			gl.glColor3f(0.2f, 0.2f, 0.2f);
			//zijkant 1
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(0, 0, 0.2);
			gl.glVertex3d(0, 5.0, 0.2);
			gl.glVertex3d(0, 5.0, 0);
			
			//zijkant 2
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 0, 0);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 0, 0.2);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 5.0, 0.2);
			gl.glVertex3d(MainClass.maze.SQUARE_SIZE*c, 5.0, 0);
		gl.glEnd();
		
		gl.glDisable(GL.GL_COLOR_MATERIAL);
	}
	
	public void checkPlayer(){
		if(MainClass.doorSwitch.getPressed()){
			if(player.locationX >=locationX && player.locationX<= locationX+5
					&& player.locationZ >=locationZ && player.locationZ<= locationZ+5){
				inArea =  true;
			}
		}
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	

}
