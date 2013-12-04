import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Pause implements GLEventListener, MouseListener, MouseMotionListener {
	/*
	 * **********************************************
	 * *			Local Variables					*
	 * **********************************************
	 */
	
	//frame setup
	public int ScreenWidth, ScreenHeight, drawScreenWidth, drawScreenHeight;
	//buttons setup
	private int buttonSizeX, buttonSizeY;
	private int bPosX;
	private int b1PosY, b2PosY;
	private int drawbuttonSizeX, drawbuttonSizeY;
	private int drawPosX;
	private int draw1PosY, draw2PosY;
	private int mouseOnBox = 0;
	boolean stop = false;
	private boolean startup = true;
	
	private ArrayList<Texture> textures;
	private ArrayList<String> textureNames;

	private Texture tempTexture;
	private String textureFileName = "";
	private String textureFileType = "png";
	
	public Pause(int screenHeight, int screenWidth){
		initWindowSize(screenHeight, screenWidth);
		
		textures = new ArrayList<Texture>();
		textureNames = new ArrayList<String>();
		
		setButtonSize();
				
		setDrawButtons();
	
//		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
//		 * The Animator class handles that for JOGL.
//		 */
//		Animator anim = new Animator( MainClass.canvas );
//		anim.start();
		
		// Also add this class as mouse motion listener, allowing this class to
		// react to mouse events that happen inside the GLCanvas.
		MainClass.canvas.addMouseMotionListener(this);
//		MainClass.canvas.addGLEventListener(this);

	}
	
	public void initWindowSize(int screenHeight, int screenWidth){
		ScreenWidth = screenWidth;
		ScreenHeight = screenHeight;
		buttonSizeX = (int) (ScreenWidth/7);
		buttonSizeY = (int) (ScreenHeight/13);
//		System.out.println("Screen: " + ScreenWidth + " " + ScreenHeight + " " + buttonSizeX + " " + buttonSizeY);
	}
	
	public void setButtonSize(){
		bPosX = (int) (ScreenWidth/6.0f - buttonSizeX/2.0f);
		b1PosY = (int) (ScreenHeight/1.2f - 0.5f*buttonSizeY);
		b2PosY = (int) (ScreenHeight/1.2f - 1.6f*buttonSizeY);
		
//		System.out.println("BottomLeft buttons (x,y1,y2,y3): " + bPosX + " , " + b1PosY + " , " + b2PosY + " , " + b3PosY);
	}
	
	public void setDrawButtons(){
		drawScreenHeight = ScreenHeight;
		drawScreenWidth = ScreenWidth;
		
		drawPosX = bPosX;
		draw1PosY = b1PosY;
		draw2PosY = b2PosY;
		drawbuttonSizeX = buttonSizeX;
		drawbuttonSizeY = buttonSizeY;
	}
	
	public void render (GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		// Set the clear color and clear the screen.
		gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		drawBackground(gl);
		// Draw the buttons.
		drawButtons(gl);
		
		gl.glFlush();
	

	}
	
	public void loadTextures(GL gl){
		try {
		    File folder = new File("menu_files/");
		    File[] tList = folder.listFiles();
		    int numberOfTextures = tList.length;
			for(int j = 0; j<tList.length;j++){
			    if(tList[j].getName().equals("Thumbs.db")){
			    	numberOfTextures -= 1;
			    }  
			    
			}
			textureNames = new ArrayList<String>(numberOfTextures);
		    
		    int i = 0;
		    for (File file : tList)
		    {
		    	
	            if(!file.getName().equals("Thumbs.db"))
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
	            	
	            	i++;
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
	
	private void drawBackground(GL gl) {
		
		if(startup){
			
			loadTextures(gl);
			
			startup = false;
			
		}

		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glColor3f(1.0f, 1.0f, 1.0f);

		int textureID = textureNames.lastIndexOf("menu_files/background_ufos.png");
		
		textures.get(textureID).getTarget();
		textures.get(textureID).bind();
				
		gl.glBegin(GL.GL_POLYGON);
			gl.glTexCoord2f(0, 1); gl.glVertex2f(0, 0);
			gl.glTexCoord2f(0, 0); gl.glVertex2f(0, drawScreenHeight);
			gl.glTexCoord2f(1, 0); gl.glVertex2f(drawScreenWidth, drawScreenHeight);
			gl.glTexCoord2f(1, 1); gl.glVertex2f(drawScreenWidth, 0);
		gl.glEnd();
		
		textures.get(textureID).disable();
		
		
	}
	
	private void drawButtons(GL gl) {
		// Draw the background boxes
		
		boxOnScreen(gl, drawPosX, draw1PosY, 1);
		
		boxOnScreen(gl, drawPosX, draw2PosY, 2);
		
		
	}
	
	private void boxOnScreen(GL gl, float x, float y, int boxnum) {
		
		String texNaam = null;
		switch(boxnum){
		case 1:
			texNaam = "start";
			break;
		case 2:
			texNaam = "editor";
			break;
		case 3:
			texNaam = "exit";
			break;
			
		
		}

		int textureID;
		if(mouseOnBox == boxnum){
			textureID = textureNames.lastIndexOf("menu_files/"+texNaam+"_over.png");
		}
		else{
			textureID = textureNames.lastIndexOf("menu_files/"+texNaam+".png");
		}

		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		textures.get(textureID).getTarget();
		textures.get(textureID).bind();
		
		gl.glBegin(GL.GL_POLYGON);
			gl.glTexCoord2f(0, 1); gl.glVertex2f(x, y);
			gl.glTexCoord2f(1, 1); gl.glVertex2f(x + drawbuttonSizeX, y);
			gl.glTexCoord2f(1, 0); gl.glVertex2f(x + drawbuttonSizeX,  y + drawbuttonSizeY);
			gl.glTexCoord2f(0, 0); gl.glVertex2f(x,  y + drawbuttonSizeY);
		gl.glEnd();
		
		textures.get(textureID).disable();
		gl.glDisable(GL.GL_BLEND);

	}
	
	public boolean ButtonPressed(int buttonX, int buttonY, int xin, int yin){
		yin = ScreenHeight - yin;

		boolean withinX = buttonX < xin && xin < buttonX+buttonSizeX;
		boolean withinY = buttonY < yin && yin < buttonY+buttonSizeY;
		
		return withinX && withinY;
	}
	
	
/*
 * **********************************************
 * *		OpenGL event handlers				*
 * **********************************************
 */

	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		render(arg0);
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// Retrieve the OpenGL handle, this allows us to use OpenGL calls.
		GL gl = arg0.getGL();
		

		// Set the matrix mode to GL_PROJECTION, allowing us to manipulate the
		// projection matrix
		gl.glMatrixMode(GL.GL_PROJECTION);

		// Always reset the matrix before performing transformations, otherwise
		// those transformations will stack with previous transformations!
		gl.glLoadIdentity();

		/*
		 * glOrtho performs an "orthogonal projection" transformation on the
		 * active matrix. In this case, a simple 2D projection is performed,
		 * matching the viewing frustum to the screen size.
		 */
		gl.glOrtho(0, ScreenWidth, 0, ScreenHeight, -1, 1);

		// Set the matrix mode to GL_MODELVIEW, allowing us to manipulate the
		// model-view matrix.
		gl.glMatrixMode(GL.GL_MODELVIEW);

		// We leave the model view matrix as the identity matrix. As a result,
		// we view the world 'looking forward' from the origin.
		gl.glLoadIdentity();

		// We have a simple 2D application, so we do not need to check for depth
		// when rendering.
		gl.glDisable(GL.GL_DEPTH_TEST); 
		gl.glDisable(GL.GL_LIGHTING);
		
		MainClass.state.setStopMainGame(true);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();

		// Set the new screen size and adjusting the sizes
		initWindowSize(height, width);
		setButtonSize();
		
//		System.out.println(x + " " + y);
		
		gl.glViewport(0, 0, ScreenWidth, ScreenHeight);
		
	}

/*
 * **********************************************
 * *		Mouse event handlers				*
 * **********************************************
 */
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		int Xin = me.getX();
		int Yin = me.getY();
		
		if (ButtonPressed( (int) bPosX, (int) b1PosY, Xin, Yin)){
			MainClass.canvas.removeGLEventListener(this);
			MainClass.state.GameStateUpdate(GameState.MAINGAME_STATE);
			MainClass.state.setStopPause(true);
			MainClass.state.setStopMainGame(false);
		}
		else if(ButtonPressed( (int) bPosX, (int) b2PosY, Xin, Yin)){
			MainClass.canvas.removeGLEventListener(this);
			MainClass.initObjects();
			MainClass.state.GameStateUpdate(GameState.TITLE_STATE);
			MainClass.state.setStopPause(true);
			MainClass.state.setStopTitle(false);
		}
	}

	/*
	 * **********************************************
	 * *		Mouse Motion event handlers			*
	 * **********************************************
	 */
		
		@Override
		public void mouseMoved(MouseEvent me){
			int Xin = me.getX();
			int Yin = me.getY();
			
			if (ButtonPressed( (int) bPosX, (int) b1PosY, Xin, Yin))
				mouseOnBox = 1;
			else if(ButtonPressed( (int) bPosX, (int) b2PosY, Xin, Yin))
				mouseOnBox = 2;
			else
				mouseOnBox = 0;
		
//			System.out.println(mouseOnBox);
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	
}
