package Maze;
import javax.media.opengl.GL;

import GameObject.GameObject;
import GameObject.Player;
import GameObject.VisibleObject;
import Main.MainClass;

public class Door extends GameObject implements VisibleObject {
	
	public double scale=1.0;
	public Player player;
	public boolean inArea= false;
	public double angle=0;
	public double dx, dy,dz;
	public double initAngle,totalAngle;
	public double c, signD=1, lengte;	
	public int doorGeluid = 0;
	public DoorSwitch doorSwitch;
	
	/**
	 * constructor van Door
	 * @param x  x coordinaat beginpunt
	 * @param y  y coordinaat ""
	 * @param z  z coordinaat ""
	 * @param x2 x coordinaat eindpunt
	 * @param y2 y coordinaat ""
	 * @param z2 z coordinaat ""
	 * @param switchX x coordinaat van switch
	 * @param switchY y coordinaat "" 
	 * @param switchZ z coordinaat ""
	 * @param m
	 */
	public Door(double x, double y, double z, double x2, double y2, double z2, double switchX, double switchY, double switchZ, MainClass m){
		super(x,y,z, m);
		switchX += main.maze.SQUARE_SIZE/2.0;
		switchZ += main.maze.SQUARE_SIZE/2.0;
		//maak switch
		doorSwitch = new DoorSwitch(switchX,switchY,switchZ,main);
		//aftand tussen begin en eind punt
		dx=x2-x;
		dy=y2-y;
		dz=z2-z;
		
		//richting deur
		double inP = x2-x;
		double lengteV = 1;
		double lengteW = Math.sqrt(Math.pow(x2-x, 2)+Math.pow(z2-z, 2));
		double test = inP/Math.max(lengteV*lengteW, 00001);
		initAngle= Math.acos(test)*180/Math.PI;
		
		//checkwelke kant op deur staat
		if(z2>z){
			initAngle= -initAngle;
		}
		totalAngle = initAngle;
		lengte = Math.sqrt(Math.pow(dx, 2)+Math.pow(dz, 2));
		
	}
	
	public void display(GL gl) {
		checkPlayer();
		gl.glPushMatrix();
		
		//translate model
		gl.glTranslated(locationX, locationY, locationZ);
		
		//pas angle aan voor animatie en speel geluid af
		if(angle<90 && angle>-90  && inArea){
			angle +=signD*0.5;
			totalAngle +=signD*0.5;
			if(doorGeluid==0){
				player.sound.creakingDoor();
				doorGeluid++;
			}
			
		}
		gl.glRotated(angle,0,1,0);
		drawDoor(gl);
		gl.glPopMatrix();

		doorSwitch.display(gl);
	}
	
	/**
	 * tekent deur met texture erop
	 * @param gl
	 */
	public void drawDoor(GL gl){
		
		gl.glEnable(GL.GL_COLOR_MATERIAL);

		double x = dx+0.2*Math.sin(initAngle*Math.PI/180);
		double y = dy;
		double z = dz+0.2*Math.cos(initAngle*Math.PI/180);
		
		int textureID = main.textureNames.lastIndexOf("textures/door.png");
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		main.textures.get(textureID).bind();
		
		gl.glBegin(GL.GL_QUADS);
			//onderkant
			gl.glTexCoord2f(1, 0); gl.glVertex3d(0, 0, 0); 
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x, 0, 0); 
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x, 0, z);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(0, 0, z);
			
			//bovenkant
			gl.glTexCoord2f(1, 0); gl.glVertex3d(0, y, 0);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x, y, 0);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x, y, z);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(0, y, z);
							
			//voorkant
			gl.glTexCoord2f(1, 0); gl.glVertex3d(0, 0, 0); 
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x, 0, 0);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x, y, 0); 
			gl.glTexCoord2f(1, 1); gl.glVertex3d(0, y, 0);
		
			//achterkant
			gl.glTexCoord2f(1, 0); gl.glVertex3d(0, 0, z);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x, 0, z);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x, y, z);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(0, y, z);
			
			//zijkant 1
			gl.glTexCoord2f(1, 0); gl.glVertex3d(0, 0, 0);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(0, 0, z);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(0, y, z);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(0, y, 0);
			
			//zijkant 2
			gl.glTexCoord2f(1, 0); gl.glVertex3d(x, 0, 0);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x, 0, z);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x, y, z);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(x, y, 0);
		gl.glEnd();
		
		main.textures.get(textureID).disable();
		gl.glDisable(GL.GL_TEXTURE_2D);
		
		gl.glDisable(GL.GL_COLOR_MATERIAL);
	}
	
	/**
	 * checkt collision met player en als deur open moet dan gaat het altijd van player af open
	 */
	public void checkPlayer(){
		//check of deur open kan
		if(doorSwitch.getPressed()){
			//checkof speler in de buurt en 
			if(player.locationX >=locationX+0.5*dx-5 && player.locationX<= locationX+0.5*dx+5
					&& player.locationZ >=locationZ+0.5*dz-5 && player.locationZ<= locationZ+0.5*dz+5){
				inArea =  true;
				if(doorGeluid==0){
					//zorgt ervoor dat deur van player af open gaat
					double s= (player.locationX*dz-player.locationZ*dx+c)/
							lengte;
					if(s>0){
						signD=-1;
					}
				}
			}
		}
	}
	
	public void setPlayer(Player player){
		this.player = player;
		doorSwitch.setPlayer(player);
		//variabel nodig voor berekening: welke kant deur open moet gaan
		c=-dz*locationX +dx*locationZ;
	}
	
	public double getStartX(){
		return locationX/main.maze.SQUARE_SIZE;
	}
	
	public double getStartZ(){
		return locationZ/main.maze.SQUARE_SIZE;
	}
	
	public double getEndX(){
		return (locationX+lengte*Math.cos(totalAngle*Math.PI/180)) /main.maze.SQUARE_SIZE;
	}
	
	public double getEndZ(){
		return (locationZ-lengte*Math.sin(totalAngle*Math.PI/180))/main.maze.SQUARE_SIZE;
	}
			
	

}
