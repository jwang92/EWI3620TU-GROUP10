package Maze;
import javax.media.opengl.GL;

import GameObject.GameObject;
import GameObject.Player;
import GameObject.VisibleObject;


public class DoorSwitch extends GameObject implements VisibleObject {
	
	public Player player;
	private boolean pressed = false;
	
	public DoorSwitch(double x, double y, double z){
		super(x,y,z);
	}

	public void display(GL gl) {
		checkPlayer();
		gl.glPushMatrix();
		gl.glTranslated(locationX, locationY, locationZ);
		drawSwitch(gl, 0.5, 0.2, 1);
		if(!pressed){
			gl.glTranslated(0.05, 0.2, 0.05);
			drawSwitch(gl, 0.4, 0.1, 2);
		}
		else{
			gl.glTranslated(0.05, 0.1, 0.05);
			drawSwitch(gl, 0.4, 0.11, 0);
		}
		gl.glPopMatrix();

	}
	
	public void drawSwitch(GL gl, double sizeWidth, double sizeHeight, int type){
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		if(type==1){
			gl.glColor3f(0.3f, 0.3f, 0.3f);
		}
		else if(type == 2){
			gl.glColor3f(0.3f, 0.0f, 0.0f);
		}
		else{
			gl.glColor3f(0.0f, 0.3f, 0.0f);
		}
		gl.glBegin(GL.GL_QUADS);
			//onderkant
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(sizeWidth, 0, 0);
			gl.glVertex3d(sizeWidth, 0, sizeWidth);
			gl.glVertex3d(0, 0, sizeWidth);
			
			//bovenkant
			gl.glVertex3d(0, sizeHeight, 0);
			gl.glVertex3d(sizeWidth, sizeHeight, 0);
			gl.glVertex3d(sizeWidth, sizeHeight, sizeWidth);
			gl.glVertex3d(0, sizeHeight, sizeWidth);
			
			if(type ==1){
				gl.glColor3f(0.2f, 0.2f, 0.2f);
			}
			else if(type == 2){
				gl.glColor3f(0.2f, 0.0f, 0.0f);
			}
			else{
				gl.glColor3f(0.0f, 0.2f, 0.0f);
			}
			//voorkant
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(sizeWidth, 0, 0);
			gl.glVertex3d(sizeWidth, sizeHeight, 0);
			gl.glVertex3d(0, sizeHeight, 0);
			
			//achterkant
			gl.glVertex3d(0, 0, sizeWidth);
			gl.glVertex3d(sizeWidth, 0, sizeWidth);
			gl.glVertex3d(sizeWidth, sizeHeight, sizeWidth);
			gl.glVertex3d(0, sizeHeight, sizeWidth);
			
			
			//zijkant 1
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(0, 0, sizeWidth);
			gl.glVertex3d(0, sizeHeight, sizeWidth);
			gl.glVertex3d(0, sizeHeight, 0);
			
			//zijkant 2
			gl.glVertex3d(sizeWidth, 0, 0);
			gl.glVertex3d(sizeWidth, 0, sizeWidth);
			gl.glVertex3d(sizeWidth, sizeHeight, sizeWidth);
			gl.glVertex3d(sizeWidth, sizeHeight, 0);
		gl.glEnd();
		
		gl.glColor3f(1f, 1f, 1f);
		gl.glDisable(GL.GL_COLOR_MATERIAL);
	}
	
	public void setPlayer(Player player){
		this.player=player;
	}
	
	public void checkPlayer(){
		if(player.locationX >=locationX+0.25-0.75 && player.locationX<= locationX+0.25+0.75
				&& player.locationZ >=locationZ+0.25-0.75 && player.locationZ<= locationZ+0.25+0.75){
			pressed =  true;
		}
	}
	
	public boolean getPressed(){
		return pressed;
	}

}
