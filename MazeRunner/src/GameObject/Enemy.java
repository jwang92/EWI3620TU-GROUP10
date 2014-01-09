package GameObject;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import Main.GameState;
import Main.MainClass;
import Maze.Maze;
import Maze.Pheromone;
import Model.Model;
import Model.ModelPart;
import Model.OBJLoader;

import com.sun.opengl.util.GLUT;
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
	
	//
	private int displayList;
	private double sx, sy,sz, px, py,pz;
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
	
	public Enemy(double x, double y, double z, double angle,boolean tex, String modelName){
		super(x, y, z);
		sx=x;
		sy=y;
		sz=z;
		this.angle = angle;
		alert = false;
		texture = tex;
		try {
			if(texture){
				if(!MainClass.enemieModelNames.contains(modelName)){
					MainClass.enemieModelNames.add(modelName);
					Model tempmodel = OBJLoader.loadTexturedModel((new File(modelName)));
					MainClass.enemieModels.add(tempmodel);
				}
				if(MainClass.enemieModelNames.contains(modelName)){
					int modelID = MainClass.enemieModelNames.lastIndexOf(modelName);
					type = modelName;
					m = MainClass.enemieModels.get(modelID);
				}
			}
			else{
				m = OBJLoader.loadModel((new File("3d_object/Predator/Predator_Youngblood/Body.obj")));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(type.equals("3d_object/Predator/Predator_Youngblood/Body.obj")){
			leftArm= new LeftArm(x,y,z,tex,"LeftArm");
			leftArm.setEnemy(this);
			rightArm= new RightArm(x,y,z,tex, "RightArm");
			rightArm.setEnemy(this);
			rightLeg = new RightLeg(x,y,z,tex,"RightLeg");
			rightLeg.setEnemy(this);
			leftLeg = new LeftLeg(x,y,z,tex,"LeftLeg");
			leftLeg.setEnemy(this);
		}
		else if(type.equals("3d_object/Bathos/bathos.obj")){
			leftWing = new LeftWing(x,y,z,tex,"LeftWing");
			leftWing.setEnemy(this);
			rightWing = new RightWing(x,y,z,tex,"RighttWing");
			rightWing.setEnemy(this);
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

	public boolean checkEnemy(double x, double z, double dT){
		double dX, dZ, distance;
		
		for(Enemy foe : MainClass.enemies){
			if(!this.equals(foe) && foe.alert){
				dX = x - foe.locationX;
				dZ = z - foe.locationZ;
				distance = Math.sqrt(dZ*dZ + dX*dX);
				if(distance <= foe.enemysize + enemysize)
					return true;
			}
		}
		
		return false;
	}
	
	public boolean checkPlayer(double x, double z, double dT){
		double dX = x - MainClass.player.locationX;
		double dZ = z - MainClass.player.locationZ;
		double distance = Math.sqrt(dZ*dZ + dX*dX);
		
		if(distance <= MainClass.player.playersize + enemysize)
			return true;
		
		return false;
	}
	
	public boolean checkWallOrDoor(double x, double z, double dT){
		boolean res = false;
		
		for(int i = 0; i < 360; i = i + 15)
			if(maze.isWall( x+enemysize*Math.sin(i*Math.PI/180) , locationY , z-0.8f+enemysize*Math.cos(i*Math.PI/180) )
					|| maze.isDoor( x+enemysize*Math.sin(i*Math.PI/180) , locationY , z-0.8f+enemysize*Math.cos(i*Math.PI/180) ))
				res = true;
		
		return res;
	}
	
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
	
	public boolean needRemoval(){
		return remove;
	}
		
	public void update(int deltaTime, Player player){
		px = player.locationX;
		py = player.locationY;
		pz = player.locationZ;

		if(!dood){
			if(alerted(player)){
				alert = alerted(player);
			}
			if(alert){
				highestPher = MainClass.mazePheromones.Search(locationX, locationY, locationZ, 15);
				
				
				double dX = highestPher.x - locationX;				
				double dZ = highestPher.z - locationZ;				
				double distance = Math.sqrt(dZ*dZ + dX*dX);
				
				newX = locationX;		
				newZ = locationZ;
				
				if(distance > 0.01){
					dX = dX/distance * speed * deltaTime;
					dZ = dZ/distance * speed * deltaTime;
					
					newX += dX;
					newZ += dZ;
					
					if(!collision(newX, newZ, deltaTime)){
						locationX = newX;
						locationZ = newZ;
					}
					
					else{
						newX = locationX;
						if(dX > 0.0){
							newX += speed * deltaTime;
						}
						else if(dX < 0.0){
							newX -= speed * deltaTime;
						}
							
						newZ = locationZ;
						if(dZ > 0.0){
							newZ += speed * deltaTime;
						}
						
						else if(dZ < 0.0){
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
			
			this.caught(player);
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
				//berekening hoek
					double inP = highestPher.z-locationZ;
					double lengteV = 1;
					double lengteW = Math.sqrt(Math.pow(highestPher.x-locationX, 2)+Math.pow(highestPher.z-locationZ, 2));
					double test = inP/Math.max(lengteV*lengteW, 00001);
					angle = Math.acos(test)*180/Math.PI;
					if(highestPher.x<locationX){
						angle = -angle;
					}
					
				}
			else if(dood){
				if(deathAngle>-90){
					deathAngle -= 2.5;
				}
				else if(deathAngle<=-90){
					remove = true;
					MainClass.player.setScore(100);
				}
			}
			gl.glRotated(20 ,Math.cos(angle*Math.PI/180), 0, -Math.sin(angle*Math.PI/180));
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
			
			gl.glTranslated(locationX, locationY+5,locationZ);
			
			//berekening hoek
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
	
	public void getMaze(Maze maze){
		this.maze = maze;
	}

	public void caught(Player player){
		double dX = player.locationX - locationX;				
		double dZ = player.locationZ - locationZ;				
		double distance = Math.sqrt(dZ*dZ + dX*dX);
		
		if( distance < (player.playersize + enemysize + 0.1) && Math.abs(locationY - player.locationY) < 0.6*maze.SQUARE_SIZE ){
			if(attackTimeout == 0){
				player.setDeltaHealth(Math.min(0, -attackPower+player.getDefensePower()));
				attackTimeout = 15;
			} else{
				attackTimeout--;
			}
			if(player.getHealth() <= 0){
				MainClass.state.GameStateUpdate(GameState.GAMEOVER_STATE);
				MainClass.state.setStopMainGame(true);
				MainClass.state.setStopGameOver(false);
			}
		}
	}
	
	public boolean alerted (Player player){
		 boolean res = Math.sqrt(Math.pow(sx-player.locationX,2 )+Math.pow(sz-player.locationZ,2)) <15 ;
		 return res;
	}
	
	public boolean damage(double x, double y, double z, double h, double d){
		double r=Math.sqrt(Math.pow(3,2)-Math.pow(Math.abs(x-locationX),2));
		double yDet;
		if(type.equals("3d_object/Predator/Predator_Youngblood/Body.obj")){
			yDet = 4.5;
		}
		else{
			yDet=2.5;
		}
		if(locationX+5>x && x>locationX-5 && locationZ+r>z && z>locationZ-r && y>0 + locationY && y <yDet +locationY){
			health -=d;
			if(health<=0){
				dood = true;
			}
			return true;
		}
		return false;
	}
	
}
