import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

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
	int x=0;
	int y= 0;
	int dx=0;
	int dy=0;
	int sx=0;
	int sy=0;
	
	private CursorHandler c = new CursorHandler(MainClass.canvas);
	
	protected boolean lookback = false;
	
    private final Set<Character> pressed = new HashSet<Character>(); // Currently pressed keys
    
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
		dX= -dx;
		dY=-dy;
		dx=0;
		dy=0;
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
		if(event.getButton()==1 && MainClass.state.getState() == 1){
			
			attack = true;
			
		}

		
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{		
		// TODO: Detect mouse movement while the mouse button is down
		
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		
		pressed.add(event.getKeyChar());
        if (pressed.size() > 0) {
        	
        	if( 1 < pressed.size() && pressed.size() < 3 && Player.speedadjust == 1)
        		Player.speedadjust = 1/(Math.sqrt(2));
        	else
        		Player.speedadjust = 1;
        	
        	for (Character keyCode : pressed) {
        		
        		if(keyCode== 'w')
        		{
        			forward = true;
        		//	back = false;
        		//	left = false;
        		//	right = false;
        		}
        		else if(keyCode=='s')
        		{
        		//	forward = false;
        			back = true;
        		//	left = false;
        		//	right = false;
        		}
        		else if(keyCode=='d')
        		{
        			//forward = false;
        		//	back = false;
        			//left = false;
        			right = true;
        		}
        		else if(keyCode=='a')
        		{
        			//forward = false;
        			//back= false;
        			left = true;
        			//right = false;
        		}
        		else if(keyCode == KeyEvent.VK_SPACE){
        			jump = true;
        		}
        		
        		if(keyCode== KeyEvent.VK_ESCAPE && MainClass.state.getState() == 1){
        			MainClass.state.GameStateUpdate(GameState.PAUSE_STATE);
        			MainClass.state.setStopMainGame(true);
        			MainClass.state.setStopPause(false);
        		}
        		if(keyCode=='e' && MainClass.state.getState() == 1){
        			lookback = true;
        		}
        		
        		
        	}
 
        	
        }
		
		
		
	}
	
	@Override
	public void keyReleased(KeyEvent event)
	{
				
		pressed.remove(event.getKeyChar());
		
		
		if(event.getKeyCode()==KeyEvent.VK_W)
		{
			forward= false;
		}
		else if(event.getKeyCode()==KeyEvent.VK_S)
		{
			back=false;
		}
		else if(event.getKeyCode()==KeyEvent.VK_D)
		{
			right =false;
		}
		else if(event.getKeyCode()==KeyEvent.VK_A)
		{
			left=false;
		}
		if(event.getKeyCode()==KeyEvent.VK_E){
			lookback = false;
		}
		if(event.getKeyCode()==KeyEvent.VK_SPACE){
			jump=false;
		}
		
	}

	/*
	 * **********************************************
	 * *		Unused event handlers				*
	 * **********************************************
	 */
	
	@Override
	public void mouseMoved(MouseEvent event)
	{

		if(MainClass.state.getState() == 1){
			
			c.setCursor(2);
			
			int midX = Math.round(MainClass.screenWidth / 2);
			int midY = Math.round(MainClass.screenHeight / 2);
					
			dx = event.getX() - midX;
			dy = event.getY() - midY + 22;
			
			x = midX;
			y = midY;
			
			Robot r = null;
			try {
				r = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			r.mouseMove(midX, midY);
		
		}else{
			
				
			dx=event.getX() -x;
			dy=event.getY() -y;
			x=event.getX();
			y=event.getY();
			
		}
		
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
	}


}
