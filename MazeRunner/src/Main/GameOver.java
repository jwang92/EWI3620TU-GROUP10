package Main;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
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

import Utils.Buttonbox;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameOver implements GLEventListener, MouseListener, MouseMotionListener {
	/*
	 * **********************************************
	 * *			Local Variables					*
	 * **********************************************
	 */
	
	boolean stop = false;
	private boolean startup = true;
	
	private Texture BGTexture;
	
	private ArrayList<Buttonbox> buttons;

	
	public GameOver(){
		
		setButtons();
				
		// Also add this class as mouse motion listener, allowing this class to
		// react to mouse events that happen inside the GLCanvas.
		MainClass.canvas.addMouseMotionListener(this);

	}
	
	public void setButtons(){
		buttons = new ArrayList<Buttonbox>();

		int buttonSizeX = (int) (MainClass.screenWidth/7);
		int buttonSizeY = (int) (MainClass.screenHeight/13);

		int x = (int) (MainClass.screenWidth/6.0f - buttonSizeX/2.0f);
		int y1 = (int) (MainClass.screenHeight/1.2f - 0.5f*buttonSizeY);
		int y2 = (int) (MainClass.screenHeight/1.2f - 1.6f*buttonSizeY);
		
		buttons.add( new Buttonbox(x, y1, buttonSizeX, buttonSizeY, "start") );
		buttons.add( new Buttonbox(x, y2, buttonSizeX, buttonSizeY, "exit") );
		
	}
		
	public void render (GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		// Set the clear color and clear the MainClass.screen.
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// Draw background
		drawBackground(gl);
		
		// Draw the buttons.
		drawButtons(gl);

		gl.glFlush();
	

	}
	
	public void drawUsername(GL gl){
		
		float fontSize = MainClass.screenWidth / 60f;
		
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glBegin(GL.GL_QUADS);
			gl.glColor4f(0f, 0f, 0f, 0.5f);
			gl.glVertex2f(0, 0);
			gl.glVertex2f(0, MainClass.screenHeight * 0.07f);
			gl.glVertex2f(MainClass.screenWidth, MainClass.screenHeight * 0.07f);
			gl.glVertex2f(MainClass.screenWidth, 0);
		gl.glEnd();
		
		gl.glDisable(GL.GL_BLEND);
		
		Font f2 = null;
		try {
			f2 = Font.createFont(Font.TRUETYPE_FONT, new File("fontje.ttf"));
		} catch (Exception e){
			//
		}
		
		Font f = f2.deriveFont(fontSize);
		TextRenderer t = new TextRenderer(f);

		t.beginRendering(MainClass.screenWidth, MainClass.screenHeight);
		t.draw("Ingelogd als " + MainClass.username, (int) (MainClass.screenWidth * 0.02f), (int) (MainClass.screenHeight * 0.02f));
		t.endRendering();
		
	}

	public void loadBackground(GL gl, String textureName, String textureFileType){
		try {
		    File folder = new File("menu_files/");
		    File[] tList = folder.listFiles();
		    
		    for (File file : tList)
		    {
	            if( file.getName().equals(textureName+"."+textureFileType) )
	            {
	            	//Get the name of the texture
	            	String textureFileName = "menu_files/" + file.getName();
	            	
	            	//Load the texture
	            	File filetexture = new File(textureFileName);
	    			TextureData data;
	    			data = TextureIO.newTextureData(filetexture, false, textureFileType);
	    			BGTexture = TextureIO.newTexture(data);
	    			
	    			//Set the the texture parameters
	    			BGTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
	    			BGTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
	    			BGTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
	    			BGTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
	    			
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
			loadBackground(gl, "background_ufos", "png");
			
			for( Buttonbox button : buttons)
				button.loadTextures(gl);
			
			startup = false;
			
		}

		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		BGTexture.getTarget();
		BGTexture.bind();
				
		gl.glBegin(GL.GL_POLYGON);
			gl.glTexCoord2f(0, 1); gl.glVertex2f(0, 0);
			gl.glTexCoord2f(0, 0); gl.glVertex2f(0, MainClass.screenHeight);
			gl.glTexCoord2f(1, 0); gl.glVertex2f(MainClass.screenWidth, MainClass.screenHeight);
			gl.glTexCoord2f(1, 1); gl.glVertex2f(MainClass.screenWidth, 0);
		gl.glEnd();
		
		BGTexture.disable();
		
	}
	
	private void drawButtons(GL gl) {
		// Draw the background boxes
		for( Buttonbox button : buttons)
			button.drawButtonbox(gl, MainClass.screenHeight, MainClass.screenWidth);
		
	}
	
/*
 * **********************************************
 * *		OpenGL event handlers				*
 * **********************************************
 */

	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
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
		 * matching the viewing frustum to the MainClass.screen size.
		 */
		gl.glOrtho(0, MainClass.screenWidth, 0, MainClass.screenHeight, -1, 1);

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
		
//		MainClass.state.setStopMainGame(true);
		MainClass.state.setStopGameOver(true);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
//		GL gl = drawable.getGL();
//
//		gl.glViewport(0, 0, MainClass.screenWidth, MainClass.screenHeight);
		
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
		
		if (buttons.get(0).OnBox(Xin, Yin) ){
			MainClass.canvas.removeGLEventListener(this);
			MainClass.initObjects();
			MainClass.state.GameStateUpdate(GameState.MAINGAME_STATE);
			MainClass.state.setStopGameOver(true);
			MainClass.input.setDefMouse(me);
			MainClass.state.setStopMainGame(false);
		}
		else if (buttons.get(1).OnBox(Xin, Yin) ){
			MainClass.canvas.removeGLEventListener(this);
			MainClass.initObjects();
			MainClass.state.GameStateUpdate(GameState.TITLE_STATE);
			MainClass.state.setStopGameOver(true);
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
		if(MainClass.state.getState()==4){
			int Xin = me.getX();
			int Yin = me.getY();
			
			boolean onBox = false;
			
			for(Buttonbox button : buttons){
				if(button.OnBox(Xin, Yin)){
					button.ChangeTexture( true );
					onBox = true;
				}
				else{
					button.ChangeTexture( false );
				}
			}
			
			if(onBox){
				MainClass.cursor.setCursor(-2);
			}
			else{
				MainClass.cursor.setCursor(-1);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
