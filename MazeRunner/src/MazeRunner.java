import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import com.sun.opengl.util.*;
import com.sun.opengl.util.j2d.TextRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/** 
 * MazeRunner is the base class of the game, functioning as the view controller and game logic manager.
 * <p>
 * Functioning as the window containing everything, it initializes both JOGL, the 
 * game objects and the game logic needed for MazeRunner.
 * <p>
 * For more information on JOGL, visit <a href="http://jogamp.org/wiki/index.php/Main_Page">this page</a>
 * for general information, and <a href="https://jogamp.org/deployment/jogamp-next/javadoc/jogl/javadoc/">this page</a>
 * for the specification of the API.
 * 
 * @author Bruno Scheele, revised by Mattijs Driel
 * 
 */
public class MazeRunner implements GLEventListener{
	static final long serialVersionUID = 7526471155622776147L;

	/*
 * **********************************************
 * *			Local variables					*
 * **********************************************
 */
	//private GLCanvas canvas;

	public int screenWidth, screenHeight;					// Screen size.
	boolean stop =false;
	
	private ArrayList<VisibleObject> visibleObjects;		// A list of objects that will be displayed on screen.
//	private Player player;									// The player object.
//	private Camera camera;									// The camera object.
//	private UserInput input;								// The user input object that controls the player.
//	private Maze maze; 										// The maze.
	private long previousTime = Calendar.getInstance().getTimeInMillis(); // Used to calculate elapsed time.

	//Shaders
	private int shaderProgram;
	
	// Fonts
	private TextRenderer tr;
	private Font f = new Font("SansSerif", Font.PLAIN, 20);
	private int previousHealth;
	private boolean swordLoaded = true;
	private boolean rw = false;
	
/*
 * **********************************************
 * *		Initialization methods				*
 * **********************************************
 */
	/**
	 * Initializes the complete MazeRunner game.
	 * <p>
	 * MazeRunner extends Java AWT Frame, to function as the window. It creats a canvas on 
	 * itself where JOGL will be able to paint the OpenGL graphics. It then initializes all 
	 * game components and initializes JOGL, giving it the proper settings to accurately 
	 * display MazeRunner. Finally, it adds itself as the OpenGL event listener, to be able 
	 * to function as the view controller.
	 */
	public MazeRunner(int screenHeight, int screenWidth) {
		this.screenHeight = screenHeight;
		this.screenWidth =screenWidth; 
		initJOGL();							// Initialize JOGL.
		initObjects();						// Initialize all the objects!
	}
	
	/**
	 * initJOGL() sets up JOGL to work properly.
	 * <p>
	 * It sets the capabilities we want for MazeRunner, and uses these to create the GLCanvas upon which 
	 * MazeRunner will actually display our screen. To indicate to OpenGL that is has to enter a 
	 * continuous loop, it uses an Animator, which is part of the JOGL api.
	 */
	private void initJOGL()	{
		// First, we set up JOGL. We start with the default settings.
		GLCapabilities caps = new GLCapabilities();
		// Then we make sure that JOGL is hardware accelerated and uses double buffering.
		caps.setDoubleBuffered( true );
		caps.setHardwareAccelerated( true );
		
		/* We need to create an internal thread that instructs OpenGL to continuously repaint itself.
		 * The Animator class handles that for JOGL.
		 */
		Animator anim = new Animator( MainClass.canvas );
		anim.start();
	}
	
