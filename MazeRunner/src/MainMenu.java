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

public class MainMenu extends Frame implements GLEventListener, MouseListener{
	private GLCanvas canvas;
	private int screenWidth = 600, screenHeight = 600;		// Screen size.
	private int buttonSizeX = 250;
	private int buttonSizeY = 100;

	public MainMenu(){
		super("Main Menu");
		
		// Let's change the window to our liking.
		setSize( screenWidth, screenHeight);
		setBackground(new Color(1.0f, 0.0f, 0.0f, 1));
		
		// The window also has to close when we want to.
		this.addWindowListener( new WindowAdapter()
		{
			public void windowClosing( WindowEvent e )
			{
				System.exit(0);
			}
		});
		
		// The OpenGL capabilities should be set before initializing the
		// GLCanvas. We use double buffering and hardware acceleration.
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);

		// Create a GLCanvas with the specified capabilities and add it to this
		// frame. Now, we have a canvas to draw on using JOGL.
		canvas = new GLCanvas(caps);
		add(canvas);

		// Set the canvas' GL event listener to be this class. Doing so gives
		// this class control over what is rendered on the GL canvas.
		canvas.addGLEventListener(this);

		// Also add this class as mouse listener, allowing this class to react
		// to mouse events that happen inside the GLCanvas.
		canvas.addMouseListener(this);
		
//		initJOGL();							// Initialize JOGL.

//		initObjects();						// Initialize all the objects!
		
		// Set the frame to visible. This automatically calls upon OpenGL to prevent a blank screen.
		setVisible(true);
	}
	
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		// Set the clear color and clear the screen.
		gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// Draw the buttons.
		drawButtons(gl);
		GLUT glut = new GLUT();
		gl.glColor3f(1.0f,  1.0f, 1.0f);
		gl.glRasterPos2f(screenWidth/2.0f-buttonSizeX/2.0f, screenHeight-2.0f*buttonSizeY );
		glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "Start Game");
		gl.glFlush();
	}
	
	private void drawButtons(GL gl) {
		// Draw the background boxes
		gl.glColor3f(0, 1.0f, 0f);
		boxOnScreen(gl, screenWidth/2.0f-buttonSizeX/2.0f, screenHeight-2.0f*buttonSizeY , buttonSizeX, buttonSizeY);
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
	//public void display(GLAutoDrawable arg0) {
	//	// TODO Auto-generated method stub
	//	
	//}

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
		gl.glOrtho(0, screenWidth, 0, screenHeight, -1, 1);

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
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		new MainMenu();
	}

}
