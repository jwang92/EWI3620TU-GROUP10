import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;

public class Upgrade extends GameObject implements VisibleObject{
	
	public static boolean pickedUp=false;
	
	public Upgrade(double x, double y, double z){
		super(x,y,z);
	}
	
	public void update(Player player){
		if (player.locationX>locationX-2 && player.locationX <locationX+2 &&player.locationZ>locationZ-2 && player.locationZ <locationZ+2){
			MainClass.sword = new Sword(player.locationX,player.locationY,player.locationZ,true, true);
			MainClass.sword.setMaze(MainClass.maze);
			MainClass.sword.setPlayer(MainClass.player);
			pickedUp=true;
		}
	}

	@Override
	public void display(GL gl) {
		if(!pickedUp){
			GLUT glut = new GLUT();
			gl.glPushMatrix();
			gl.glTranslated(locationX,locationY,locationZ);
			glut.glutSolidCube(1.0f);
			gl.glPopMatrix();
		}
		else
			MainClass.sword.genVBO(gl);
	}

}
