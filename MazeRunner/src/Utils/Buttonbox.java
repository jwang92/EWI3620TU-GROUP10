package Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import Main.MainClass;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

public class Buttonbox {
	private MainClass main;
	
	private int buttonSizeX, buttonSizeY;
	private int PosX, PosY;
	private int screenWidth, screenHeight;
	private boolean mouseOnBox;	//selected
	
	private ArrayList<Texture> textures;
	private ArrayList<String> textureNames;

	private Texture tempTexture;
	private String textureName;
	private String textureFileName;
	private String textureFileType;

	public Buttonbox(int xCoord, int yCoord, int xSize, int ySize, String textureName, MainClass mclass){
		main = mclass;
		PosX = xCoord;
		PosY = yCoord;
		buttonSizeX = xSize;
		buttonSizeY = ySize;
		this.textureName = textureName;
		
		screenHeight = main.screenHeight;
		screenWidth = main.screenWidth;
		
		textureFileType = "png";
	}

	public void loadTextures(GL gl){
		try {
		    File folder = new File("menu_files/");
		    File[] tList = folder.listFiles();
		    
		    textures = new ArrayList<Texture>();
			textureNames = new ArrayList<String>();
		    
		    for (File file : tList)
		    {
	            if( file.getName().equals(textureName+"."+textureFileType) 
	            		|| file.getName().equals(textureName+"_over."+textureFileType) )
	            {
	            	//Get the name of the texture
	            	textureFileName = "menu_files/" + file.getName();
	            	
	            	//Load the texture
	            	File filetexture = new File(textureFileName);
	    			TextureData data;
	    			data = TextureIO.newTextureData(filetexture, false, textureFileType);
	    			tempTexture = TextureIO.newTexture(data);
	    			
	    			//Set the the texture parameters
	    			tempTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
	    			tempTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
	    			tempTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
	    			tempTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
	    			
	    			//Add the texture to the arraylist
	    			textures.add(tempTexture);
	            	textureNames.add(textureFileName);
	            	
	            	textureFileName = textureNames.get(0);
	            }	
	        }
			
			//GenerateMipmap
			//gl.glGenerateMipmapEXT(GL.GL_TEXTURE_2D);
			
			// Use linear filter for texture if image is larger than the original texture
			//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
			
			// Use linear filter for texture if image is smaller than the original texture
			//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
			
			//Select the texture coordinates

		} catch (GLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void drawButtonbox(GL gl, int scrH, int scrW) {
		
		adjustToReshape(scrH, scrW);
		
		int textureID;
		if(mouseOnBox){
			textureID = textureNames.lastIndexOf("menu_files/"+textureName+"_over.png");
		}
		else{
			textureID = textureNames.lastIndexOf("menu_files/"+textureName+".png");
		}
		
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		textures.get(textureID).getTarget();
		textures.get(textureID).bind();
		
		gl.glBegin(GL.GL_POLYGON);
			gl.glTexCoord2f(0, 1); gl.glVertex2f(PosX, PosY);
			gl.glTexCoord2f(1, 1); gl.glVertex2f(PosX + buttonSizeX, PosY);
			gl.glTexCoord2f(1, 0); gl.glVertex2f(PosX + buttonSizeX, PosY + buttonSizeY);
			gl.glTexCoord2f(0, 0); gl.glVertex2f(PosX, PosY + buttonSizeY);
		gl.glEnd();
		
		textures.get(textureID).disable();
		gl.glDisable(GL.GL_BLEND);

	}
	
	public void adjustToReshape(int scrH, int scrW){
		double SX = (double) scrW / (double) screenWidth;
		double SY = (double) scrH / (double) screenHeight;
				
		if(SX != 1 || SY != 1){
			screenHeight = scrH;
			screenWidth = scrW;

			PosX = (int) Math.round(SX*PosX);
			PosY = (int) Math.round(SY*PosY);
			buttonSizeX = (int) Math.round(SX*buttonSizeX);
			buttonSizeY = (int) Math.round(SY*buttonSizeY);
		}
	}

	public boolean OnBox(int x, int y){
		y = screenHeight - y;
		
		boolean withinX = PosX < x && x < PosX+buttonSizeX;
		boolean withinY = PosY < y && y < PosY+buttonSizeY;
		
		return withinX && withinY;
	}
	
	public void ChangeTexture(boolean OnBox){
		mouseOnBox = OnBox;
	}

}
