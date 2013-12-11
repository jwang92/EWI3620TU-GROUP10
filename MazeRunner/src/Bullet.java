import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;

public class Bullet extends GameObject implements VisibleObject {
	
	private double speed=1;
	private boolean removal=false;
	
	public Bullet(double x, double y, double z){
		super(x,y,z);
	}
	
	public void update(int deltaTime, Player player){
		double h = Math.toRadians(player.getHorAngle());
		double v = Math.toRadians(player.getVerAngle());
		double tx = -Math.cos(v)*Math.sin(h);
		double tz = -Math.cos(v)*Math.cos(h);
		double ty = Math.sin(v);
		locationX += speed*tx;
		locationY += speed*ty;
		locationZ += speed*tz;
		for(Enemy e: MainClass.enemies){
			if(e.hitpointCheck(locationX, locationY,locationZ)){
				e.damage(locationX, locationY,locationZ,player.getHorAngle(),50);
				//MainClass.bullets.remove(this);
				removal =true;
			}
		}
	}

	public void display(GL gl) {
		GLUT glut = new GLUT();
		gl.glPushMatrix();
		gl.glTranslated(locationX,locationY,locationZ);
		glut.glutSolidSphere(0.1f, 15, 15);
		gl.glPopMatrix();
	}
	
	public boolean needRemoval(){
		return removal;
	}
}
