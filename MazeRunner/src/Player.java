/**
 * Player represents the actual player in MazeRunner.
 * <p>
 * This class extends GameObject to take advantage of the already implemented location 
 * functionality. Furthermore, it also contains the orientation of the Player, ie. 
 * where it is looking at and the player's speed. 
 * <p>
 * For the player to move, a reference to a Control object can be set, which can then
 * be polled directly for the most recent input. 
 * <p>
 * All these variables can be adjusted freely by MazeRunner. They could be accessed
 * by other classes if you pass a reference to them, but this should be done with 
 * caution.
 * 
 * @author Bruno Scheele
 *
 */
public class Player extends GameObject {	
	private double horAngle, verAngle;
	private double speed;
	public static double speedadjust;
	private Maze maze; 										// The maze.
	private double newX, newZ;

	private Control control = null;
	
	/**
	 * The Player constructor.
	 * <p>
	 * This is the constructor that should be used when creating a Player. It sets
	 * the starting location and orientation.
	 * <p>
	 * Note that the starting location should be somewhere within the maze of 
	 * MazeRunner, though this is not enforced by any means.
	 * 
	 * @param x		the x-coordinate of the location
	 * @param y		the y-coordinate of the location
	 * @param z		the z-coordinate of the location
	 * @param h		the horizontal angle of the orientation in degrees
	 * @param v		the vertical angle of the orientation in degrees
	 */
	public Player( double x, double y, double z, double h, double v ) {
		// Set the initial position and viewing direction of the player.
		super( x, y, z );
		horAngle = h;
		verAngle = v;
		speed = 0.01;
		speedadjust = 1;
	}
	
	/**
	 * Sets the Control object that will control the player's motion
	 * <p>
	 * The control must be set if the object should be moved.
	 * @param input
	 */
	public void setControl(Control control)
	{
		this.control = control;
	}
	
	/**
	 * Gets the Control object currently controlling the player
	 * @return
	 */
	public Control getControl()
	{
		return control;
	}

	/**
	 * Returns the horizontal angle of the orientation.
	 * @return the horAngle
	 */
	public double getHorAngle() {
		return horAngle;
	}

	/**
	 * Sets the horizontal angle of the orientation.
	 * @param horAngle the horAngle to set
	 */
	public void setHorAngle(double horAngle) {
		this.horAngle = horAngle;
	}

	/**
	 * Returns the vertical angle of the orientation.
	 * @return the verAngle
	 */
	public double getVerAngle() {
		return verAngle;
	}

	/**
	 * Sets the vertical angle of the orientation.
	 * @param verAngle the verAngle to set
	 */
	public void setVerAngle(double verAngle) {
		if(-45 <= verAngle && verAngle <= 45)
			this.verAngle = verAngle;
	}
	
	/**
	 * Returns the speed.
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed.
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Check whether the player is near a wall
	 * @param x
	 * @param z
	 * @param dT
	 * @return
	 */
	public boolean checkWall(double x, double z, double dT){
		double d = 1 * speed * dT; 		//distance from the wall
		boolean res = false;
		
		for(int i = 0; i < 360; i = i + 45)
			if(maze.isWall( x+d*Math.sin(i*Math.PI/180) , z+d*Math.cos(i*Math.PI/180) , locationY ))
				res = true;
		
		return res;
	}

