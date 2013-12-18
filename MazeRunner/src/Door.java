import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;

public class Door extends GameObject implements VisibleObject {
	
	public double scale=1.0;
	public Player player;
	public boolean inArea= false;
	public double angle=0;
	public double dx, dy,dz;
	public double initAngle,totalAngle;
	double c, signD=1, lengte;	
	
	public Door(double x, double y, double z, double x2, double y2, double z2){
		super(x,y,z);
		dx=x2-x;
		dy=y2-y;
		dz=z2-z;
		double inP = x2-x;
		double lengteV = 1;
		double lengteW = Math.sqrt(Math.pow(x2-x, 2)+Math.pow(z2-z, 2));
		double test = inP/Math.max(lengteV*lengteW, 00001);
		initAngle= Math.acos(test)*180/Math.PI;
		if(z2>z){
			initAngle= -initAngle;
		}
		totalAngle = initAngle;
		lengte = Math.sqrt(Math.pow(dx, 2)+Math.pow(dz, 2));
	}
	
	public void display(GL gl) {
		checkPlayer();
		gl.glPushMatrix();
		gl.glTranslated(locationX, locationY, locationZ);
//		if(!(scale<=0.0)  && inArea){
//			scale -=0.01;
//		}
		if(angle<90 && angle>-90  && inArea){
			angle +=signD;
			totalAngle +=signD;
		}
		gl.glRotated(angle,0,1,0);
		drawDoor(gl);
		gl.glPopMatrix();

	}
	
	public void drawDoor(GL gl){
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		gl.glColor3f(0.3f, 0.3f, 0.3f);
		double x = dx+0.2*Math.sin(initAngle*Math.PI/180);
		double y = dy;
		double z = dz+0.2*Math.cos(initAngle*Math.PI/180);
		gl.glBegin(GL.GL_QUADS);
			//onderkant
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(x, 0, 0);
			gl.glVertex3d(x, 0, z);
			gl.glVertex3d(0, 0, z);
			
			//bovenkant
			gl.glVertex3d(0, y, 0);
			gl.glVertex3d(x, y, 0);
			gl.glVertex3d(x, y, z);
			gl.glVertex3d(0, y, z);
			
			//voorkant
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(x, 0, 0);
			gl.glVertex3d(x, y, 0);
			gl.glVertex3d(0, y, 0);
			
			//achterkant
			gl.glVertex3d(0, 0, z);
			gl.glVertex3d(x, 0, z);
			gl.glVertex3d(x, y, z);
			gl.glVertex3d(0, y, z);
			
			gl.glColor3f(0.28f, 0.28f, 0.28f);
			//zijkant 1
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(0, 0, z);
			gl.glVertex3d(0, y, z);
			gl.glVertex3d(0, y, 0);
			
			//zijkant 2
			gl.glVertex3d(x, 0, 0);
			gl.glVertex3d(x, 0, z);
			gl.glVertex3d(x, y, z);
			gl.glVertex3d(x, y, 0);
		gl.glEnd();
		
		gl.glDisable(GL.GL_COLOR_MATERIAL);
	}
	
	public void checkPlayer(){
		System.out.println(dx + " " + " " + dz);
		if(MainClass.doorSwitch.getPressed()){
			if(player.locationX >=locationX+0.5*dx-5 && player.locationX<= locationX+0.5*dx+5
					&& player.locationZ >=locationZ+0.5*dz-5 && player.locationZ<= locationZ+0.5*dz+5){
				inArea =  true;
				double s= (player.locationX*dz-player.locationZ*dx+c)/
						lengte;
				if(s>0){
					signD=-1;
				}
				
			}
		}
	}
	
	public void setPlayer(Player player){
		this.player = player;
		c=-dz*locationX +dx*locationZ;
	}
	
	public double getStartX(){
		return locationX/MainClass.maze.SQUARE_SIZE;
	}
	
	public double getStartZ(){
		return locationZ/MainClass.maze.SQUARE_SIZE;
	}
	
	public double getEndX(){
		return (locationX+lengte*Math.cos(totalAngle*Math.PI/180)) /MainClass.maze.SQUARE_SIZE;
	}
	
	public double getEndZ(){
		return (locationZ-lengte*Math.sin(totalAngle*Math.PI/180))/MainClass.maze.SQUARE_SIZE;
	}
			
	

}
