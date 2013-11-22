import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;


public class Enemy extends GameObject implements VisibleObject {
	private double speed;
	private Maze maze; 										// The maze.
	private double newX, newZ;
	
	public Enemy(double x, double y, double z){
		super(x, y, z);
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public boolean checkWall(double x, double z, double dT){
		double d = 1 * speed * dT; 		//distance from the wall
		boolean res = false;
		
		for(int i = 0; i < 360; i = i + 45)
			if(maze.isWall( x+d*Math.sin(i*Math.PI/180) , z+d*Math.cos(i*Math.PI/180) , locationY ))
				res = true;
		
		return res;
	}
	
	public void update(int deltaTime){
	}

	public void display(GL gl) {
		GLUT glut = new GLUT();
		
		gl.glPushMatrix();
		gl.glTranslated((int)locationX, (int)locationY, (int)locationZ);
		glut.glutSolidCube(10.0f);
		gl.glPopMatrix();
		
	}
	
	public void getMaze(Maze maze){
		this.maze=maze;
	}
	

}
