import java.awt.event.*;

import javax.media.opengl.GLCanvas;

/**
 * The UserInput class is an extension of the Control class. It also implements three 
 * interfaces, each providing handler methods for the different kinds of user input.
 * <p>
 * For making the assignment, only some of these handler methods are needed for the 
 * desired functionality. The rest can effectively be left empty (i.e. the methods 
 * under 'Unused event handlers').  
 * <p>
 * Note: because of how java is designed, it is not possible for the game window to
 * react to user input if it does not have focus. The user must first click the window 
 * (or alt-tab or something) before further events, such as keyboard presses, will 
 * function.
 * 
 * @author Mattijs Driel
 *
 */
public class UserInput extends Control 
		implements MouseListener, MouseMotionListener, KeyListener
{
	// TODO: Add fields to help calculate mouse movement
	private double x0, y0, x, y;
	
	/**
	 * UserInput constructor.
	 * <p>
	 * To make the new UserInput instance able to receive input, listeners 
	 * need to be added to a GLCanvas.
	 * 
	 * @param canvas The GLCanvas to which to add the listeners.
	 */
	public UserInput(GLCanvas canvas)
	{
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
	}
	
	/*
	 * **********************************************
	 * *				Updating					*
	 * **********************************************
	 */

	@Override
	public void update()
	{
		// TODO: Set dX and dY to values corresponding to mouse movement
		dX = (int) (x0 - x);
		dY = (int) (y0 - y);
	}

	/*
	 * **********************************************
	 * *		Input event handlers				*
	 * **********************************************
	 */

	@Override
	public void mousePressed(MouseEvent event)
	{
		// TODO: Detect the location where the mouse has been pressed
		x0 = event.getX();
		y0 = event.getY();
		// Prevent player viewing angle adjustment at t=0
		x = x0;
		y = y0;
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{		
		// TODO: Detect mouse movement while the mouse button is down
		x = event.getX();
		y = event.getY();
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		// TODO: Set forward, back, left and right to corresponding key presses
		switch (event.getKeyChar()) {
			case 'w': forward = true; 	break;
			case 'a': left = true; 		break;
			case 's': back = true; 		break;
			case 'd': right = true; 	break;		
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		// TODO: Set forward, back, left and right to corresponding key presses
		switch (event.getKeyChar()) {
			case 'w': forward = false; 	break;
			case 'a': left = false; 	break;
			case 's': back = false; 	break;
			case 'd': right = false; 	break;		
		}
//		if(event.getKeyChar() == 'w')
//			forward = false;
//		if(event.getKeyChar() == 'a')
//			left = false;
//		if(event.getKeyChar() == 's')
//			back = false;
//		if(event.getKeyChar() == 'd')
//			right = false;
	}

	/*
	 * **********************************************
	 * *		Unused event handlers				*
	 * **********************************************
	 */
	
	@Override
	public void mouseMoved(MouseEvent event)
	{
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		x0 = x;
		y0 = y;
	}


}
