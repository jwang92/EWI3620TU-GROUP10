package GameObject;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL;

import Main.MainClass;
import Model.Model;
import Model.ModelPart;
import Model.OBJLoader;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;

public class RangedWeapons extends GameObject implements VisibleObject {
	
	private int displayList;
	private Player player;
	private boolean texture;
	
	private ArrayList<Model> models;
	private ArrayList<IntBuffer> handles;
	private int currentGun = 0;
	
	//Shaders
	private int shaderProgram = 0;
	
	public RangedWeapons(double x, double y, double z, boolean tex, int type, MainClass mclass) {
		super(x,y,z, mclass);
		currentGun = type;
		handles = new ArrayList<IntBuffer>();
		models = new ArrayList<Model>();
	
		String[] modelNames = new String[1];
		modelNames[0] = "3d_object/raygun/raygun_mark2.obj";
		
		texture = tex;
		
		try {
			
			if(texture){
				
				for (String mName : modelNames) {
					Model m = OBJLoader.loadTexturedModel((new File(mName)));
					models.add(m);
				}
				
			}
			else{
				Model m = OBJLoader.loadModel((new File("3d_object/raygun/raygun_mark2.obj")));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void update(int deltaTime, Player player){
		this.player = player;
		locationX=player.locationX;
		locationY=player.locationY;
		locationZ=player.locationZ;
		if(player.control.getAttack()){
			main.bullets.add(new Bullet(locationX,locationY,locationZ, player,main));
			player.sound.photon();
			player.control.setAttack(false);
		}
	}
	
	public void switchGun(int g){
		currentGun = g;
	}
	
	public void display(GL gl) {
		gl.glPushMatrix();
		
		//Enable the shaderprogram
		if(shaderProgram >0){
			gl.glUseProgram(shaderProgram);
		}
		
		//Draw nothing if the vboHandle are not loaded
		if(handles.size() == 0){
			
		}
		
		else{	
			IntBuffer vboHandle = IntBuffer.allocate(10);
			vboHandle = handles.get(currentGun);
			
			//Translate the model
			gl.glTranslated(locationX, locationY - main.player.getdY_walk(), locationZ);
			double h = Math.toRadians(player.getHorAngle());
			double v = Math.toRadians(player.getVerAngle());
			double tx = -Math.cos(v)*Math.sin(h);
			double tz = -Math.cos(v)*Math.cos(h);
			double ty = Math.sin(v);
			gl.glTranslated(tx, ty, tz);
			
			//Rotate the model
			gl.glRotated(player.getHorAngle(), 0, 1, 0);
			gl.glRotated(player.getVerAngle(), 1,0 ,0);
			if(player.control.getAttack()){
				
			}
			
			
			//Reset the color to white
			gl.glColor4f(1.0f,1.0f,1.0f,1.0f);
			
			//Initialize counters
			int vertexSize = 0;
			int vertexCount = 0;
			
			//Initialize the texture
			Texture tempTexture = null;
			Model m = models.get(currentGun);
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
	
	public void genVBO(GL gl){
		
		for(int i = 0; i < models.size(); i++){
			
			IntBuffer buf = IntBuffer.allocate(10);
			buf = OBJLoader.createVBO(models.get(i), gl);
			
			handles.add(buf);
			
		}
		
	}
	
	public void setShaderProgram(int program){
		shaderProgram = program;
	}
	

	public void setPlayer(Player player){
		this.player=player;
	}
	
}