	/**
	 * initializeObjects() creates all the objects needed for the game to start normally.
	 * <p>
	 * This includes the following:
	 * <ul>
	 * <li> the default Maze
	 * <li> the Player
	 * <li> the Camera
	 * <li> the User input
	 * </ul>
	 * <p>
	 * Remember that every object that should be visible on the screen, should be added to the
	 * visualObjects list of MazeRunner through the add method, so it will be displayed 
	 * automagically. 
	 */
	private void initObjects()	{
		// We define an ArrayList of VisibleObjects to store all the objects that need to be
		// displayed by MazeRunner.
		visibleObjects = new ArrayList<VisibleObject>();
		// Add the maze that we will be using.
		//maze2 = new Maze();
		visibleObjects.add(MainClass.maze);

		// Initialize the player.
//		//player = new Player( 6 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, 	// x-position
//							 maze.SQUARE_SIZE / 2,							// y-position
//							 5 * maze.SQUARE_SIZE + maze.SQUARE_SIZE / 2, 	// z-position
//							 90, 0 );										// horizontal and vertical angle
//
//		//camera = new Camera( player.getLocationX(), player.getLocationY(), player.getLocationZ(), 
//				             player.getHorAngle(), player.getVerAngle() );
//		
//		//input = new UserInput(canvas);
		MainClass.player.setControl(MainClass.input);
		MainClass.player.getMaze(MainClass.maze);
		
		
		for(Enemy e: MainClass.enemies){
			e.getMaze(MainClass.maze);
		}
		if(!rw){
			MainClass.sword.setMaze(MainClass.maze);
			MainClass.sword.setPlayer(MainClass.player);
			MainClass.shield.setMaze(MainClass.maze);
			MainClass.shield.setPlayer(MainClass.player);
		}
		else{
			MainClass.rWeapon.setPlayer(MainClass.player);
		}
		
		MainClass.door.setPlayer(MainClass.player);
		MainClass.doorSwitch.setPlayer(MainClass.player);
	}

/*
 * **********************************************
 * *		OpenGL event handlers				*
 * **********************************************
 */

	/**
	 * init(GLAutodrawable) is called to initialize the OpenGL context, giving it the proper parameters for viewing.
	 * <p>
	 * Implemented through GLEventListener. 
	 * It sets up most of the OpenGL settings for the viewing, as well as the general lighting.
	 * <p> 
	 * It is <b>very important</b> to realize that there should be no drawing at all in this method.
	 */
	public void init(GLAutoDrawable drawable) {
		drawable.setGL( new DebugGL(drawable.getGL() )); // We set the OpenGL pipeline to Debugging mode.
        GL gl = drawable.getGL();
        GLU glu = new GLU();
        
        //Load the shaders
        setUpShaders("shaders/phong_lighting.vs","shaders/phong_lighting.fs",gl);
        
        gl.glClearColor(0, 0, 0, 0);								// Set the background color.
        
        // Now we set up our viewpoint.
        gl.glMatrixMode( GL.GL_PROJECTION );						// We'll use orthogonal projection.
        gl.glLoadIdentity();										// Reset the current matrix.
        glu.gluPerspective( 60, screenWidth, screenHeight, 200);	// Set up the parameters for perspective viewing.
        gl.glMatrixMode( GL.GL_MODELVIEW );
        
        // Enable back-face culling.
        gl.glCullFace( GL.GL_BACK );
        gl.glEnable( GL.GL_CULL_FACE );
        
        // Enable Z-buffering.
        gl.glEnable( GL.GL_DEPTH_TEST );
        
        // Set and enable the lighting.
        //LIGHT0: ambient light high in the sky
        float lightPosition0[] = { 0.0f, 50.0f, 0.0f, 1.0f }; 			// High up in the sky!
        float lightColour0[] = { 1.0f, 1.0f, 1.0f, 1.0f };				// White light!
        gl.glLightfv( GL.GL_LIGHT0, GL.GL_POSITION, lightPosition0, 0 );	// Note that we're setting Light0.
        gl.glLightfv( GL.GL_LIGHT0, GL.GL_AMBIENT, lightColour0, 0);
        gl.glLightfv( GL.GL_LIGHT0, GL.GL_DIFFUSE, lightColour0, 0);
        //gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, new float[] {0.0f, 0.0f, 0.0f, 1f}, 0);
        
        //LIGHT1: Shader light in the room
        float lightPosition1[] = { 0.0f, 0.2f, 0.0f, 1.0f}; 			// High up in the sky!
        float lightColour1[] = { 1.0f, 1.0f, 1.0f, 1.0f };				// White light!
        gl.glLightfv( GL.GL_LIGHT1, GL.GL_POSITION, lightPosition1, 0 );	// Note that we're setting Light1.
        gl.glLightfv( GL.GL_LIGHT1, GL.GL_DIFFUSE, lightColour1, 0);
        
        gl.glEnable(GL.GL_LIGHTING );
        gl.glEnable( GL.GL_LIGHT0 );
        gl.glEnable(GL.GL_LIGHT1);
        
        // Set the shading model.
        gl.glShadeModel( GL.GL_SMOOTH );
        ArrayList<String> loadedModels = new ArrayList<String>();
        ArrayList<Enemy> tempEnemies = new ArrayList<Enemy>();
        for(Enemy e: MainClass.enemies){
        	if(!loadedModels.contains(e.getType())){
            	e.genVBO(gl);
            	loadedModels.add(e.getType());
            	tempEnemies.add(e);
        	}
        	else{
        		int modelID = loadedModels.indexOf(e.getType());
        		IntBuffer vbo = tempEnemies.get(modelID).getVBOHandle();
        		e.setVBOHandle(vbo);
        	}

        }
        MainClass.sword.genVBO(gl);
        MainClass.rWeapon.genVBO(gl);
        MainClass.shield.genVBO(gl);
        
        //Load the maze
        MainClass.maze.genDisplayList(gl);
        
        //MainClass.enemy.setShaderProgram(shaderProgram);
        
        // Fonts setten
        
        try {
			Font f2 = Font.createFont(Font.TRUETYPE_FONT, new File("fontje.ttf"));
			f = f2.deriveFont(14f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		previousHealth = MainClass.player.getHealth();
	}
	
	/**
	 * display(GLAutoDrawable) is called upon whenever OpenGL is ready to draw a new frame and handles all of the drawing.
	 * <p>
	 * Implemented through GLEventListener. 
	 * In order to draw everything needed, it iterates through MazeRunners' list of visibleObjects. 
	 * For each visibleObject, this method calls the object's display(GL) function, which specifies 
	 * how that object should be drawn. The object is passed a reference of the GL context, so it 
	 * knows where to draw.
	 */
	public void display(GLAutoDrawable drawable) {
		render(drawable);
	}
	
public void draw2D(GL gl){
		
		gl.glMatrixMode( GL.GL_PROJECTION );
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0, screenWidth, screenHeight, 0, -1, 1);
		gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glPushMatrix();
		gl.glLoadIdentity();
		
		gl.glDisable(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_LIGHT0);
		gl.glDisable(GL.GL_LIGHT1);
		gl.glDisable( GL.GL_DEPTH_TEST );
		
		tr = new TextRenderer(f);
		drawHit(gl);
		drawHealthbar(gl, tr);
		drawUpgrades(gl, tr);
		drawScore(gl, tr);
		drawLevelExit(gl, tr);
	
		
		gl.glEnable( GL.GL_DEPTH_TEST );
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_LIGHT1);	
		
	
		
		gl.glMatrixMode( GL.GL_PROJECTION );
		gl.glPopMatrix();
		gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glPopMatrix();
		
	}

public void drawHit(GL gl){
	gl.glEnable(GL.GL_BLEND);
	gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE);
	
