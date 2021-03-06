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

import com.sun.opengl.util.texture.Texture;


public class Sword extends GameObject implements VisibleObject {										
	private double speed = 0.0015;
	private Player player;
	public int attackCounter=0;
	private boolean texture;
		
	private ArrayList<Model> models;
	private ArrayList<IntBuffer> handles;
	private int currentSword = 0;
		
	//Shaders
	private int shaderProgram = 0;
	
	public Sword(double x, double y, double z, boolean tex, int type, MainClass mclass){
		super(x, y, z, mclass);
		handles = new ArrayList<IntBuffer>();
		models = new ArrayList<Model>();
	
		String[] modelNames = new String[3];
		modelNames[0] = "3d_object/sword with arm/sword_and_arm.obj";
		modelNames[1] = "3d_object/frostsword and arm/frostsword_and_arm.obj";
		modelNames[2] = "3d_object/lightsaber/lightsaber.obj";
		
		texture = tex;
		
		try {
			
			if(texture){
				
				for (String mName : modelNames) {
					Model m = OBJLoader.loadTexturedModel((new File(mName)));
					models.add(m);
				}
				
			}
			else{
				Model m = OBJLoader.loadModel((new File("3d_object/sword.obj")));
				models.add(m);
			}
		} catch (IOException e) {
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
		
	public void genVBO(GL gl){
		
		for(int i = 0; i < models.size(); i++){
			
			IntBuffer buf = IntBuffer.allocate(10);
			buf = OBJLoader.createVBO(models.get(i), gl);
			
			handles.add(buf);
			
		}
		
	}
		
	public void update(int deltaTime, Player player){
		locationX=player.locationX;
		locationY=player.locationY;
		locationZ=player.locationZ;
	}
	
	public void switchSword(int s){
		currentSword = s;
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
			vboHandle = handles.get(currentSword);
			
			//zorgt ervoor dat je niet kan aanvallen tijdens het aanvallen
			if(player.control.getAttack() && attackCounter==18){
				attackCounter=0;
				player.control.setAttack(false);
			}
			
			//Translate het model met bolco�rdinaten
			gl.glTranslated(locationX, locationY + main.player.getdY_walk(), locationZ);
			double h = Math.toRadians(player.getHorAngle());
			double v = Math.toRadians(player.getVerAngle());
			double tx = -Math.cos(v)*Math.sin(h);
			double tz = -Math.cos(v)*Math.cos(h);
			double ty = Math.sin(v);
			gl.glTranslated(tx, ty, tz);
			
			//Rotate het model
			gl.glRotated(player.getHorAngle(), 0, 1, 0);
			gl.glRotated(player.getVerAngle(), 1,0 ,0);
			if(player.control.getAttack()){
				gl.glTranslated(0, -0.8, 0);
				gl.glRotated(-5*attackCounter,1,0,0);
				gl.glTranslated(0, 0.8, 0);
				attackCounter +=1;
				//attack damage afhankelijk van soort zwaard
				if(attackCounter==18){
					int d = 0;
					switch(currentSword+1){
					case 1:
						d = 10;
						break;
					case 2:
						d = 20;
						break;
					case 3:
						d = 30;
						break;
					}
					//voor elk enemy kijk of hitpoint overeenkomt met hitbox
					for(Enemy e: main.enemies){
						e.damage(player.getLocationX()-2.2*Math.sin(player.getHorAngle()*Math.PI/180), 
								player.getLocationY(),
								player.getLocationZ()-2.2*Math.cos(player.getHorAngle()*Math.PI/180), 
								player.getHorAngle(), d);
					}
				}
					
			}
			
			
			//Reset the color to white
			gl.glColor4f(1.0f,1.0f,1.0f,1.0f);
			
			//Initialize counters
			int vertexSize = 0;
			int vertexCount = 0;
			
			//Initialize the texture
			Texture tempTexture = null;
			Model m = models.get(currentSword);
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
	
	public void setPlayer(Player player){
		this.player=player;
	}
}
