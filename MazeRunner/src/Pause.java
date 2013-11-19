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

public class Pause implements GLEventListener, MouseListener /*, MouseMotionListener*/ {
	/*
	 * **********************************************
	 * *			Local Variables					*
	 * **********************************************
	 */
	
	//frame setup
	public int ScreenWidth, ScreenHeight;
	//buttons setup
	private int buttonSizeX = 250, buttonSizeY = 100;
	private int bPosX;
	private int b1PosY, b2PosY, b3PosY;
	private int mouseOnBox = 0;
	boolean stop = false;

	
	public Pause(int screenHeight, int screenWidth){
		ScreenWidth= screenWidth;
		ScreenHeight = screenHeight;
		
		setButtonSize();
	
	}
	
	public void setButtonSize(){
		bPosX = (int) (ScreenWidth/2.0f-buttonSizeX/2.0f);
		b1PosY = (int) (ScreenHeight-2.0f*buttonSizeY);
		b2PosY = (int) (ScreenHeight-3.0f*buttonSizeY);
		b3PosY = (int) (ScreenHeight-4.0f*buttonSizeY);
	}
	
	public void render (GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		// Set the clear color and clear the screen.
		gl.glClearColor(0.0f, 0.0f, 1.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// Draw the buttons.
		drawButtons(gl);
		gl.glFlush();
	
//		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
//		 * The Animator class handles that for JOGL.
//		 */
//		Animator anim = new Animator( MainClass.canvas );
//		anim.start();
	}
	
	private GL BoxColor(GL gl, int boxnum){
		if(mouseOnBox == boxnum)
			gl.glColor3f(0, 1.0f, 0);
		else
			gl.glColor3f(1.0f, 0, 0);
		return gl;
	}
	
	private void drawButtons(GL gl) {
	
		// Draw the background boxes
		gl = BoxColor(gl, 1);
		boxOnScreen(gl, bPosX, b1PosY, "Start");
		
		//gl = BoxColor(gl, 2);
		//boxOnScreen(gl, bPosX, b2PosY, "Editor");
		
		gl = BoxColor(gl, 3);
		boxOnScreen(gl, bPosX, b3PosY, "Stop");
		
	}
	
	private void boxOnScreen(GL gl, float x, float y, String text) {
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + buttonSizeX, y);
		gl.glVertex2f(x + buttonSizeX, y + buttonSizeY);
		gl.glVertex2f(x, y + buttonSizeY);
		gl.glEnd();
		
		GLUT glut = new GLUT();
		gl.glColor3f(1.0f,  1.0f, 1.0f);
		gl.glRasterPos2d(x + buttonSizeX/5.0, y + buttonSizeY/3.0);
		glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, text);

	}
	
	public boolean ButtonPressed(int buttonX, int buttonY, int xin, int yin){
		yin = ScreenHeight - yin;

		return buttonX < xin && xin < buttonX+buttonSizeX && buttonY < yin && yin < buttonY+buttonSizeY;
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
		if(MainClass.state.getStopPause()==false){
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
		MainClass.state.setStopMainGame(true);
		
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
		int Xin = me.getX();
		int Yin = me.getY();
		
		if (ButtonPressed( (int) bPosX, (int) b1PosY, Xin, Yin)) {
				MainClass.state.GameStateUpdate(GameState.MAINGAME_STATE);
				MainClass.state.setStopPause(true);
				MainClass.state.setStopMainGame(false);
		} 
	}

//	/*
//	 * **********************************************
//	 * *		Mouse Motion event handlers			*
//	 * **********************************************
//	 */
//		
//		@Override
//		public void mouseMoved(MouseEvent me){
//			int Xin = me.getXOnScreen();
//			int Yin = me.getYOnScreen();
////			System.out.println(Xin + " " + Yin);
//			
//			if (ButtonPressed( (int) bPosX, (int) b1PosY, Xin, Yin))
//				mouseOnBox = 1;
//			else if(ButtonPressed( (int) bPosX, (int) b2PosY, Xin, Yin))
//				mouseOnBox = 2;
//			else if(ButtonPressed( (int) bPosX, (int) b3PosY, Xin, Yin))
//				mouseOnBox = 3;
//			else
//				mouseOnBox = 0;
//		
////			System.out.println(mouseOnBox);
//		}
//
//		@Override
//		public void mouseDragged(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//		
	
}
