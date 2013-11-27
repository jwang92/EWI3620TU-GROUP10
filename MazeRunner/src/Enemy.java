import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;


public class Enemy extends GameObject implements VisibleObject {
	private Maze maze; 										// The maze.
	private double newX, newZ;
	private double speed = 0.001;
	private Model m ;
	private int displayList;
	
	public Enemy(double x, double y, double z){
		super(x, y, z);
		try {
			m = OBJLoader.loadModel((new File("3d_object/lion.obj")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public boolean checkWall(double x, double z, double dT){
		double d = 2.05; 		//distance from the wall
		boolean res = false;
		
		for(int i = 0; i < 360; i = i + 45)
			if(maze.isWall( x+d*Math.sin(i*Math.PI/180) , z+d*Math.cos(i*Math.PI/180) , locationY))
				res = true;
		
		return res;
	}
	
	public void genDisplayList(GL gl){
        displayList = OBJLoader.createDisplayList(m, gl);
	}
		
	public void update(int deltaTime, Player player){
		
		double dX = player.locationX - locationX;
		if(dX > 0)
			dX = speed * deltaTime;
		if(dX < 0)
			dX = -1 * speed * deltaTime;
		
		newX = locationX + dX;
				
		double dZ = player.locationZ - locationZ;
		if(dZ > 0)
			dZ = speed * deltaTime;
		if(dZ < 0)
			dZ = -1 * speed * deltaTime;
		
		newZ = locationZ + dZ;
		
		if(!checkWall(newX, newZ, deltaTime)){
			locationX = newX;
			locationZ = newZ;
		}else if(!checkWall(newX, locationZ, deltaTime)){
			locationX = newX;
		}else if(!checkWall(locationX, newZ, deltaTime)){
			locationZ = newZ;
		}
		
	}

	public void display(GL gl) {
		//GLUT glut = new GLUT();
		
		gl.glColor3f(1, 0, 0);
		
		gl.glPushMatrix();
		gl.glTranslated(locationX, locationY, locationZ);
		if(displayList == 0){
			gl.glCallList(1);	
		}
		else{
			gl.glCallList(displayList);
		}
		//glut.glutSolidSphere(2.0d, 10, 10);
		gl.glPopMatrix();
		
	}
	
	public void getMaze(Maze maze){
		this.maze = maze;
	}
	

}
