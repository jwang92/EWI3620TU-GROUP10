import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;


public class Shield extends GameObject implements VisibleObject {
	private Maze maze; 										// The maze.
	private double newX, newZ;
	private double speed = 0.0015;
	private Model m ;
	private int displayList;
	private Player player;
	private boolean texture;
	private IntBuffer vboHandle = IntBuffer.allocate(10);
	
	//public static boolean upgrade;
	
	private int defenseCounter=0;
	private double defenseAngle = 0;
	//Shaders
	private int shaderProgram = 0;
	
	public Shield(double x, double y, double z,boolean tex, boolean upgrade){
		super(x, y, z);
		texture = tex;
		try {
			if(!upgrade){
				if(texture){
					m = OBJLoader.loadTexturedModel((new File("3d_object/Shield/Shield.obj")));
				}
				else{
					m = OBJLoader.loadModel((new File("3d_object/sShield/Shield.obj.obj")));
				}
			}
			else{
			}
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
	
	public void setShaderProgram(int program){
		shaderProgram = program;
	}
	
	public boolean checkWall(double x, double z, double dT){
		double d = 2.0d; 		//distance from the wall
		boolean res = false;
		
		for(int i = 0; i < 360; i = i + 15)
			if(maze.isWall( x+d*Math.sin(i*Math.PI/180) , locationY , z-0.8f+d*Math.cos(i*Math.PI/180) ))
				res = true;
		
		return res;
	}
	
//	public void genDisplayList(GL gl){
//        displayList = OBJLoader.createDisplayList(m, gl);
//	}
	
	public void genVBO(GL gl){
		vboHandle = OBJLoader.createVBO(m, gl);
	}
		
	public void update(int deltaTime, Player player){
		locationX=player.locationX;
		locationY=player.locationY;
		locationZ=player.locationZ;
	}

	public void display(GL gl) {
		gl.glPushMatrix();
		
		//Enable the shaderprogram
		if(shaderProgram >0){
			gl.glUseProgram(shaderProgram);
		}
		
		//Draw nothing if the vboHandle are not loaded
		if(vboHandle.get(0)<=0||vboHandle.get(1)<=0){
			
		}
		
		else{
			
			//Translate the model
			gl.glTranslated(locationX, locationY, locationZ);
			double h = Math.toRadians(player.getHorAngle());
			double v = Math.toRadians(player.getVerAngle());
			double tx = -Math.cos(v)*Math.sin(h);
			double tz = -Math.cos(v)*Math.cos(h);
			double ty = Math.sin(v);
			gl.glTranslated(tx, ty, tz);
			
			//Rotate the model
			gl.glRotated(player.getHorAngle(), 0, 1, 0);
			gl.glRotated(player.getVerAngle(), 1,0 ,0);	
			
			if(player.control.getDefense() && defenseAngle >-90)
			{
				defenseAngle -= 5;
			}
			if(player.control.getDefense() && defenseAngle <=-90)
			{
				player.setDefensePower(5);;
			}
			else if(!player.control.getDefense() && defenseAngle < 0)
			{
					defenseAngle += 5;
			}
			else if(!player.control.getDefense() && defenseAngle >= 0)
			{
				defenseAngle = 0;
			}		
			gl.glRotated(defenseAngle,0,1,0);
			//Reset the color to white
			gl.glColor4f(1.0f,1.0f,1.0f,1.0f);
			
			//Initialize counters
			int vertexSize = 0;
			int vertexCount = 0;
			
			//Initialize the texture
			Texture tempTexture = null;
			for(int i=0;i<m.getModelParts().size();i++){
				ModelPart p = m.getModelParts().get(i);
				ModelPart.Face face = p.getFaces().get(0);
				
				//the modelPart has textureCoordinates so the material should be used
				if(face.hasMaterial()){
	                gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, new float[] {1f,face.getMaterial()
                            .diffuseColour[0], face.getMaterial().diffuseColour[1],
                            face.getMaterial().diffuseColour[2]},1);
	                gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, new float[] {1f,face.getMaterial()
                            .ambientColour[0], face.getMaterial().ambientColour[1],
                            face.getMaterial().ambientColour[2]}, 1);
	                gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, face.getMaterial().specularCoefficient);
				}
				
				//Use default material
				else{
	                gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, new float[] {1.0f, 1.0f, 1.0f, 1.0f}, 1);
					gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, new float[] {1.0f, 1.0f, 1.0f, 1.0f}, 1);
	                gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 120f);
				}
				
				//Enable the texture if the modelPart has a texture
				if(face.hasTexture()){
					gl.glActiveTexture(GL.GL_TEXTURE0);
					gl.glEnable(GL.GL_TEXTURE_2D);
					tempTexture = face.getTexture();
					tempTexture.bind();
				}

				//Bind the vbo buffers for the normal and vertex arrays
                vertexSize = p.getFaces().size()*3;
				gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 120f);
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboHandle.get(0));
				gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0L);
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboHandle.get(1));
				gl.glNormalPointer(GL.GL_FLOAT, 0, 0L);
				
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboHandle.get(2));
				gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0L);

				//Enable the Array draw Mode for vertex,normals and textures
				gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
				gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
				
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
	}
	
	public void setMaze(Maze maze){
		this.maze = maze;
	}

	public void setPlayer(Player player){
		this.player=player;
	}
	
}