	gl.glBegin(GL.GL_QUADS);
	
	if(previousHealth > MainClass.player.getHealth()){
		gl.glColor4f(1f, 0.0f, 0.0f, 0.7f);
	}else{
		gl.glColor4f(1f, 0.0f, 0.0f, 0.0f);
	}

		gl.glVertex2f(0f, 0f);
		gl.glVertex2f(0f, screenHeight);
		gl.glVertex2f(screenWidth, screenHeight);
		gl.glVertex2f(screenWidth, 0);
	gl.glEnd();
	
	gl.glDisable(GL.GL_BLEND);
	
	
}

public void drawUpgrades(GL gl, TextRenderer t){
	
	ArrayList<int[]> ups = MainClass.player.getUpgrades();
	
	t.beginRendering(screenWidth, screenHeight);
	
	boolean swordText = false;
	int thingsDrawn = 0;
	
	for(int i = 0; i < ups.size(); i++){
		
		switch(ups.get(i)[0]){
		case 1:
			t.draw("Speed: " + ups.get(i)[1] + "ms", screenWidth - 220, screenHeight - 20 - (thingsDrawn++ * 20));
			break;
		case 2:
			swordText = true;
			switch(ups.get(i)[2]){
			case 2:
				t.draw("Superzwaard", screenWidth - 220, screenHeight - 20 - (thingsDrawn++ * 20));
				break;
			case 3:
				t.draw("Leeuwenzwaard :o", screenWidth - 220, screenHeight - 20 - (thingsDrawn++ * 20));
			}
			
			break;
		}

	}
	
	if(!swordText)
		t.draw("Kutzwaard", screenWidth - 220, screenHeight - 20 - (thingsDrawn++ * 20));	
	
	t.endRendering();
	
	
	for(int i = 0; i < ups.size(); i++){
		
		if((ups.get(i)[0] == 2 || ups.get(i)[0] == 4) && swordLoaded == false){
			
			if(!rw){
				//MainClass.sword.genVBO(gl);
			}
			else{
				//MainClass.rWeapon.genVBO(gl);
			}
			swordLoaded = true;

		}
		
	}
	
}

