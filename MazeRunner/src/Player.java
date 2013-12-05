import java.util.ArrayList;

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
	private double verticalSpeed;
	private double gravity = 0.00005;
	public static double speedadjust;
	private Maze maze; 										// The maze.
	private double newX, newZ;
	private int health;
	
	//Defense
	private int defensePower = 0;
	
	private ArrayList<int[]> currentUpgrades;
	
	//Sounds
	public Sounds sound = new Sounds();

	Control control = null;
	
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
		health = 100;
		currentUpgrades = new ArrayList<int[]>();
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

	public void setDeltaHealth(int health){
		this.health += health;
	}
	
	public int getHealth(){
		return health;		
	}
	
	public int getDefensePower(){
		return defensePower;
	}
	
	public void setDefensePower(int dp){
		defensePower = dp;
	}
	
	public ArrayList<int[]> getUpgrades(){
		return currentUpgrades;
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
			if(maze.isWall( x+d*Math.sin(i*Math.PI/180) ,locationY,  z+d*Math.cos(i*Math.PI/180) ))
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
			
			
			double dY = maze.isRamp(locationX, locationY, locationZ);
			if(dY != 0){
				verticalSpeed = 0;
				locationY += dY;
			}
			else if(maze.isFloor(locationX, locationY, locationZ)){
				locationY = maze.getFloorHeight(locationY)+2.5;
				if(control.getJump()){
					verticalSpeed = 0.025;
				}
				else{
					verticalSpeed = 0;	
				}
			}
			else{
				verticalSpeed -= gravity*deltaTime;
			}
			locationY += verticalSpeed*deltaTime;
			
			double oldX = locationX;
			double oldZ = locationZ;

			if(control.getForward()){
				newX = locationX - speed * deltaTime * Math.sin(horAngle*Math.PI/180);
				newZ = locationZ - speed * deltaTime * Math.cos(horAngle*Math.PI/180);
				// Move only to new coördinates if there is no wall there
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
				// Move only to new coördinates if there is no wall there
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
				// Move only to new coördinates if there is no wall there
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
				// Move only to new coördinates if there is no wall there
				if(!checkWall(newX, newZ, deltaTime)){
					locationX = newX;
					locationZ = newZ;
				}else if(!checkWall(newX, locationZ, deltaTime)){
					locationX = newX;
				}else if(!checkWall(locationX, newZ, deltaTime)){
					locationZ = newZ;
				}
			}
			if((Math.abs(locationX - oldX) > 0.01 || Math.abs(locationZ - oldZ) > 0.01) && sound.walking == false){
				sound.walk();
			}
			else if((Math.abs(locationX - oldX) < 0.01 && Math.abs(locationZ - oldZ) < 0.01) && sound.walking == true){
				sound.stopWalk();
			}
			
			// Pickupcheck
			checkPickup(locationX, locationY, locationZ);
			
			// Running upgrades
			checkUpgrades(deltaTime);
			
			//The player leaves a trail of pheromones which the enemies will follow
			MainClass.mazePheromones.addPher(locationX, locationY, locationZ);
		}
	}
	
	public void checkUpgrades(int dt){
		
		for(int i = 0; i < currentUpgrades.size(); i++){
			
			int check = currentUpgrades.get(i)[0];
			
			switch(check){
			
				case 1: // Speedupgrade
					
					int tempdt = currentUpgrades.get(i)[1] - dt;
					if(tempdt <= 0){
						tempdt = 0;
					}

					int[] temp = {1, tempdt};
					currentUpgrades.set(i, temp);
					if(currentUpgrades.get(i)[1] <= 0){
						
						this.speed = 0.01;
						currentUpgrades.remove(i);
						
					}
					break;
				case 2: // Swordupgrade?
					// todo
					break;
				default:
					// niets doen
					break;
				
			}
				
		}
		
	}
	
	public void checkPickup(double x, double y, double z){
		
		int check = maze.isPickup(x, z, y);
		
		int[] temp = new int[2];
		
		switch(check){
		
			case 1: // Speedupgrade
				this.speed = 0.03;
				temp[0] = 1; // type 1
				temp[1] = 2000; //tijd voor upgrade 2 seconden
				currentUpgrades.add(temp);
				break;
			case 2: // Swordupgrade
				
				MainClass.sword = new Sword(x, y, z, true, 2);
				MainClass.sword.setMaze(MainClass.maze);
				MainClass.sword.setPlayer(MainClass.player);
				MainClass.mazeRunner.setSwordloader(false);
				
				temp[0] = 2; // type 2
				temp[1] = -1; //tijd voor upgrade 2 seconden
				currentUpgrades.add(temp);
				break;
			default:
				// niets doen
				break;
			
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
