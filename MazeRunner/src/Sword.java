import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;


public class Sword extends GameObject implements VisibleObject {
	private Maze maze; 										// The maze.
	private double newX, newZ;
	private double speed = 0.0015;
	private Model m ;
	private int displayList;
	private Player player;
	public int attackCounter=0;
	
	public Sword(double x, double y, double z){
		super(x, y, z);
		try {
			m = OBJLoader.loadModel((new File("3d_object/sword.obj")));
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
		double d = 2.0d; 		//distance from the wall
		boolean res = false;
		
		for(int i = 0; i < 360; i = i + 15)
			if(maze.isWall( x+d*Math.sin(i*Math.PI/180) , locationY , z-0.8f+d*Math.cos(i*Math.PI/180) ))
				res = true;
		
		return res;
	}
	
	public void genDisplayList(GL gl){
        displayList = OBJLoader.createDisplayList(m, gl);
	}
		
	public void update(int deltaTime, Player player){
		locationX=player.locationX;
		locationY=player.locationY-0.8;
		locationZ=player.locationZ;
	}

	public void display(GL gl) {
		//GLUT glut = new GLUT();
		gl.glColor3f(1, 0, 0);
		
		
		if(player.control.getAttack() && attackCounter==18){
			attackCounter=0;
			player.control.setAttack(false);
		}
		
		gl.glPushMatrix();
		gl.glTranslated(locationX-Math.sin(player.getHorAngle()*Math.PI/180), 
				locationY + Math.sin(player.getVerAngle()*Math.PI/180), 
				locationZ-Math.cos(player.getHorAngle()*Math.PI/180));
		gl.glRotated(player.getHorAngle(), 0, 1, 0);
		gl.glRotated(player.getVerAngle(), 1,0 ,0);
		if(player.control.getAttack()){
			gl.glRotated(-5*attackCounter,1,0,0);
			attackCounter +=1;
			if(attackCounter==18){
				MainClass.enemy.damage(player.getLocationX(), player.getLocationY(), player.getLocationZ(), player.getHorAngle());
			}
				
		}
		
		if(displayList == 0){
			gl.glCallList(1);	
		}
		else{
			gl.glCallList(displayList);
		}
		//glut.glutSolidSphere(2.0d, 10, 10);
		gl.glPopMatrix();
		
	}
	
	public void setMaze(Maze maze){
		this.maze = maze;
	}

	public void setPlayer(Player player){
		this.player=player;
	}
}