	/**
	 * Updates the physical location and orientation of the player
	 * @param deltaTime The time in milliseconds since the last update.
	 */
	public void update(int deltaTime)
	{
		if (control != null)
		{
			control.update();
			
			// TODO: Rotate the player, according to control
			setHorAngle( getHorAngle() + control.getdX() * 30*speed);
			setVerAngle( getVerAngle() + control.getdY() * 30*speed);
			
//			// TODO: Move the player, according to control
//			if(control.getForward())
//			{
//				setLocationX(getLocationX()-Math.sin(Math.PI*getHorAngle()/180)*getSpeed()*deltaTime);
//				setLocationZ(getLocationZ()-Math.cos(Math.PI*getHorAngle()/180)*getSpeed()*deltaTime);
//			}
//			if(control.getBack())
//			{
//				setLocationX(getLocationX()+Math.sin(Math.PI*getHorAngle()/180)*getSpeed()*deltaTime);
//				setLocationZ(getLocationZ()+Math.cos(Math.PI*getHorAngle()/180)*getSpeed()*deltaTime);
//
//			}
//			if(control.getRight())
//			{
//				setLocationZ(getLocationZ()-Math.sin(Math.PI*getHorAngle()/180)*getSpeed()*deltaTime);
//				setLocationX(getLocationX()+Math.cos(Math.PI*getHorAngle()/180)*getSpeed()*deltaTime);
//			}
//			if(control.getLeft())
//			{
//				setLocationZ(getLocationZ()+Math.sin(Math.PI*getHorAngle()/180)*getSpeed()*deltaTime);
//				setLocationX(getLocationX()-Math.cos(Math.PI*getHorAngle()/180)*getSpeed()*deltaTime);
//			}

//			// TODO: Adjust player viewing angle
//			setHorAngle( horAngle + control.getdX() * speed/3.0);
//			setVerAngle( verAngle + control.getdY() * speed/3.0);
			
			// TODO: Move the player, according to control
			double speed = this.speed * speedadjust;
			
			
			double dY = maze.isRamp(locationX, locationZ, locationY);
			
			locationY += dY;
			
			if(control.getForward()){
				newX = locationX - speed * deltaTime * Math.sin(horAngle*Math.PI/180);
				newZ = locationZ - speed * deltaTime * Math.cos(horAngle*Math.PI/180);
				// Move only to new co�rdinates if there is no wall there
				if(!checkWall(newX, newZ, deltaTime)){
					locationX = newX;
					locationZ = newZ;
				}else if(!checkWall(newX, locationZ, deltaTime)){
					locationX = newX;
				}else if(!checkWall(locationX, newZ, deltaTime)){
					locationZ = newZ;
				}
			}
			if(control.getLeft()){
				newX = locationX - speed * deltaTime * Math.cos(horAngle*Math.PI/180);
				newZ = locationZ + speed * deltaTime * Math.sin(horAngle*Math.PI/180);
				// Move only to new co�rdinates if there is no wall there
				if(!checkWall(newX, newZ, deltaTime)){
					locationX = newX;
					locationZ = newZ;
				}else if(!checkWall(newX, locationZ, deltaTime)){
					locationX = newX;
				}else if(!checkWall(locationX, newZ, deltaTime)){
					locationZ = newZ;
				}
			}
			if(control.getBack()){
				newX = locationX + speed * deltaTime * Math.sin(horAngle*Math.PI/180);
				newZ = locationZ + speed * deltaTime * Math.cos(horAngle*Math.PI/180);
				// Move only to new co�rdinates if there is no wall there
				if(!checkWall(newX, newZ, deltaTime)){
					locationX = newX;
					locationZ = newZ;
				}else if(!checkWall(newX, locationZ, deltaTime)){
					locationX = newX;
				}else if(!checkWall(locationX, newZ, deltaTime)){
					locationZ = newZ;
				}
			}
			if(control.getRight()){
				newX = locationX + speed * deltaTime * Math.cos(horAngle*Math.PI/180);
				newZ = locationZ - speed * deltaTime * Math.sin(horAngle*Math.PI/180);
				// Move only to new co�rdinates if there is no wall there
				if(!checkWall(newX, newZ, deltaTime)){
					locationX = newX;
					locationZ = newZ;
				}else if(!checkWall(newX, locationZ, deltaTime)){
					locationX = newX;
				}else if(!checkWall(locationX, newZ, deltaTime)){
					locationZ = newZ;
				}
			}
		}
	}
	
	/**
	 * Get the maze from MazeRunner
	 * @param maze
	 */
	public void getMaze(Maze maze){
		this.maze=maze;
	}
}
