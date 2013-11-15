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

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class MainMenu implements GLEventListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int buttonSizeX;
	public int buttonSizeY;
	public float b1PosX;
	public float b1PosY;
	public int ScreenWidth, ScreenHeight;
	private UserInput input;
	
	public MainMenu(int screenHeight, int screenWidth){
		buttonSizeX = 250;
		buttonSizeY = 100;
		ScreenWidth= screenWidth;
		ScreenHeight = screenHeight;
		b1PosX =ScreenWidth/2.0f-buttonSizeX/2.0f;
		b1PosY=ScreenHeight-2.0f*buttonSizeY;
	}
	
	public void render (GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		// Set the clear color and clear the screen.
		gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// Draw the buttons.
		drawButtons(gl);
		GLUT glut = new GLUT();
		gl.glColor3f(1.0f,  1.0f, 1.0f);
		gl.glRasterPos2f(b1PosX,b1PosY );
		glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "Start Game");
		gl.glFlush();
	}
	
	private void drawButtons(GL gl) {
		// Draw the background boxes
		gl.glColor3f(0, 1.0f, 0f);
		boxOnScreen(gl, b1PosX, b1PosY, buttonSizeX, buttonSizeY);
	}
	
	private void boxOnScreen(GL gl, float x, float y, float sizeX, float sizeY) {
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + sizeX, y);
		gl.glVertex2f(x + sizeX, y + sizeY);
		gl.glVertex2f(x, y + sizeY);
		gl.glEnd();
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
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
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
		
		if (me.getY() >100 && me.getY() <200 && me.getX() > b1PosX && me.getX() < b1PosX+buttonSizeX) {
			
				MainClass.state.GameStateUpdate(GameState.MAINGAME_STATE);
			} 
	}


	
}
