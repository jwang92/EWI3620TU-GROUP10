import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;

import com.sun.opengl.util.Animator;


public class MainMenu extends Frame implements GLEventListener, MouseListener{
	private GLCanvas canvas;
	private int screenWidth = 600, screenHeight = 600;		// Screen size.

	public MainMenu(){
		super("Main Menu");
		
		// Let's change the window to our liking.
		setSize( screenWidth, screenHeight);
		setBackground( Color.blue );

		// The window also has to close when we want to.
		this.addWindowListener( new WindowAdapter()
		{
			public void windowClosing( WindowEvent e )
			{
				System.exit(0);
			}
		});
		
//		initJOGL();							// Initialize JOGL.

//		initObjects();						// Initialize all the objects!
		
		// Set the frame to visible. This automatically calls upon OpenGL to prevent a blank screen.
		setVisible(true);
	}
	
//	private void initJOGL()	{
//		// First, we set up JOGL. We start with the default settings.
//		GLCapabilities caps = new GLCapabilities();
//		// Then we make sure that JOGL is hardware accelerated and uses double buffering.
//		caps.setDoubleBuffered( true );
//		caps.setHardwareAccelerated( true );
//
//		// Now we add the canvas, where OpenGL will actually draw for us. We'll use settings we've just defined. 
//		canvas = new GLCanvas( caps );
//		add( canvas );
//		/* We need to add a GLEventListener to interpret OpenGL events for us. Since MazeRunner implements
//		 * GLEventListener, this means that we add the necesary init(), display(), displayChanged() and reshape()
//		 * methods to this class.
//		 * These will be called when we are ready to perform the OpenGL phases of MazeRunner. 
//		 */
//		canvas.addGLEventListener( this );
//
//		// Also add this class as mouse listener, allowing this class to react
//		// to mouse events that happen inside the GLCanvas.
//		canvas.addMouseListener(this);
//
//		// An Animator is a JOGL help class that can be used to make sure our
//		// GLCanvas is continuously being re-rendered. The animator is run on a
//		// separate thread from the main thread.
//		Animator anim = new Animator(canvas);
//		anim.start();
//	}
	
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
		// TODO Auto-generated method stub
		
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
