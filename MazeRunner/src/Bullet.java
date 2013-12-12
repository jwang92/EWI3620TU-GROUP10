import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;

public class Bullet extends GameObject implements VisibleObject {
	
	private double speed=2;
	private boolean removal=false;
	private double tx, ty, tz;
	private int teller=0;
	
	public Bullet(double x, double y, double z, Player player){
		super(x,y-0.2,z);
		double h = Math.toRadians(player.getHorAngle());
		double v = Math.toRadians(player.getVerAngle());
		tx = -Math.cos(v)*Math.sin(h);
		tz = -Math.cos(v)*Math.cos(h);
		ty = Math.sin(v);
	}
	
	public void update(int deltaTime, Player player){
		if(checkWall(locationX, locationY,locationZ) || teller>=250){
			removal = true;
		}
		locationX += speed*tx;
		locationY += speed*ty;
		locationZ += speed*tz;
		for(Enemy e: MainClass.enemies){
			if(e.damage(locationX, locationY,locationZ,player.getHorAngle(),50)){
				
				//MainClass.bullets.remove(this);
				removal =true;
			}
		}
		teller++;
	}

	public void display(GL gl) {
		GLUT glut = new GLUT();
		gl.glPushMatrix();
		gl.glTranslated(locationX,locationY,locationZ);
		glut.glutSolidSphere(0.025f, 15, 15);
		gl.glPopMatrix();
	}
	
	public boolean needRemoval(){
		return removal;
	}
	
	public boolean checkWall(double x, double y, double z){
		double d1 = speed*tx/100;
		double d2 = speed*ty/100;
		double d3 = speed*tz/100;
		for(int i =0; i<100; i++){
			if(MainClass.maze.isWall(x+i*d1, y+i*d2, z+i*d3)){
				return true;
			}
		}
		return false;
	}
}
