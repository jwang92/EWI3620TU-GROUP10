package GameObject;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import Main.MainClass;
import Model.Model;
import Model.ModelPart;
import Model.OBJLoader;
import com.sun.opengl.util.texture.Texture;

public class LeftLeg extends GameObject implements VisibleObject {										
	private Enemy enemy;
	
	//Model
	private Model m ;
	
	private boolean texture, forward =true;
	private IntBuffer vboHandle = IntBuffer.allocate(10);
	private double angle, rotateAngle=0;
	
	//Shaders
	private int shaderProgram = 0;
	
	//Death
	private int deathAngle = 0;
	private boolean remove = false;
	
	public LeftLeg(double x, double y, double z,boolean tex, String modelName, MainClass mclass){
		super(x, y, z, mclass);
		texture = tex;
		try {
			if(texture){
				m = OBJLoader.loadTexturedModel((new File("3d_object/Predator/Predator_Youngblood/LeftLeg.obj")));
			}
			else{
				m = OBJLoader.loadModel((new File("3d_object/Predator/Predator_Youngblood/LeftLeg.obj")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setShaderProgram(int program){
		shaderProgram = program;
	}

	
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
		locationX=enemy.locationX;
		locationY=enemy.locationY;
		locationZ=enemy.locationZ;
		if(rotateAngle<= 20 && !forward){
			rotateAngle+=(1.0/3.0);
		}
		else if(!forward){
			forward =true;
		}
		if(rotateAngle>= -20 && forward){
			rotateAngle -=(1.0/3.0);
		}
		else if(forward){
			forward =false;
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
			
			if(enemy.alert && !enemy.dood){
				angle=enemy.angle;
				
				gl.glTranslated(0, 2.5, 0);
				gl.glRotated(rotateAngle ,Math.cos(angle*Math.PI/180), 0, -Math.sin(angle*Math.PI/180));
				gl.glTranslated(0, -2.5, 0);
			}
			else if(enemy.dood){
				if(deathAngle>-90){
					deathAngle -= 2.5;
				}
				else if(deathAngle<=-90){
					remove = true;
					main.player.setScore(100);
				}
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
	}
	
	
	public void setEnemy(Enemy enemy){
		this.enemy = enemy;
	}

}
