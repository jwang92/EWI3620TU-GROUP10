import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;


public class MainClass extends Frame implements GLEventListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	public static GLCanvas canvas;
	public static int screenWidth = 600, screenHeight = 600;		// Screen size.

	
	public static MazeRunner mazeRunner;
	public static GameStateManager state;
	public static MainMenu mainMenu;
	public static Maze maze;
	public static Player player;
	public static Camera camera;
	public static UserInput input;
	public static Pause pause;
	
	//Load the textures
	protected static ArrayList<Texture> textures;
	protected static ArrayList<String> textureNames;
	private Texture tempTexture;
	private String textureFileName = "";
	private String textureFileType = "png";
	private float textureTop, textureBottom, textureLeft, textureRight;
	
	public MainClass(){
		super("Medieval Invasion");
		
		// Let's change the window to our liking.
		setSize( screenWidth, screenHeight);
		setBackground(new Color(0.0f, 0.0f, 0.0f, 1));
		
		
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
		
		// Set the frame to visible. This automatically calls upon OpenGL to prevent a blank screen.
		setVisible(true);
		initObjects();
		
		//Textures
		textures = new ArrayList<Texture>();
		textureNames = new ArrayList<String>();
	}
	
	public void display(GLAutoDrawable drawable) {
		render(drawable);
	}
	
	public void render (GLAutoDrawable drawable){
		GL gl = drawable.getGL();

		// Set the clear color and clear the screen.
		gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		int tel = state.getState();
		if(tel==0){
			mainMenu.render(drawable);
		}
		else if (tel==1){
			mazeRunner.render(drawable);
			initUpdater(drawable,0,0, screenWidth, screenHeight);
		}
		else if (tel==2){
			pause.render(drawable);
			pause.init(drawable);
		}
		else if (tel==3){
			System.exit(0);
		}

	}
	
/*
 * **********************************************
 * *		OpenGL event handlers				*
 * **********************************************
 */

	//@Override
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
		
		loadTextures();
		
		// First, we set up JOGL. We start with the default settings.
		GLCapabilities caps = new GLCapabilities();
		// Then we make sure that JOGL is hardware accelerated and uses double buffering.
		caps.setDoubleBuffered( true );
		caps.setHardwareAccelerated( true );
		
		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		Animator anim = new Animator( canvas );
		anim.start();
		
				
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		int tel = state.getState();
		if(tel == 1){
			mazeRunner.reshape(arg0, arg1, arg2, arg3, arg4);
		}
		
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
		int tel = state.getState();
		if(tel==0){
			mainMenu.mouseReleased(me);
		}
		else if(tel==2){
			pause.mouseReleased(me);
		}
	}
	
	public void initUpdater(GLAutoDrawable drawable, int x, int y, int screenWidth, int screenHeight){
		int tel=state.getState();
		if(tel==1){
			mazeRunner.mazeInit(drawable,0,0,screenWidth, screenHeight);
		}
		else if(tel==2){
			pause.init(drawable);
		}
	}
	
	public void loadTextures(){
		try {
		    File folder = new File("textures/");
		    File[] tList = folder.listFiles();
		    textureNames = new ArrayList<String>(tList.length-1);
		    
		    int i = 0;
		    for (File file : tList)
		    {
	            if(!file.getName().equals("Thumbs.db"))
	            {
	            	//Get the name of the texture
	            	textureFileName = "textures/" + file.getName();
	            	
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
	            	System.out.println(textureNames.toString());
	            	//Load the texture coordinates
	    			TextureCoords textureCoords = tempTexture.getImageTexCoords();
	    			textureTop = textureCoords.top();
	    			textureBottom = textureCoords.bottom();
	    			textureLeft = textureCoords.left();
	    			textureRight = textureCoords.right();
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
	
	public static void initObjects(){
		mainMenu = new MainMenu(screenHeight, screenWidth);
		state = new GameStateManager();
		maze = new Maze();
		player = new Player( 6 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, 	// x-position
							 maze.SQUARE_SIZE / 2,							// y-position
							 5 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, 	// z-position
							 90, 0 );										// horizontal and vertical angle

		camera = new Camera( player.getLocationX(), player.getLocationY(), player.getLocationZ(), 
				             player.getHorAngle(), player.getVerAngle() );
		
		input = new UserInput(canvas);
		
		mazeRunner = new MazeRunner(screenHeight, screenWidth);
		pause = new Pause(screenHeight, screenWidth);
	}

}
