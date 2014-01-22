package Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;

import GameObject.Bullet;
import GameObject.Enemy;
import GameObject.Player;
import GameObject.RangedWeapons;
import GameObject.Shield;
import GameObject.Sword;
import Maze.Door;
import Maze.Maze;
import Maze.MazePheromones;
import Model.Model;
import UserInput.CursorHandler;
import UserInput.UserInput;
import Utils.TextureLoader;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;

public class MainClass extends Frame implements GLEventListener, MouseListener {
	
	//main parameters
	private static final long serialVersionUID = 1L;
	public GLCanvas canvas;
	public int screenWidth = 600, screenHeight = 600;		// Screen size.
	public int tel;
	public String username = "0";
	
	//gamestates
	public GameStateManager state;
	public MainMenu mainMenu;
	public MazeRunner mazeRunner;
	public Pause pause;
	public GameOver gameover;
	public Login login;
	public Highscores highscores;
	public Finish finish;
	
	//Maze
	public Maze maze;
	public Player player;
	public MazePheromones mazePheromones;
	
	//Enemies
	public ArrayList<Model> enemieModels = new ArrayList<Model>();
	public ArrayList<String> enemieModelNames = new ArrayList<String>();
	public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	//Objects
	public Sword sword;
	public Shield shield;
	public RangedWeapons rWeapon;
	public ArrayList<Door> doors;
	
	//User
	public CursorHandler cursor;
	public Camera camera;
	public UserInput input;
	
	//Load the textures
	public ArrayList<Texture> textures;
	public ArrayList<String> textureNames;
	private int numberOfTextures;
	
	//Load textrenderers
	public ArrayList<TextRenderer> trenderers;
	
	
	public MainClass(int Width, int Height, boolean fullscreen){
		super("Medieval Invasion");
		
		// Let's change the window to our liking.
		setBackground(new Color(0.0f, 0.0f, 0.0f, 1));
		setResizable(false);
		if(fullscreen){
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			screenWidth = (int) screenSize.getWidth();
			screenHeight = (int) screenSize.getHeight();
			setSize( screenWidth, screenHeight);
			setUndecorated(true);
		}
		else{
			screenHeight = Height;
			screenWidth = Width;
			setSize( screenWidth, screenHeight);
			setLocationRelativeTo(null); 
		}
		
		//Textures
		textures = new ArrayList<Texture>();
		textureNames = new ArrayList<String>();
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
		
		login = new Login(this);
		highscores = new Highscores(this);
		mainMenu = new MainMenu(this);
		state = new GameStateManager(this);
		cursor = new CursorHandler(canvas);
		
		// Set the frame to visible. This automatically calls upon OpenGL to prevent a blank screen.
		initObjects();
		setVisible(true);
		
		
	}
	
	public void display(GLAutoDrawable drawable) {
		render(drawable);
	}
		
	public void render (GLAutoDrawable drawable){
		
		GL gl = drawable.getGL();

//		System.out.println("render: "+screenWidth +"x"+ screenHeight);
		
		// Set the clear color and clear the screen.
		gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
//	    gl.glFlush();
		
		if(tel != state.getState()){
			if(state.getState() == 1)
				cursor.setCursor(2);
			else
				cursor.setCursor(-1);
		}
		
		tel = state.getState();
		
		switch(tel){
		case 0:
			mainMenu.render(drawable);
			break;
		case 1:
			mazeRunner.render(drawable);
			break;
		case 2:
			pause.render(drawable);
			break;
		case 3:
			System.exit(0);
			break;
		case 4:
			gameover.render(drawable);
			break;
		case 5:
			login.render(drawable);
			break;
		case 6:
			highscores.render(drawable);
			break;
		case 7:
			finish.render(drawable);
			break;
		}
		
		initUpdater(drawable, 0, 0, screenWidth, screenHeight);

	}
	
/*
 * **********************************************
 * *		OpenGL event handlers				*
 * **********************************************
 */

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

		loadTRenderers();
				
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
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		screenWidth = arg3;
		screenHeight = arg4;
	}

