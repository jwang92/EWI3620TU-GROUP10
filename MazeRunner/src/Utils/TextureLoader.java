package Utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

public class TextureLoader {
	
	public static Texture loadTexture(String texturePath){
		Texture tempTexture = null; 
		try {
			//Get the name of the texture
	    	if(texturePath.endsWith(".png")){
	        	//Load the texture
	        	File filetexture = new File(texturePath);
	    		TextureData data;
	    		data = TextureIO.newTextureData(filetexture, false, "png");
	    		tempTexture = TextureIO.newTexture(data);
	    		
	    		//Set the the texture parameters
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
	    		
	    	}
	    	else if(texturePath.endsWith(".tga")){
	        	//Load the texture
	        	File filetexture = new File(texturePath);
	    		TextureData data;
	    		data = TextureIO.newTextureData(filetexture, false, "tga");
	    		tempTexture = TextureIO.newTexture(data);
	    		
	    		//Set the the texture parameters
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
	    	}
	    	else if(texturePath.endsWith(".jpg")){
	        	//Load the texture
	        	File filetexture = new File(texturePath);
	    		TextureData data;
	    		data = TextureIO.newTextureData(filetexture, false, "jpg");
	    		tempTexture = TextureIO.newTexture(data);
	    		
	    		//Set the the texture parameters
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
	    		tempTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
	    	}
		} catch (GLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tempTexture;
	}
	
	public static ArrayList<HashMap> loadTextureArray(String stringFolder){
		ArrayList<HashMap> res = new ArrayList<HashMap>();
		HashMap<String, Texture> tempTextureHashMap = new HashMap<String, Texture>();
		try {
			Texture tempTexture;
			String textureFileName = "";
			String textureFileType = "png";
		    File folder = new File(stringFolder);
		    File[] tList = folder.listFiles();
		    int numberOfTextures = tList.length;
			for(int j = 0; j<tList.length;j++){
			    if(tList[j].getName().equals("Thumbs.db")){
			    	numberOfTextures -= 1;
			    }  
			}
			res = new ArrayList<HashMap>(numberOfTextures);
		    for (File file : tList)
		    {
		    	tempTextureHashMap = new HashMap<String, Texture>();
	            if(!file.getName().equals("Thumbs.db"))
	            {
	            	//Get the name of the texture
	            	textureFileName = stringFolder + file.getName();
	            	
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
	    			tempTextureHashMap.put(textureFileName, tempTexture);
	    			res.add(tempTextureHashMap);
	            	//Load the texture coordinates
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
	return res;
	}
}
