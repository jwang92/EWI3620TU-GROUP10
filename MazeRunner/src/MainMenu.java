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

public class MainMenu implements GLEventListener, MouseListener , MouseMotionListener {
	/*
	 * **********************************************
	 * *			Local Variables					*
	 * **********************************************
	 */
	
	private static final long serialVersionUID = 1L;
	
	//frame setup
	public int ScreenWidth, ScreenHeight;
//	public int screenX, screenY;
	//buttons setup
	private int buttonSizeX, buttonSizeY, drawbuttonSizeX, drawbuttonSizeY;
	private int bPosX, drawPosX;
	private int b1PosY, b2PosY, b3PosY, draw1PosY, draw2PosY, draw3PosY;
	private int mouseOnBox = 0;

	
	public MainMenu(int screenHeight, int screenWidth){
		initWindowSize(screenHeight, screenWidth);
		
		setButtonSize();
				
		setDrawButtons();
		
		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		Animator anim = new Animator( MainClass.canvas );
		anim.start();
		
		// Also add this class as mouse motion listener, allowing this class to
		// react to mouse events that happen inside the GLCanvas.
		MainClass.canvas.addMouseMotionListener(this);
		MainClass.canvas.addGLEventListener(this);

	}

	public void initWindowSize(int screenHeight, int screenWidth){
		ScreenWidth = screenWidth;
		ScreenHeight = screenHeight;
		buttonSizeX = (int) (ScreenWidth/3);
		buttonSizeY = (int) (ScreenHeight/6);
//		System.out.println("Screen: " + ScreenWidth + " " + ScreenHeight + " " + buttonSizeX + " " + buttonSizeY);
	}
	
	public void setButtonSize(){
		bPosX = (int) (ScreenWidth/2.0f - buttonSizeX/2.0f);
		b1PosY = (int) (ScreenHeight/2.0f + 0.5f*buttonSizeY);
		b2PosY = (int) (ScreenHeight/2.0f - 0.5f*buttonSizeY);
		b3PosY = (int) (ScreenHeight/2.0f - 1.5f*buttonSizeY);
		
//		System.out.println("BottomLeft buttons (x,y1,y2,y3): " + bPosX + " , " + b1PosY + " , " + b2PosY + " , " + b3PosY);
	}
	
	public void setDrawButtons(){
		drawPosX = bPosX;
		draw1PosY = b1PosY;
		draw2PosY = b2PosY;
		draw3PosY = b3PosY;
		drawbuttonSizeX = buttonSizeX;
		drawbuttonSizeY = buttonSizeY;
	}
	
	public void render (GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		// Set the clear color and clear the screen.
		gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// Draw the buttons.
		drawButtons(gl);
		gl.glFlush();
	

	}
	
	private void drawButtons(GL gl) {
		// Draw the background boxes
		boxOnScreen(gl, drawPosX, draw1PosY, "Start", 1);
		
		boxOnScreen(gl, drawPosX, draw2PosY, "Editor", 2);
		
		boxOnScreen(gl, drawPosX, draw3PosY, "Stop", 3);
		
	}
	
	private void boxOnScreen(GL gl, float x, float y, String text, int boxnum) {
		if(mouseOnBox == boxnum){
			gl.glColor3f(0, 1.0f, 0);
			gl.glLineWidth(5);
			gl.glBegin(GL.GL_LINE_LOOP);
			gl.glVertex2f(x, y);
			gl.glVertex2f(x + drawbuttonSizeX, y);
			gl.glVertex2f(x + drawbuttonSizeX, y + drawbuttonSizeY);
			gl.glVertex2f(x, y + drawbuttonSizeY);
			gl.glEnd();
		}
		
		GLUT glut = new GLUT();
		gl.glColor3f(1.0f,  1.0f, 1.0f);
		gl.glRasterPos2d(x + drawbuttonSizeX/5.0, y + drawbuttonSizeY/3.0);
		glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, text);

	}
	
	public boolean ButtonPressed(int buttonX, int buttonY, int xin, int yin){
		yin = ScreenHeight - yin;

//		return buttonX < xin && xin < buttonX+buttonSizeX && buttonY < yin && yin < buttonY+buttonSizeY;

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
		
		MainClass.state.setStopTitle(true);
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
		
//		System.out.println("HIER");
		
		if (ButtonPressed( (int) bPosX, (int) b1PosY, Xin, Yin)) {
			MainClass.canvas.removeGLEventListener(this);
			
			MainClass.state.GameStateUpdate(GameState.MAINGAME_STATE);
			MainClass.state.setStopTitle(true);
			MainClass.state.setStopMainGame(false);
		}
		else if (ButtonPressed( (int) bPosX, (int) b2PosY, Xin, Yin)) {
			new LevelEditor();
		}
		else if (ButtonPressed( (int) bPosX, (int) b3PosY, Xin, Yin)) {
			MainClass.state.GameStateUpdate(GameState.STOP_STATE);
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
			else if(ButtonPressed( (int) bPosX, (int) b3PosY, Xin, Yin))
				mouseOnBox = 3;
			else
				mouseOnBox = 0;
		
//			System.out.println(mouseOnBox);
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	
}
