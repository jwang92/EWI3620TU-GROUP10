package GameObject;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.media.opengl.GL;
import Main.GameState;
import Main.MainClass;
import Maze.Maze;
import Maze.Pheromone;
import Model.Model;
import Model.ModelPart;
import Model.OBJLoader;
import Utils.Sounds;
import Utils.Point3D;

import com.sun.opengl.util.texture.Texture;


public class Enemy extends GameObject implements VisibleObject {
	private Maze maze; 										// The maze.
	public LeftArm leftArm;
	public RightArm rightArm;
	public RightLeg rightLeg;
	public LeftLeg leftLeg;
	public RightWing rightWing;
	public LeftWing leftWing;
	private double newX, newZ;
	private double speed = 0.0025;
	protected double enemysize = 1.0;
	
	//Model
	private String type;
	private Model m ;
	
	private double px,pz, ymax, ymin, dmgDet, healthBarPos;
	protected boolean alert;
	private boolean texture;
	private IntBuffer vboHandle = IntBuffer.allocate(10);
	private int attackTimeout = 0;
	public double angle, healthAngle;
	
	private Pheromone highestPher;
	//Shaders
	private int shaderProgram = 0;
	
	//Attack
	private int attackPower = 10;
	
	//Health
	private int health = 100;
	
	//Death
	private int deathAngle = 0;
	public boolean dood = false;
	private boolean remove = false;
	
	//Sounds
	public Sounds sound = new Sounds();
	
	//Route
	private ArrayList<Point3D> route = new ArrayList<Point3D>();
	public boolean findRoute = false;
	
