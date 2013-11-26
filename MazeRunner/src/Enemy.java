import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;


public class Enemy extends GameObject implements VisibleObject {
	private Maze maze; 										// The maze.
//	private double newX, newZ;
	private double speed = 0.001;
	
	public Enemy(double x, double y, double z){
		super(x, y, z);
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
//	public boolean checkWall(double x, double z, double dT){
//		double d = 1 * speed * dT; 		//distance from the wall
//		boolean res = false;
//		
//		for(int i = 0; i < 360; i = i + 45)
//			if(maze.isWall( x+d*Math.sin(i*Math.PI/180) , z+d*Math.cos(i*Math.PI/180) ))
//				res = true;
//		
//		return res;
//	}
	
	public void update(int deltaTime, Player player){
		
		double dX = player.locationX - locationX;
		if(dX > 0)
			dX = speed * deltaTime;
		if(dX < 0)
			dX = -1 * speed * deltaTime;
		
		locationX += dX;
		
//		double dY = player.locationY - locationY;
//		dY = Math.abs(dY) * 0.1;
//		
//		locationY += dY;
		
		double dZ = player.locationZ - locationZ;
		if(dZ > 0)
			dZ = speed * deltaTime;
		if(dZ < 0)
			dZ = -1 * speed * deltaTime;
		
		locationZ += dZ;

		
	}

	public void display(GL gl) {
		GLUT glut = new GLUT();
		
		gl.glColor3f(1, 0, 0);
		
		gl.glPushMatrix();
		gl.glTranslated( locationX, locationY, locationZ);
		glut.glutSolidCube(2.0f);
		gl.glPopMatrix();
		
	}
	
	public void getMaze(Maze maze){
		this.maze = maze;
	}
	

}