/*
 * **********************************************
 * *		Mouse event handlers				*
 * **********************************************
 */
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Not relevant
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Not relevant
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Not relevant
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// Not relevant
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		switch(tel){
		case 0:
			mainMenu.mouseReleased(me);
			break;
		case 2:
			pause.mouseReleased(me);
			break;
		case 4:
			gameover.mouseReleased(me);
			break;
		case 5:
			login.mouseReleased(me);
			break;
		case 6:
			highscores.mouseReleased(me);
			break;
		case 7:
			finish.mouseReleased(me);
			break;
		}

	}
	
	public void initUpdater(GLAutoDrawable drawable, int x, int y, int screenWidth, int screenHeight){
		int tel=state.getState();
		if(tel==0 && !state.getStopTitle() ){
			mainMenu.init(drawable);
		}
		else if(tel==1 && !state.getStopMainGame() ){
			mazeRunner.mazeInit(drawable,0,0,screenWidth, screenHeight);
		}
		else if(tel==2 && !state.getStopPause() ){
			pause.init(drawable);
		}
		else if(tel==4 && !state.getStopGameOver() ){
			gameover.init(drawable);
		}
		else if(tel==5 && !state.getStopLogin() ){
			login.init(drawable);
		}
		else if(tel==6 && !state.getStopHighscores() ){
			highscores.init(drawable);
		}
		else if(tel==7 && !state.getStopFinish() ){
			highscores.init(drawable);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadTextures(){
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap> tempTextureHashMapArray = TextureLoader.loadTextureArray("textures/");
		numberOfTextures = tempTextureHashMapArray.size();
		textureNames = new ArrayList<String>(numberOfTextures);
		textures = new ArrayList<Texture>(numberOfTextures);
		for(int i = 0; i<numberOfTextures;i++){
			HashMap<String, Texture> tempTextureHashMap = tempTextureHashMapArray.get(i);
			String tempTextureName = tempTextureHashMap.entrySet().iterator().next().getKey();
			Texture tempTexture = tempTextureHashMap.entrySet().iterator().next().getValue();
			textureNames.add(tempTextureName);
			textures.add(tempTexture);		
		}

	}
	
	public void loadTRenderers(){
		trenderers = new ArrayList<TextRenderer>();
		Font f2 = null;
		
		// load font geosanslight
		try {
			f2 = Font.createFont(Font.TRUETYPE_FONT, new File("GeosansLight.ttf"));
		} catch (Exception e){
			System.out.println("Fout: GeosansLight.ttf niet gevonden");
		}
		
		// geosanslight - 20f		main.trenders.get(0)
		float fontSize = (float) Math.floor(screenHeight * 20.0f/600.0f);
		Font f = f2.deriveFont(fontSize);
		trenderers.add( new TextRenderer(f) );
		
		// geosanslight - 30f		main.trenders.get(1)
		fontSize = (float) Math.floor(screenHeight * 30.0f/600.0f);
		f = f2.deriveFont(fontSize);
		trenderers.add( new TextRenderer(f) );
		
		//load font fontje
		try {
			f2 = Font.createFont(Font.TRUETYPE_FONT, new File("fontje.ttf"));
		} catch (Exception e){
			System.out.println("Fout: fontje.ttf niet gevonden");
		}
		
		// fontje - 20f				main.trenders.get(2)
		fontSize = (float) Math.floor(screenHeight * 20.0f/600.0f);
		f = f2.deriveFont(fontSize);
		trenderers.add( new TextRenderer(f) );

		// fontje - 30f				main.trenders.get(3)
		fontSize = (float) Math.floor(screenHeight * 30.0f/600.0f);
		f = f2.deriveFont(fontSize);
		trenderers.add( new TextRenderer(f) );

	}
	
	/**
	 * Zet alle condities (objects en enkele states) op beginwaarden
	 */
	public void initObjects(){

		maze = new Maze(this);
		
		mazePheromones = new MazePheromones(this);
		
		player = new Player( maze.getLevelInfo().getPlayerPos().x * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, 	// x-position
							maze.getLevelInfo().getPlayerPos().z * 5 - 2.5,							// y-position
							maze.getLevelInfo().getPlayerPos().y * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, 	// z-position
							 90, 0, this );										// horizontal and vertical angle
		sword = new Sword( 6 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2 -1, 	
				 maze.SQUARE_SIZE / 2 -1,							
				 5 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2,true, 0, this);

		rWeapon = new RangedWeapons( 6 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2 -1, 	
			 maze.SQUARE_SIZE / 2 -1,							
			 5 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2,true, 0, this);

		shield = new Shield( 6 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2 -1, 	
				 maze.SQUARE_SIZE / 2 -1,							
				 5 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2,true, false, this);
		
		doors = maze.loadDoors();

		enemies.removeAll(enemies);
		enemies = maze.loadEnemies();
		
		camera = new Camera( player.getLocationX(), player.getLocationY(), player.getLocationZ(), 
				             player.getHorAngle(), player.getVerAngle() ,this);
		
		input = new UserInput(canvas,this);
		
		mazeRunner = new MazeRunner(screenHeight, screenWidth, this);
		pause = new Pause(this);
		gameover = new GameOver(this);
		finish = new Finish(this);


	}

	/**
	 * Zelfde als initObjects(), maar laad een nieuwe map/level in
	 * @param newloadfolder naam van de nieuwe level
	 */
	public void initObjects(String newloadfolder){

		maze = new Maze(newloadfolder,this);
		
		mazePheromones = new MazePheromones(this);
		
		player = new Player( maze.getLevelInfo().getPlayerPos().x * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, 	// x-position
							maze.getLevelInfo().getPlayerPos().z * 5 - 2.5,							// y-position
							maze.getLevelInfo().getPlayerPos().y * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, 	// z-position
							 90, 0 , this );										// horizontal and vertical angle
		sword = new Sword( 6 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2 -1, 	
							 maze.SQUARE_SIZE / 2 -1,							
							 5 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2,true, 0, this);
		
		rWeapon = new RangedWeapons( 6 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2 -1, 	
				 maze.SQUARE_SIZE / 2 -1,							
				 5 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2,true, 0 , this);
		
		shield = new Shield( 6 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2 -1, 	
				 maze.SQUARE_SIZE / 2 -1,							
				 5 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2,true, false , this);
		
		doors = maze.loadDoors();

		enemies.removeAll(enemies);
		enemies = maze.loadEnemies();
		
		
		camera = new Camera( player.getLocationX(), player.getLocationY(), player.getLocationZ(), 
				             player.getHorAngle(), player.getVerAngle() , this);
		
		
		mazeRunner = new MazeRunner(screenHeight, screenWidth, this);
		pause = new Pause(this);
		gameover = new GameOver(this);
		finish = new Finish(this);

	}

	public void setScreenSize(int ScreenHeight, int ScreenWidth){
		screenHeight = ScreenHeight;
		screenWidth = ScreenWidth;

	}

}