public void drawScore(GL gl, TextRenderer t){
	int score = MainClass.player.getScore();
	t.beginRendering(screenWidth, screenHeight);
	t.draw(Integer.toString(score), screenWidth - 100, 10);
	t.endRendering();
}

public void drawLevelExit(GL gl, TextRenderer t){
	if(MainClass.maze.isExit(MainClass.player.locationX, MainClass.player.locationY, MainClass.player.locationZ) != null){
		f = f.deriveFont(36f);
		
		t = new TextRenderer(f);

		t.beginRendering(screenWidth, screenHeight);
		t.draw("press ENTER to enter next level", screenWidth/6, screenHeight-100);
		t.endRendering();
		
		f = f.deriveFont(14f);
		
	}
}

public void drawHealthbar(GL gl, TextRenderer t){
	
	// Box met bar tekenen
	
	gl.glBegin(GL.GL_QUADS);
		gl.glColor3f(0.6f, 0.6f, 0.6f);
		gl.glVertex2f(20.0f, 20.0f);
		gl.glVertex2f(248.0f, 20.0f);
		gl.glVertex2f(248.0f, 40.0f);
		gl.glVertex2f(20.0f, 40.0f);
	gl.glEnd();
	
	float blockWidth = 9;
	
	for(int i = 0; i < Math.floor(MainClass.player.getHealth() / 5); i++){
		
		gl.glBegin(GL.GL_QUADS);
			gl.glColor3f(0f, 1f, 0f);
			gl.glVertex2f(25f + (i * blockWidth) + (i * 2), 25f);
			gl.glVertex2f(25.0f + (i * blockWidth) + blockWidth + (i * 2), 25f);
			gl.glVertex2f(25.0f + (i * blockWidth) + blockWidth + (i * 2), 35.0f);
			gl.glVertex2f(25.0f + (i * blockWidth) + (i * 2), 35.0f);
		gl.glEnd();
		
		
	}
	
	// Text erbij
	gl.glBegin(GL.GL_QUADS);
		gl.glColor3f(0.6f, 0.6f, 0.6f);
		gl.glVertex2f(194.0f, 40.0f);
		gl.glVertex2f(248.0f, 40.0f);
		gl.glVertex2f(248.0f, 60.0f);
		gl.glVertex2f(194.0f, 60.0f);
	gl.glEnd();
			
	
	t.beginRendering(screenWidth, screenHeight);
	t.draw(MainClass.player.getHealth()+"%", 200, screenHeight - 55);
	t.endRendering();
	
	previousHealth = MainClass.player.getHealth();

	
}

	public void setSwordloader(boolean tf){
		swordLoaded = tf;
	}
	
	public void setRW(boolean rw){
		this.rw=rw;
	}

	public void render(GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		GLU glu = new GLU();
		
		// Calculating time since last frame.
		Calendar now = Calendar.getInstance();		
		long currentTime = now.getTimeInMillis();
		int deltaTime = (int)(currentTime - previousTime);
		previousTime = currentTime;
		
		// Update any movement since last frame.
		updateMovement(deltaTime);
		updateCamera();
		 
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
        glu.gluLookAt( MainClass.camera.getLocationX(), MainClass.camera.getLocationY(), MainClass.camera.getLocationZ(), 
        		MainClass.camera.getVrpX(), MainClass.camera.getVrpY(), MainClass.camera.getVrpZ(),
        		MainClass.camera.getVuvX(), MainClass.camera.getVuvY(), MainClass.camera.getVuvZ() );

        // Display all the visible objects of MazeRunner.
        for( Iterator<VisibleObject> it = visibleObjects.iterator(); it.hasNext(); ) {
        	it.next().display(gl);
        	for(int i=0;i<MainClass.enemies.size();i++){
        		Enemy e = MainClass.enemies.get(i);
        		if(e.needRemoval()){
        			MainClass.enemies.remove(e);
        			i--;
        		}
        		else{
        			e.display(gl);
        		}
        	}
        	if(!rw){
        		MainClass.sword.display(gl);
        		MainClass.shield.display(gl);
        	}
        	else{
        		MainClass.player.setDefensePower(0);
        		MainClass.rWeapon.display(gl);
        	}
            for(int i=0; i<MainClass.bullets.size(); i++){
            	Bullet b = MainClass.bullets.get(i);
            	if(b.needRemoval()){
            		MainClass.bullets.remove(i);
            		i--;
            	}
            	else{
            		b.display(gl);
            	}
            }
            MainClass.door.display(gl);
            MainClass.doorSwitch.display(gl);
        }
        
        draw2D(gl);

        gl.glLoadIdentity();
        // Flush the OpenGL buffer.
        gl.glFlush();
	}
	
	/**
	 * displayChanged(GLAutoDrawable, boolean, boolean) is called upon whenever the display mode changes.
	 * <p>
	 * Implemented through GLEventListener. 
	 * Seeing as this does not happen very often, we leave this unimplemented.
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// GL gl = drawable.getGL();
	}
	
	/**
	 * reshape(GLAutoDrawable, int, int, int, int, int) is called upon whenever the viewport changes shape, to update the viewport setting accordingly.
	 * <p>
	 * Implemented through GLEventListener. 
	 * This mainly happens when the window changes size, thus changin the canvas (and the viewport 
	 * that OpenGL associates with it). It adjust the projection matrix to accomodate the new shape.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		
		// Setting the new screen size and adjusting the viewport.
		screenWidth = width;
		screenHeight = height;
		gl.glViewport( 0, 0, screenWidth, screenHeight );
		
		// Set the new projection matrix.
		gl.glMatrixMode( GL.GL_PROJECTION );
		gl.glLoadIdentity();
		glu.gluPerspective( 60, screenWidth/screenHeight, .1, 200 );
		gl.glMatrixMode( GL.GL_MODELVIEW );
	}

/*
 * **********************************************
 * *				Methods						*
 * **********************************************
 */

	/**
	 * updateMovement(int) updates the position of all objects that need moving.
	 * This includes rudimentary collision checking and collision reaction.
	 */
	private void updateMovement(int deltaTime)
	{
		MainClass.player.update(deltaTime);
		for(Enemy e: MainClass.enemies){
			e.update(deltaTime, MainClass.player);
		}
		if(!rw){
			MainClass.sword.update(deltaTime, MainClass.player);
			MainClass.shield.update(deltaTime, MainClass.player);
		}
		else{
			MainClass.rWeapon.update(deltaTime, MainClass.player);
		}
		MainClass.mazePheromones.evapPheromones();
		for(Bullet b: MainClass.bullets){
			b.update(deltaTime, MainClass.player);
		}
	}

	/**
	 * updateCamera() updates the camera position and orientation.
	 * <p>
	 * This is done by copying the locations from the Player, since MazeRunner runs on a first person view.
	 */
	private void updateCamera() {
		
		
		
		MainClass.camera.setLocationX( MainClass.player.getLocationX() );
		MainClass.camera.setLocationY( MainClass.player.getLocationY() );  
		MainClass.camera.setLocationZ( MainClass.player.getLocationZ() );
		MainClass.camera.setHorAngle( MainClass.player.getHorAngle() + (MainClass.input.lookback? 180:0));
		MainClass.camera.setVerAngle( MainClass.player.getVerAngle() );
		MainClass.camera.calculateVRP();
	}
	
	//initializer which is called upon by MainClass
	public void mazeInit(GLAutoDrawable drawable, int x, int y, int width, int height){
    	init(drawable);
    	reshape(drawable, 0, 0, screenWidth, screenHeight);
    	MainClass.state.setStopMainGame(true);
        
	}
	
    private void setUpShaders(String vertexShaderLocation, String fragmentShaderLocation,GL gl) {
        shaderProgram = ShaderLoader.loadShaderPair(vertexShaderLocation, fragmentShaderLocation, gl);
    }
	
/*
 * **********************************************
 * *				  Main						*
 * **********************************************
 */
	/**
	 * Program entry point
	 * 
	 * @param args
	 */
}