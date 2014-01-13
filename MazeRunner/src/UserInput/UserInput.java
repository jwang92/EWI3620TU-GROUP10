package UserInput;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Robot;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GLCanvas;

import GameObject.Player;
import Main.GameState;
import Main.MainClass;
import Maze.LevelExit;
import Utils.Inputbox;

//import com.sun.media.sound.Toolkit;

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
		implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener
{

	int x=0;
	int y= 0;
	int dx=0;
	int dy=0;
	int sx=0;
	int sy=0;
	
	private int defX, defY;
		
	public boolean lookback = false;
	public boolean rUpgrade = false;
	
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
		canvas.addMouseWheelListener(this);
	}
	
	public void setDefMouse(MouseEvent me){
		defX = me.getXOnScreen();
		defY = me.getYOnScreen();
	}
	
	/*
	 * **********************************************
	 * *				Updating					*
	 * **********************************************
	 */

	@Override
	public void update()
	{
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
		if(event.getButton()==1 && MainClass.state.getState() == 1 && !defense && !attack){
			
			attack = true;
			
		}
		else if(event.getButton()==3 && MainClass.state.getState() == 1 && !defense &&!attack){
			defense = true;
		}

		
	}

	@Override
	public void mouseDragged(MouseEvent event){		
		this.mouseMoved(event);
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
        		
        		if(keyCode== 'w'){
        			forward = true;
        		} else if(keyCode=='s'){
        			back = true;
        		} else if(keyCode=='d'){
        			right = true;
        		} else if(keyCode=='a'){
        			left = true;
        		} else if(keyCode == KeyEvent.VK_SPACE){
        			jump = true;
        		} else if(keyCode =='q'){
        			MainClass.mazeRunner.setRW(false);
        		} else if(keyCode =='r'){
        			if(rUpgrade){
        				MainClass.mazeRunner.setRW(true);
        			}
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
		
		for(Inputbox input : MainClass.login.inputs){
			if(input.getSelect()){
				input.enteredKey(event);
			}
		}
		
		pressed.remove(event.getKeyChar());
		
		if(event.getKeyCode()==KeyEvent.VK_W){
			forward= false;
		} else if(event.getKeyCode()==KeyEvent.VK_S){
			back=false;
		} else if(event.getKeyCode()==KeyEvent.VK_D){
			right =false;
		} else if(event.getKeyCode()==KeyEvent.VK_A){
			left=false;
		} if(event.getKeyCode()==KeyEvent.VK_E){
			lookback = false;
		}
		
		if(event.getKeyCode()==KeyEvent.VK_SPACE){
			jump=false;
		}
		
		if(event.getKeyCode()==KeyEvent.VK_ENTER){
			
			if(MainClass.mazeRunner.isText()){
				MainClass.mazeRunner.removeText();
			}
			
			LevelExit exit = MainClass.maze.isExit(MainClass.player.locationX, MainClass.player.locationY, MainClass.player.locationZ);
			if(exit != null){
				MainClass.initObjects(exit.getNewLoadFolder());
				MainClass.state.sMainGame = false;
			}
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

			int screenx = event.getXOnScreen();
			int screeny = event.getYOnScreen();
				
			dx = screenx - defX;
			dy = screeny - defY;
			
			x = defX;
			y = defY;
			
			Robot r = null;
			try {
				r = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			r.mouseMove(defX, defY);
		
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
		if(event.getButton()==3 && MainClass.state.getState() == 1 && defense){
			defense = false;
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent me) {
		
		if(rUpgrade){
			
			if(MainClass.mazeRunner.getRW()){
				MainClass.mazeRunner.setRW(false);
			} else {
				MainClass.mazeRunner.setRW(true);
			}
		}
		
	}
	
}