	public Enemy(double x, double y, double z, double angle,boolean tex, String modelName, MainClass mclass){
		super(x, y, z, mclass);
		this.angle = angle;
		alert = false;
		texture = tex;
		try {
			if(texture){
				if(!main.enemieModelNames.contains(modelName)){
					main.enemieModelNames.add(modelName);
					Model tempmodel = OBJLoader.loadTexturedModel((new File(modelName)));
					main.enemieModels.add(tempmodel);
				}
				if(main.enemieModelNames.contains(modelName)){
					int modelID = main.enemieModelNames.lastIndexOf(modelName);
					type = modelName;
					m = main.enemieModels.get(modelID);
				}
			}
			else{
				m = OBJLoader.loadModel((new File("3d_object/Predator/Predator_Youngblood2/Body.obj")));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//afhankekelijk van soort enemy: laadt juiste model en onderdelen en hitbix waardes
		
		if(type.equals("3d_object/Predator/Predator_Youngblood/Body.obj")){
			leftArm= new LeftArm(x,y,z,tex,"LeftArm", main, true);
			leftArm.setEnemy(this);
			rightArm= new RightArm(x,y,z,tex, "RightArm", main, true);
			rightArm.setEnemy(this);
			rightLeg = new RightLeg(x,y,z,tex,"RightLeg", main, true);
			rightLeg.setEnemy(this);
			leftLeg = new LeftLeg(x,y,z,tex,"LeftLeg", main, true);
			leftLeg.setEnemy(this);
			
			ymin=0;
			ymax = 4.5;
			dmgDet=1.25;
			healthBarPos = 4.5;
		}
		else if(type.equals("3d_object/Predator/Predator_Youngblood2/Body.obj")){
			leftArm= new LeftArm(x,y,z,tex,"LeftArm", main, false);
			leftArm.setEnemy(this);
			rightArm= new RightArm(x,y,z,tex, "RightArm", main, false);
			rightArm.setEnemy(this);
			rightLeg = new RightLeg(x,y,z,tex,"RightLeg", main, false);
			rightLeg.setEnemy(this);
			leftLeg = new LeftLeg(x,y,z,tex,"LeftLeg", main, false);
			leftLeg.setEnemy(this);
			
			ymin=0;
			ymax = 3.4;
			dmgDet=1.0;
			healthBarPos = 3.6;
		}
		else if(type.equals("3d_object/Bathos/bathos.obj")){
			leftWing = new LeftWing(x,y,z,tex,"LeftWing", main);
			leftWing.setEnemy(this);
			rightWing = new RightWing(x,y,z,tex,"RighttWing", main);
			rightWing.setEnemy(this);
			
			ymin=1.5;
			ymax=3.4;
			dmgDet=1.0;
			healthBarPos = 3.6;
		}
		else{
			ymin=0;
			ymax=2.6;
			dmgDet=1.0;
			healthBarPos = 2.8;
		}
	}
	
	public String getType(){
		return type;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setShaderProgram(int program){
		shaderProgram = program;
	}

	public boolean equals(Object that){
		if(that instanceof Enemy){
			Enemy other = (Enemy) that;
			
			return (this.locationX == other.locationX 
					&& this.locationY == other.locationY 
					&& this.locationZ == other.locationZ);
		}
		
		return false;
	}

	/**
	 * collision with other enemy
	 * @param x  x co�rdinaat
	 * @param z  x co�rdinaat
	 * @param dT deltatijd
	 * @return
	 */
	public boolean checkEnemy(double x, double z, double dT){
		double dX, dZ, dY, distance;
		
		for(Enemy foe : main.enemies){
			if(!this.equals(foe) && foe.alert){
				dX = x - foe.locationX;
				dZ = z - foe.locationZ;
				dY = locationY - foe.locationY;
				distance = Math.sqrt(dZ*dZ + dX*dX);
				if( distance <= foe.enemysize + enemysize 
						&& Math.abs(dY) <= maze.SQUARE_SIZE / 2.0f )
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 *  check collision with player
	 * @param x  x co�rdinaat
	 * @param z  x co�rdinaat
	 * @param dT deltatijd
	 * @return
	 */
	public boolean checkPlayer(double x, double z, double dT){
		double dX = x - main.player.locationX;
		double dZ = z - main.player.locationZ;
		double dY = locationY - main.player.locationY;
		double distance = Math.sqrt(dZ*dZ + dX*dX);
		
		if( distance <= main.player.playersize + enemysize 
				&& (maze.SQUARE_SIZE*-0.9 <= dY && dY <= maze.SQUARE_SIZE*0.1 ) )
			return true;
		
		return false;
	}
	
	/**
	 * check collision with wall or door
	 * @param x  x co�rdinaat
	 * @param z  x co�rdinaat
	 * @param dT deltatijd
	 * @return
	 */
	public boolean checkWallOrDoor(double x, double z, double dT){
		boolean res = false;
		
		for(int i = 0; i < 360; i = i + 15)
			if(maze.isWall( x+enemysize*Math.sin(i*Math.PI/180) , locationY , z-0.8f+enemysize*Math.cos(i*Math.PI/180) )
					|| maze.isDoor( x+enemysize*Math.sin(i*Math.PI/180) , locationY , z-0.8f+enemysize*Math.cos(i*Math.PI/180) ))
				res = true;
		
		return res;
	}
	
	/**
	 * check all collisions
	 * @param x  x co�rdinaat
	 * @param z  x co�rdinaat
	 * @param dT deltatijd
	 * @return
	 */
	public boolean collision(double x, double z, double dT){
		return checkWallOrDoor(x, z, dT)
				|| checkEnemy(x, z, dT)
				|| checkPlayer(x, z, dT);
	}
	
//	public void genDisplayList(GL gl){
//        displayList = OBJLoader.createDisplayList(m, gl);
//	}
	
	public void genVBO(GL gl){
		vboHandle = OBJLoader.createVBO(m, gl);
	}
	
	public IntBuffer getVBOHandle(){
		return vboHandle;
	}
	
	public void setVBOHandle(IntBuffer vbo){
		vboHandle = vbo;
	}
	
	/**
	 * Check if the enemy needs to be removed
	 * @return True if the enemy needs to be removed
	 */
	public boolean needRemoval(){
		return remove;
	}
	
	/**
	 * Every Frame update the location of the player
	 * @param deltaTime Time since the last Frame
	 * @param player main player
	 */
	public void update(int deltaTime, Player player){
		px = player.locationX;
		pz = player.locationZ;

		//update location 
		if(!dood){
			//Check if the player alerts the enemy (player within range)
			if(alerted(player)){
				alert = alerted(player);
			}
			//If the enemy is alerted follow maze pheromones.
			if(alert){
				highestPher = main.mazePheromones.Search(locationX, locationY, locationZ, 15);
				
				
				double dX = highestPher.x - locationX;				
				double dZ = highestPher.z - locationZ;				
				double distance = Math.sqrt(dZ*dZ + dX*dX);
				
				newLocation(dX, dZ, distance, deltaTime);

			}
			
			//If there is a route to follow and the enemy doesn't follow pheromones, follow the route
			else{
				if(findRoute && route.size()<=0){
					findRoute();
					findRoute = false;
				}
				if(route.size()>0){
					//Delete the point on the route if the enemy is already there, else walk to the next point.
					double routeX = route.get(route.size()-1).getX() + 1;
					double routeZ = route.get(route.size()-1).getZ() + 1;
					boolean pointReached = false;
					if((locationX/main.maze.SQUARE_SIZE)>= (routeX-0.2) && (locationX/main.maze.SQUARE_SIZE) <= (routeX + 0.2)){
						if((locationZ/main.maze.SQUARE_SIZE)>= (routeZ-0.2) && (locationZ/main.maze.SQUARE_SIZE) <= (routeZ + 0.2)){
							route.remove(route.size()-1);
							pointReached = true;
						}						
					}
					if(!pointReached){
						double dX = (route.get(route.size()-1).getX() + 1)*main.maze.SQUARE_SIZE - locationX;				
						double dZ = (route.get(route.size()-1).getZ() + 1)*main.maze.SQUARE_SIZE - locationZ;				
						double distance = Math.sqrt(dZ*dZ + dX*dX);
						
						newLocation(dX, dZ, distance, deltaTime);
					}
					
				}
			}
			
			this.caught(player);
		}
	}
	
	/**
	 * test if collision, if not calculate new location
	 * @param dx delta X
	 * @param dz delat Z
	 * @param distance distance new and old location
	 * @param deltaTime
	 */
	public void newLocation(double dx, double dz, double distance, int deltaTime){
		newX = locationX;		
		newZ = locationZ;
		
		if(distance > 0.01){
			dx = dx/distance * speed * deltaTime;
			dz = dz/distance * speed * deltaTime;
			
			newX += dx;
			newZ += dz;
			
			if(!collision(newX, newZ, deltaTime)){
				locationX = newX;
				locationZ = newZ;
			}
			
			else{
				newX = locationX;
				if(dx > 0.0){
					newX += speed * deltaTime;
				}
				else if(dx < 0.0){
					newX -= speed * deltaTime;
				}
					
				newZ = locationZ;
				if(dz > 0.0){
					newZ += speed * deltaTime;
				}
				
				else if(dz < 0.0){
					newZ -= speed * deltaTime;
				}
		
				if(!collision(newX, locationZ, deltaTime)){
					locationX = newX;
				}else if(!collision(locationX, newZ, deltaTime)){
					locationZ = newZ;
				}
			}
		}
	}

	public void display(GL gl) {
		gl.glPushMatrix();
		//gl.glEnable(GL.GL_TEXTURE_NORMAL_EXT);
		//Enable the shaderprogram
		if(shaderProgram >0){
			gl.glUseProgram(shaderProgram);
		}
		
		//Draw nothing if the vboHandle are not loaded
		if(vboHandle.get(0)<=0||vboHandle.get(1)<=0){
			
		}
		
		else{
			//Translate the model to the right location
			gl.glTranslated(locationX, locationY, locationZ);
			
			if(alert && !dood){
				//calculate angle: wich direction to look at
					double inP = highestPher.z-locationZ;
					double lengteV = 1;
					double lengteW = Math.sqrt(Math.pow(highestPher.x-locationX, 2)+Math.pow(highestPher.z-locationZ, 2));
					double test = inP/Math.max(lengteV*lengteW, 00001);
					angle = Math.acos(test)*180/Math.PI;
					if(highestPher.x<locationX){
						angle = -angle;
					}
					
				}
			else if(!alert && !dood){
				//calculate angle: wich direction to look at
				if(route.size()>0){
					double dX = (route.get(route.size()-1).getX() + 1)*main.maze.SQUARE_SIZE - locationX;				
					double inP = (route.get(route.size()-1).getZ() + 1)*main.maze.SQUARE_SIZE - locationZ;
					double lengteV = 1;
					double lengteW = Math.sqrt(Math.pow(dX, 2)+Math.pow(inP, 2));
					double test = inP/Math.max(lengteV*lengteW, 00001);
					angle = Math.acos(test)*180/Math.PI;
					if(dX<0){
						angle = -angle;
					}	
				}
			}	
			//angle for dying animation
			else if(dood){
				if(deathAngle>-90){
					deathAngle -= 2.5;
				}
				//remove when dead
				else if(deathAngle<=-90){
					remove = true;
					if(type.equals("3d_object/Predator/Predator_Youngblood/Body.obj")){
						main.player.setScore(300);
					}
					else{
						main.player.setScore(100);
					}
				}
			}
			
			if(type.equals("3d_object/Bathos/bathos.obj")){
				gl.glTranslated(0, 2.5, 0);
				gl.glRotated(20 ,Math.cos(angle*Math.PI/180), 0, -Math.sin(angle*Math.PI/180));
				gl.glTranslated(0, -2.5, 0);
			}
			
			gl.glRotated(angle,0, 1, 0);
			gl.glRotated(deathAngle, 1, 0, 0);
			//Reset the color to white
			gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
			gl.glColor3f(1.0f,1.0f,1.0f);
			//Initialize counters
			int vertexSize = 0;
			int vertexCount = 0;
			
			//Initialize the texture
			Texture tempTexture = null;
			for(int i=0;i<m.getModelParts().size();i++){
				gl.glColor3f(1.0f,1.0f,1.0f);
				ModelPart p = m.getModelParts().get(i);
				ModelPart.Face face = p.getFaces().get(0);
				
				//Enable the Array draw Mode for vertex,normals and textures
				gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
				gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
				
				//the modelPart has textureCoordinates so the material should be used
				if(face.hasMaterial() && !face.hasTexture()){
	                gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, new float[] {1f,face.getMaterial()
                            .diffuseColour[0], face.getMaterial().diffuseColour[1],
                            face.getMaterial().diffuseColour[2]},1);
	                gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, new float[] {1f,face.getMaterial()
                            .ambientColour[0], face.getMaterial().ambientColour[1],
                            face.getMaterial().ambientColour[2]}, 1);
	                gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, face.getMaterial().specularCoefficient);
				}
				
				//Use default material
				else if (!face.hasTexture()){
	                gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, new float[] {1.0f,1.0f, 1.0f, 1.0f}, 1);
					gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, new float[] {1.0f,1.0f, 1.0f, 1.0f}, 1);
	                gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 120f);
				}
				
				//Bind the vbo buffers for the normal and vertex arrays
                vertexSize = p.getFaces().size()*3;
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboHandle.get(2));
				gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0L);
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboHandle.get(0));
				gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0L);
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboHandle.get(1));
				gl.glNormalPointer(GL.GL_FLOAT, 0, 0L);
				
				//Enable the texture if the modelPart has a texture
				if(face.hasTexture()){
					gl.glActiveTexture(GL.GL_TEXTURE0);
					gl.glEnable(GL.GL_TEXTURE_2D);
					tempTexture = face.getTexture();
					tempTexture.bind();
				}
				
				//Draw the arrays
				gl.glDrawArrays(GL.GL_TRIANGLES, vertexCount, vertexSize);
				
				//Enable the Array draw Mode for vertex,normals and textures
				gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
				gl.glDisableClientState(GL.GL_NORMAL_ARRAY);
				gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY);
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
				
				//Keep track of the size of the modelParts. This 
				vertexCount += vertexSize;
				
				//Disable the texture i
				if(face.hasTexture()){
					tempTexture.disable();
					gl.glDisable(GL.GL_TEXTURE_2D);
				}
			}
		}
		
		//Disable shaderprograms
		gl.glUseProgram(0);	
		
		gl.glPopMatrix();
		//healthbar enemy
		if(!dood){
			gl.glPushMatrix();
			
				gl.glTranslated(locationX,locationY+healthBarPos,locationZ);
			
			//berekening angle: to direct player
				double inP = px-locationX;
				double lengteV = 1;
				double lengteW = Math.sqrt(Math.pow(px-locationX, 2)+Math.pow(pz-locationZ, 2));
				double test = inP/Math.max(lengteV*lengteW, 00001);
				healthAngle = Math.acos(test)*180/Math.PI;
				if(pz>locationZ){
					healthAngle = -healthAngle;
				}
			gl.glRotated(healthAngle,0, 1, 0);
			
			gl.glEnable(GL.GL_COLOR_MATERIAL);
			
			gl.glColor3f(0.2f, 0.2f, 0.2f);
			
			gl.glBegin(GL.GL_QUADS);
				gl.glVertex3d(-0.001, -0.05, 1.05);
				gl.glVertex3d(-0.001, -0.05, -1.05);
				gl.glVertex3d(-0.001, 0.55, -1.05);
				gl.glVertex3d(-0.001, 0.55, 1.05);
			gl.glEnd();
			
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glBegin(GL.GL_QUADS);
				gl.glVertex3d(0.0, 0.0, 1.0);
				gl.glVertex3d(0.0, 0.0, 1.0-(2.0*health/100.0));
				gl.glVertex3d(0.0, 0.5, 1.0-(2.0*health/100.0));
				gl.glVertex3d(0.0, 0.5, 1.0);
			gl.glEnd();
			gl.glColor3f(1.0f, 1.0f, 1.0f);
			gl.glDisable(GL.GL_COLOR_MATERIAL);
			gl.glPopMatrix();
			
		}
	}
	
	public void setMaze(Maze maze){
		this.maze = maze;
	}

	/**
	 * if player is in catching range, do damage
	 * @param player
	 */
	public void caught(Player player){
		double dX = player.locationX - locationX;				
		double dZ = player.locationZ - locationZ;				
		double distance = Math.sqrt(dZ*dZ + dX*dX);
		
		//do damage
		if( distance < (player.playersize + enemysize + 0.1) && Math.abs(locationY - player.locationY) < 0.6*maze.SQUARE_SIZE ){
			if(attackTimeout == 0){
				player.setDeltaHealth(Math.min(0, -attackPower+player.getDefensePower()));
				attackTimeout = 15;
			} else{
				attackTimeout--;
			}
			//if player dies, go to gameover state
			if(player.getHealth() <= 0){
				main.state.GameStateUpdate(GameState.GAMEOVER_STATE);
				main.state.setStopMainGame(true);
				main.state.setStopGameOver(false);
			}
		}
	}
	
	/**
	 * if player in area, then enemy gets alerted
	 * @param player
	 * @return
	 */
	public boolean alerted (Player player){
		 boolean res = (Math.sqrt(Math.pow(locationX-player.locationX,2 )+Math.pow(locationZ-player.locationZ,2)) < 15) ;
		 if(res == true){
			 //If there are other enemies in the enemies range, tell them the location of the player.
			 ArrayList<Enemy> enemies = main.enemies;
			 for(Enemy e: enemies){
				 if(!this.equals(e) && !findRoute){
					 double dx = locationX - e.getLocationX();
					 double dy = locationY - e.getLocationY();
					 double dz = locationZ - e.getLocationZ();
					 double dis = Math.sqrt(dx*dx + dy*dy + dz*dz);
					 if(dis<15){
						 e.findRoute = true;
					 }
				 }
			 }
		 }
		 return res;
	}
	
	/**
	 * Find a route between the player and the enemy
	 */
	public void findRoute(){
		int storeyEnemy = main.maze.getStorey(locationY);
		int storeyPlayer = main.maze.getStorey(main.player.getLocationY());
		if(storeyEnemy == storeyPlayer){
			route = main.maze.getNavMesh().get(storeyEnemy).findRoute(
					(float) (locationX/main.maze.SQUARE_SIZE) -1, 
					(float) (locationZ/main.maze.SQUARE_SIZE)-1, 
					(float) (main.player.getLocationX()/main.maze.SQUARE_SIZE)-1, 
					(float) (main.player.getLocationZ()/main.maze.SQUARE_SIZE)-1);
		}
		if(route == null){
			route = new ArrayList<Point3D>();
		}
	}
	
	/**
	 * checks if hitpoint is in hitbox
	 * @param x  x coordinaat
	 * @param y  y coordinaat
	 * @param z  z coordinaat
	 * @param h  horizontal angle of player
	 * @param d  damage to be done to enemy
	 * @return
	 */
	public boolean damage(double x, double y, double z, double h, double d){
		double c=Math.sqrt((locationX-x)*(locationX-x)+(locationZ-z)*(locationZ-z));
		if(c<dmgDet && y>ymin + locationY && y <ymax +locationY){
			health -=d;
			if(health<=0){
				dood = true;
			}
			return true;
		}
		return false;
	}
	
}
