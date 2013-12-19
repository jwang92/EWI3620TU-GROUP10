package GameObject;
import java.util.ArrayList;

import Main.MainClass;
import Maze.Maze;
import UserInput.Control;
import Utils.Sounds;

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
	protected double playersize = 1.0;
	private double verticalSpeed;
	private double gravity = 0.00004;
	public static double speedadjust;
	private Maze maze; 										// The maze.
	private double newX, newZ;
	private int health;
	public int score=0;
	private int sensitivity = 10;
	
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
		speed = 0.02;
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
		if(-70 <= verAngle && verAngle <= 70)
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
		if(this.health + health > 100)
			this.health = 100;
		else
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
	
	public boolean checkEnemy(double x, double z, double dT){
		double dX, dZ, distance;
		
		for(Enemy foe : MainClass.enemies){
			if(foe.alert){
				dX = Math.abs(x - foe.locationX);
				dZ = Math.abs(z - foe.locationZ);
				distance = Math.sqrt(dZ*dZ + dX*dX);
				if(distance <= foe.enemysize + playersize)
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Check whether the player is near a wall
	 * @param x
	 * @param z
	 * @param dT
	 * @return
	 */
	public boolean checkWallOrDoor(double x, double z, double dT){
		double d = 1 * speed * dT; 		//distance from the wall
		boolean res = false;
		
		for(int i = 0; i < 360; i = i + 45)
			if(maze.isWall( x+d*Math.sin(i*Math.PI/180) ,locationY,  z+d*Math.cos(i*Math.PI/180) )
					|| maze.isDoor( x+d*Math.sin(i*Math.PI/180) ,locationY,  z+d*Math.cos(i*Math.PI/180) ) )
				res = true;
		
		return res;
	}
	
	public boolean collision(double x, double z, double dT){
			return checkWallOrDoor(x, z, dT) || checkEnemy(x, z, dT);
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
			
			setHorAngle( getHorAngle() + control.getdX() * sensitivity * speed);
			setVerAngle( getVerAngle() + control.getdY() * sensitivity * speed);
			
			double speed = this.speed * speedadjust;
			double newLocationY = locationY;
			boolean throughFloor = false;
			boolean throughRoof = false;
			boolean throughRampTest1 = false;
			boolean throughRampTest2 = false;
			
			double dY = maze.isRamp(locationX, locationY, locationZ);
			if(dY != Double.MAX_VALUE){
				
				if(dY == Double.MIN_VALUE){
					throughRampTest1 = true;
				}
				else{
					verticalSpeed = 0;
					locationY += dY;
				}
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
				newLocationY += verticalSpeed*deltaTime;
				if(verticalSpeed<0){
					throughFloor = maze.throughFloor(locationX, newLocationY, locationY, locationZ);
				}
				else if(verticalSpeed>0){
					throughRoof = maze.throughRoof(locationX, newLocationY, locationY, locationZ);
				}
			}
			
			if(throughFloor){
				locationY = maze.getFloorHeight(locationY)+2.5;
			}
			else if(throughRoof){
				verticalSpeed = 0;
			}
			else{
				locationY += verticalSpeed*deltaTime;
			}

			
			double oldX = locationX;
			double oldZ = locationZ;

			if(control.getForward()){
				newX = locationX - speed * deltaTime * Math.sin(horAngle*Math.PI/180);
				newZ = locationZ - speed * deltaTime * Math.cos(horAngle*Math.PI/180);
				// Move only to new coördinates if there is no wall or ramp there
				if(throughRampTest1){
					double d = maze.isRamp(newX, locationY, newZ);
					if(d == Double.MIN_VALUE){
						throughRampTest2 = true;
					}					
				}
				if(!collision(newX, newZ, deltaTime) && !throughRampTest2){
					locationX = newX;
					locationZ = newZ;
				}else if(!collision(newX, locationZ, deltaTime)&& !throughRampTest2){
					locationX = newX;
				}else if(!collision(locationX, newZ, deltaTime)&& !throughRampTest2){
					locationZ = newZ;
				}
			}
			if(control.getLeft()){
				newX = locationX - speed * deltaTime * Math.cos(horAngle*Math.PI/180);
				newZ = locationZ + speed * deltaTime * Math.sin(horAngle*Math.PI/180);
				// Move only to new coördinates if there is no wall or ramp there
				if(throughRampTest1){
					double d = maze.isRamp(newX, locationY, newZ);
					if(d == Double.MIN_VALUE){
						throughRampTest2 = true;
					}					
				}
				if(!collision(newX, newZ, deltaTime) && !throughRampTest2){
					locationX = newX;
					locationZ = newZ;
				}else if(!collision(newX, locationZ, deltaTime) && !throughRampTest2){
					locationX = newX;
				}else if(!collision(locationX, newZ, deltaTime) && !throughRampTest2){
					locationZ = newZ;
				}
			}
			if(control.getBack()){
				newX = locationX + speed * deltaTime * Math.sin(horAngle*Math.PI/180);
				newZ = locationZ + speed * deltaTime * Math.cos(horAngle*Math.PI/180);
				// Move only to new coördinates if there is no wall or ramp there
				if(throughRampTest1){
					double d = maze.isRamp(newX, locationY, newZ);
					if(d == Double.MIN_VALUE){
						throughRampTest2 = true;
					}					
				}
				if(!collision(newX, newZ, deltaTime) && !throughRampTest2){
					locationX = newX;
					locationZ = newZ;
				}else if(!collision(newX, locationZ, deltaTime) && !throughRampTest2){
					locationX = newX;
				}else if(!collision(locationX, newZ, deltaTime) && !throughRampTest2){
					locationZ = newZ;
				}
			}
			if(control.getRight()){
				newX = locationX + speed * deltaTime * Math.cos(horAngle*Math.PI/180);
				newZ = locationZ - speed * deltaTime * Math.sin(horAngle*Math.PI/180);
				// Move only to new coördinates if there is no wall or ramp there
				if(throughRampTest1){
					double d = maze.isRamp(newX, locationY, newZ);
					if(d == Double.MIN_VALUE){
						throughRampTest2 = true;
					}					
				}
				if(!collision(newX, newZ, deltaTime) && !throughRampTest2){
					locationX = newX;
					locationZ = newZ;
				}else if(!collision(newX, locationZ, deltaTime) && !throughRampTest2){
					locationX = newX;
				}else if(!collision(locationX, newZ, deltaTime) && !throughRampTest2){
					locationZ = newZ;
				}
			}
			
			
			if((Math.abs(locationX - oldX) > 0.01 || Math.abs(locationZ - oldZ) > 0.01) && sound.getWalk() == false){
				sound.walk();
			}
			else if((Math.abs(locationX - oldX) < 0.01 && Math.abs(locationZ - oldZ) < 0.01) && sound.getWalk() == true){
				sound.stopWalk();
			}
			if(control.getAttack() && MainClass.sword.attackCounter == 9){
				sound.swing();
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
		
		switch(check){
		
			case 1: // Speedupgrade
							
				int[] temp = new int[2];
				this.speed = 0.03;
				temp[0] = 1; // type 1
				temp[1] = 5000; //tijd voor upgrade 5 seconden
				for(int i = 0; i < currentUpgrades.size(); i++){
					if(currentUpgrades.get(i)[0] == 1){
						currentUpgrades.remove(i);
					}
				}
				currentUpgrades.add(temp);
				break;
			case 2: // Swordupgrade
				MainClass.mazeRunner.setRW(false);

				int[] temp2 = new int[3];	
				temp2[0] = 2; // type 2
				temp2[1] = -1; //tijd voor upgrade 2 seconden
				temp2[2] = upgradeSword(x, y, z);
				
				for(int i = 0; i < currentUpgrades.size(); i++){
					if(currentUpgrades.get(i)[0] == 2){
						currentUpgrades.remove(i);
					}
				}
				
				currentUpgrades.add(temp2);
				
				break;
			case 3: // Health
				setDeltaHealth(30);
				

				break;
			case 4: // Ranged weapong
				
				int[] temp3 = new int[3];	
				temp3[0] = 4; // type 3
				temp3[2] = 1;
				currentUpgrades.add(temp3);

				sound.dropSword();
				
				MainClass.rWeapon.switchGun(0);
				MainClass.mazeRunner.setRW(true);
				MainClass.input.rUpgrade = true;
			
				break;
			default:
				// niets doen
				break;
			
		}
		
	}
	
	public int upgradeSword(double x, double y, double z){
		
		int maxSword = 3;
		int swordToSet = 2;
				
		for(int i = 0; i < currentUpgrades.size(); i++){
			if(currentUpgrades.get(i)[0] == 2){
				if(currentUpgrades.get(i)[2] < maxSword){
					swordToSet = currentUpgrades.get(i)[2] + 1;
				}else{
					swordToSet = maxSword;
				}
			}
		}
				
		sound.dropSword();
		
		MainClass.sword.switchSword(swordToSet - 1);
		
		
		return swordToSet;
		
	}
	
	/**
	 * Get the maze from MazeRunner
	 * @param maze
	 */
	public void getMaze(Maze maze){
		this.maze=maze;
	}
	
	public void setScore(int s){
		score += s;
	}
	
	public int getScore(){
		return score;
	}
}
