import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLJPanel;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

public class LevelEditorFrame extends Frame implements GLEventListener, MouseListener,MouseMotionListener {
	static final long serialVersionUID = 7526471155622776147L;
	
	// Screen size.
	private int screenWidth = 800, screenHeight = 600;
	private float buttonSize = screenHeight / 10.0f;
	
	//Grid distances
	private int worldSizeX = 10, worldSizeY = 10; 
	private float gridDistance = 50.0f;
	private float gridOffsetX = 10, gridOffsetY = 10 + buttonSize;
	private float gridDragX = 0, gridDragY = 0;

	// A GLCanvas is a component that can be added to a frame. The drawing
	// happens on this component.
	private GLJPanel canvas;

	private static final byte DM_OBJECT = 0;
	private static final byte DM_WALL = 1;
	private static final byte DM_FLOOR = 2;
	private static final byte DM_ROOF = 3;
	private byte drawMode = DM_OBJECT;

	private ArrayList<Point2D.Float> points;
	
	//Grid
	private ArrayList<Point2D.Float> gridpoints;
	private ArrayList<Point2D.Float> grid = new ArrayList<Point2D.Float>();
	private Point2D.Float gridHighlight = new Point2D.Float();
	
	//Walls
	private Wall wall = new Wall(-1, -1, -1, -1, "");
	private WallList wallList = new WallList();
	
	//Floors
	private Floor floor = new Floor();
	private FloorList floorList = new FloorList();
	
	//Roofs
	private Roof roof = new Roof();
	private RoofList roofList = new RoofList();
	
	//Texture
	private ArrayList<Texture> textures;
	private ArrayList<String> textureNames;
	private Texture brickTexture;
	private Texture woodTexture;
	private String textureFileName = "brick.png";
	private String textureFileType = ".png";
	private float textureTop, textureBottom, textureLeft, textureRight;
	//private int textureID;

	/**
	* When instantiating, a GLCanvas is added to draw the level editor. 
	* An animator is created to continuously render the canvas.
	*/
	public LevelEditorFrame(GLJPanel panel) {
		//super("Knight vs Aliens: Level Editor");
		
		//Screen points
		points = new ArrayList<Point2D.Float>();
		
		//Grid points
		gridpoints = new ArrayList<Point2D.Float>();
		
		//Textures
		textures = new ArrayList<Texture>();
		textureNames = new ArrayList<String>();
		

		//Set the desired size and background color of the frame
		//setSize(screenWidth, screenHeight);
		//setBackground(Color.white);
		//setBackground(new Color(0.95f, 0.95f, 0.95f));
		
		//Initialize the grid
		initGrid();

		// When the "X" close button is called, the application should exit.
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// The OpenGL capabilities should be set before initializing the
		// GLCanvas. We use double buffering and hardware acceleration.
		//GLCapabilities caps = new GLCapabilities();
		//caps.setDoubleBuffered(true);
		//caps.setHardwareAccelerated(true);

		// Create a GLCanvas with the specified capabilities and add it to this
		// frame. Now, we have a canvas to draw on using JOGL.
		canvas = panel;
		add(canvas);

		// Set the canvas' GL event listener to be this class. Doing so gives
		// this class control over what is rendered on the GL canvas.
		canvas.addGLEventListener(this);

		// Also add this class as mouse listener, allowing this class to react
		// to mouse events that happen inside the GLCanvas.
		canvas.addMouseListener(this);
		
		canvas.addMouseMotionListener(this);

		// An Animator is a JOGL help class that can be used to make sure our
		// GLCanvas is continuously being re-rendered. The animator is run on a
		// separate thread from the main thread.
		Animator anim = new Animator(canvas);
		anim.start();

		// With everything set up, the frame can now be displayed to the user.
		//setVisible(true);
		}

	@Override
	/**
	 * A function defined in GLEventListener. It is called once, when the frame containing the GLCanvas 
	 * becomes visible. In this assignment, there is no moving ´camera´, so the view and projection can 
	 * be set at initialization. 
	 */
	public void init(GLAutoDrawable drawable) {
		// Retrieve the OpenGL handle, this allows us to use OpenGL calls.
		GL gl = drawable.getGL();

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
			
	}

	
	/**
	 * Initialize the grid
	 */
	public void initGrid(){
		for(int x = 1; x < worldSizeX; x++){
			for(int y = 1; y < worldSizeY; y++){
				grid.add(new Point2D.Float((float)(x),(float)(y)));
			}
		}
	}
	
	public void loadTextures(){
		try {
//			gl.glGenTextures(10, texNamesArray, 0);
//			gl.glBindTexture(GL.GL_TEXTURE_2D, texNamesArray[0]);
            
			//Load texture image	
			//URL stream = getClass().getClassLoader().getResource("brick.png");
			textureFileName = "textures/brick.png";
			File file = new File("textures/brick.png");
			TextureData data;
			data = TextureIO.newTextureData(file, false, "png");
			brickTexture = TextureIO.newTexture(data);
			brickTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			brickTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			brickTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			brickTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			
			textures.add(brickTexture);
			textureNames.add("textures/brick.png");
			
			textureFileName = "textures/wood.png";
			file = new File("textures/wood.png");
			data = TextureIO.newTextureData(file, false, "png");
			woodTexture = TextureIO.newTexture(data);
			woodTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			woodTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			woodTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			woodTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			
			textures.add(woodTexture);
			textureNames.add("textures/wood.png");
			
			//GenerateMipmap
			//gl.glGenerateMipmapEXT(GL.GL_TEXTURE_2D);
			
			// Use linear filter for texture if image is larger than the original texture
			//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
			
			// Use linear filter for texture if image is smaller than the original texture
			//gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
			
			//Select the texture coordinates
			TextureCoords textureCoords = brickTexture.getImageTexCoords();
			textureTop = textureCoords.top();
			textureBottom = textureCoords.bottom();
			textureLeft = textureCoords.left();
			textureRight = textureCoords.right();
		} catch (GLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Override
	/**
	 * A function defined in GLEventListener. This function is called many times per second and should 
	 * contain the rendering code.
	 */
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		// Set the clear color and clear the screen.
		gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		//Draw the grid
		
		drawGrid(gl);

		// Draw a figure based on the current draw mode and user input
		drawFigure(gl);
		
		// Draw the Floor or Roof according to the drawMode
		switch (drawMode) {
		case DM_OBJECT:
			drawFloors(gl);
			break;
		case DM_WALL:
			drawFloors(gl);
			break;
		case DM_FLOOR:
			drawFloors(gl);
			break;
		case DM_ROOF:
			drawRoofs(gl);
			break;
		}
		
		// Draw the Walls
		drawWalls(gl);
		
		// Draw the File
		if(wallList.getWalls().size() > 0){
			try {
				wallList.WriteToFile("WallsTest.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Delete the old points if neccesary
		deletePoints();
		

		// Flush the OpenGL buffer, outputting the result to the screen.
		gl.glFlush();
	
	}
	
	public WallList getWallList(){
		
		return wallList;
		
	}
	
	public FloorList getFloorList(){
			
		return floorList;
		
	}
	
	public RoofList getRoofList(){
		
		return roofList;
		
	}
	
	public void loadFromFolder(String folder) throws FileNotFoundException{
		
		wallList.Read(folder + "/Walls.txt");
		floorList.Read(folder + "/Floor.txt");
		roofList.Read(folder + "/Roof.txt");
		
	}

		
	public void drawGrid(GL gl){
		Point2D.Float p1 = new Point2D.Float();
		for(int i = 0; i < grid.size();i++){
			if(gridHighlight.equals(grid.get(i))){
				gl.glColor3f(0, 0.5f, 0f);
			}
			else{
				gl.glColor3f(0.0f, 0.0f, 0.0f);
			}
			p1.x = gridOffsetX + (grid.get(i).x-1)*gridDistance;
			p1.y = screenHeight - gridOffsetY - (grid.get(i).y-1)*gridDistance;
			pointOnScreen(gl,p1.x,p1.y);
		}
	}
	
	public void setDrawMode(int i){
		
		points.clear();
		gridpoints.clear();
		
		if(i == 1)
			drawMode = DM_WALL;
		else if(i == 2)
			drawMode = DM_ROOF;
		else if(i == 3)
			drawMode = DM_FLOOR;
		else if(i == 4){}
			// Texture inbouwen
	}

	/**
	 * A method that draws a figure, when the user has inputted enough points
	 * for the current draw mode.
	 * 
	 * @param gl
	 */
	private void drawFigure(GL gl) {
		// Set line and point size, and set color to black.
		gl.glLineWidth(3);
		gl.glPointSize(10.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);

		//Screen points
		Point2D.Float p1, p2, p3, p4;
		
		//Grid points
		Point2D.Float g1, g2, g3, g4;
		switch (drawMode) {
		case DM_OBJECT:
			if (points.size() >= 1) {
				// If the draw mode is "point" and the user has supplied at
				// least one point, draw that point.
				p1 = points.get(0);
				pointOnScreen(gl, p1.x, p1.y);
			}
		case DM_WALL:
			if (points.size() >= 2) {
				// If the draw mode is "line" and the user has supplied at least
				// two points, draw a line between those points
				g1 = gridpoints.get(0);
				g2 = gridpoints.get(1);
				wall = new Wall((int)g1.x,(int)g1.y,(int)g2.x,(int)g2.y,"brick.png");
				wallList.addWall(wall);
			}
			break;
		case DM_FLOOR:
			if (points.size() >= 4) {
				// If the draw mode is "floor" and the user has supplied at least
				// three points, draw a line between those points
				g1 = gridpoints.get(0);
				g2 = gridpoints.get(1);
				g3 = gridpoints.get(2);
				g4 = gridpoints.get(3);
				ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
				p.add(g1);
				p.add(g2);
				p.add(g3);
				p.add(g4);
				if(floorList.getFloors().size()==0){
					floor = new Floor(p,"textures/brick.png");
				}
				else{
					floor = new Floor(p,"textures/wood.png");
				}
				floorList.addFloor(floor);
				}
			break;
		case DM_ROOF:
			if (points.size() >= 4) {
				// If the draw mode is "roof" and the user has supplied at least
				// three points, draw a line between those points
				g1 = gridpoints.get(0);
				g2 = gridpoints.get(1);
				g3 = gridpoints.get(2);
				g4 = gridpoints.get(3);
				ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
				p.add(g1);
				p.add(g2);
				p.add(g3);
				p.add(g4);
				roof = new Roof(p,"textures/brick.png");
				roofList.addRoof(roof);
				}
			break;
			}
		}
	
	private void drawWalls(GL gl){
		//Screen points
		Point2D.Float p1 = new Point2D.Float(), p2 = new Point2D.Float();
		for(int i = 0;i<wallList.getWalls().size();i++){
			p1.x = gridOffsetX + (wallList.getWalls().get(i).getStartx()-1)*gridDistance;
			p1.y = screenHeight -gridOffsetY - (wallList.getWalls().get(i).getStarty()-1)*gridDistance;
			p2.x = gridOffsetX + (wallList.getWalls().get(i).getEndx()-1)*gridDistance;
			p2.y = screenHeight - gridOffsetY - (wallList.getWalls().get(i).getEndy()-1)*gridDistance;
			wallOnScreen(gl, p1.x, p1.y, p2.x, p2.y);
		}
	}
	
	private void drawFloors(GL gl){
		//Screen points
		ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
		for(int i = 0; i < floorList.getFloors().size();i++){
			for(int j =0; j < floorList.getFloors().get(i).getPoints().size();j++){
				Point2D.Float p1 = new Point2D.Float();
				p1.x = gridOffsetX + (floorList.getFloors().get(i).getPoints().get(j).x-1)*gridDistance;
				p1.y = screenHeight - gridOffsetY - (floorList.getFloors().get(i).getPoints().get(j).y-1)*gridDistance;
				p.add(p1);
			}
			int textureID = textureNames.lastIndexOf(floorList.getFloors().get(i).getTexture());
			polygonOnScreen(gl,p,textureID);
			p.clear();
		}
	}
	
	private void drawRoofs(GL gl){
		//Screen points
		ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();
		for(int i = 0; i < roofList.getRoofs().size();i++){
			for(int j =0; j < roofList.getRoofs().get(i).getPoints().size();j++){
				Point2D.Float p1 = new Point2D.Float();
				p1.x = gridOffsetX + (roofList.getRoofs().get(i).getPoints().get(j).x-1)*gridDistance;
				p1.y = screenHeight - gridOffsetY - (roofList.getRoofs().get(i).getPoints().get(j).y-1)*gridDistance;
				p.add(p1);
			}
			int textureID = textureNames.lastIndexOf(floorList.getFloors().get(i).getTexture());
			polygonOnScreen(gl,p,textureID);
			p.clear();
		}
	}
	
	
	
	
	/**
	 * Help method that uses GL calls to draw a point.
	 */
	private void pointOnScreen(GL gl, float x, float y) {
		// Aanpassen van de grootte van de punten hier
		//gl.glPointSize(5.0f);
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2f(x, y);
		gl.glEnd();
	}

	/**
	 * Help method that uses GL calls to draw a line.
	 */
	private void wallOnScreen(GL gl, float x1, float y1, float x2, float y2) {
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glEnd();
	}
	
	private void polygonOnScreen(GL gl, ArrayList<Point2D.Float> p, int textureID){
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_TEXTURE);
		
		//Enable textures
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glEnable(GL.GL_TEXTURE_2D);
		//gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_DECAL);
		
		//Apply texture
		//gl.glBindTexture(GL.GL_TEXTURE_2D, texNamesArray[0]);
		textures.get(textureID).getTarget();
		//brickTexture.enable();
		textures.get(textureID).bind();		
		gl.glBegin(GL.GL_POLYGON);
//		for(int i =0; i<p.size();i++){
//			if(i ==0){
//				gl.glTexCoord2f(0.0f,1.0f);
//			} else if(i == 1){
//				gl.glTexCoord2f(0.0f,1.0f);
//			} else if(i == 2){
//				gl.glTexCoord2f(1.0f,1.0f);
//			} else if(i == 3){
//				gl.glTexCoord2f(1.0f,0.0f);
//			}
//			gl.glVertex2f(p.get(i).x, p.get(i).y);
//		}
		gl.glTexCoord2f(textureTop,textureLeft);
		gl.glVertex2f(p.get(0).x, p.get(0).y);
		gl.glTexCoord2f(textureBottom,textureLeft);
		gl.glVertex2f(p.get(1).x, p.get(1).y);
		gl.glTexCoord2f(textureBottom,textureRight);
		gl.glVertex2f(p.get(2).x, p.get(2).y);
		gl.glTexCoord2f(textureTop,textureRight);
		gl.glVertex2f(p.get(3).x, p.get(3).y);
		
		gl.glEnd();
		
		//Disable texture
		textures.get(textureID).disable();
		
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
	}
	
//	private void roofOnScreen(GL gl, ArrayList<Point2D.Float> p){
//		gl.glColor3f(0.0f, 0.0f, 1.0f);
//		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
//		gl.glBegin(GL.GL_POLYGON);
//		for(int i =0; i<p.size();i++){
//			gl.glVertex2f(p.get(i).x, p.get(i).y);
//		}
//		gl.glEnd();
//		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
//	}
		
	/**
	 * Help method that uses Gl calls to draw a triangle
	 */
	private void triangleOnScreen(GL gl, float x1, float y1, float x2, float y2, float x3, float y3) {
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glVertex2f(x3, y3);
		gl.glEnd();
	}
		
	/**
	 * Help method that uses GL calls to draw a square
	 */
	private void boxOnScreen(GL gl, float x, float y, float size) {
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + size, y);
		gl.glVertex2f(x + size, y + size);
		gl.glVertex2f(x, y + size);
		gl.glEnd();
	}
	
	private void deletePoints(){
		if (drawMode == DM_OBJECT && points.size() >= 1) {
			// If we're placing objects and one point was stored, reset the points list
			points.clear();
			gridpoints.clear();
		} else if (drawMode == DM_WALL && points.size() >= 2) {
			// If we're drawing lines and two points were already stored, reset the points list
			points.clear();
			gridpoints.clear();
		} else if (drawMode == DM_FLOOR && points.size() >= 4) {
			// If we're drawing Floors and four points were already stored, reset the points list
			points.clear();
			gridpoints.clear();
		} else if (drawMode == DM_ROOF && points.size() >= 4) {
			// If we're drawing Roofs and four points were already stored, reset the points list
			points.clear();
			gridpoints.clear();
		}
	}

	@Override
	/**
	 * A function defined in GLEventListener. This function is called when there is a change in certain 
	 * external display settings. 
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
		// Not needed.
	}

	@Override
	/**
	 * A function defined in GLEventListener. This function is called when the GLCanvas is resized or moved. 
	 * Since the canvas fills the frame, this event also triggers whenever the frame is resized or moved.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();

		// Set the new screen size and adjusting the viewport
		screenWidth = width;
		screenHeight = height;
		buttonSize = height / 10.0f;
		gl.glViewport(0, 0, screenWidth, screenHeight);

		// Update the projection to an orthogonal projection using the new screen size
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, screenWidth, 0, screenHeight, -1, 1);
	}

	@Override
	/**
	 * A function defined in MouseListener. Is called when the pointer is in the GLCanvas, and a mouse button is released.
	 */
	public void mouseReleased(MouseEvent me) {
	
		if((gridHighlight.x >= ((me.getX()-gridOffsetX)/gridDistance+1)-0.1) 
				&& (gridHighlight.x <= ((me.getX()-gridOffsetX)/gridDistance+1)+0.1)
				&& (gridHighlight.y >= ((me.getY()-gridOffsetY)/gridDistance+1)-0.1)
				&& (gridHighlight.y <= ((me.getY()-gridOffsetY)/gridDistance+1)+0.1)){	
			if (drawMode == DM_OBJECT && points.size() >= 1) {
				// If we're placing objects and one point was stored, reset the points list
				points.clear();
				gridpoints.clear();
			} else if (drawMode == DM_WALL && points.size() >= 2) {
				// If we're drawing lines and two points were already stored, reset the points list
				points.clear();
				gridpoints.clear();
			} else if (drawMode == DM_FLOOR && points.size() >= 4) {
				// If we're drawing Floors and four points were already stored, reset the points list
				points.clear();
				gridpoints.clear();
			} else if (drawMode == DM_ROOF && points.size() >= 4) {
				// If we're drawing Roofs and four points were already stored, reset the points list
				points.clear();
				gridpoints.clear();
			}
			// Add a new point to the points list.
			points.add(new Point2D.Float((float)(gridHighlight.x*gridDistance),(float)(screenHeight - gridHighlight.y*gridDistance)));
			gridpoints.add(gridHighlight);
			System.out.println(gridHighlight.x*gridDistance + " " + (screenHeight - gridHighlight.y*gridDistance));
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Not needed.
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Not needed.
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Not needed.
	}

	@Override
	public void mousePressed(MouseEvent me) {
		gridDragX = me.getX();
		gridDragY = me.getY();
	}
	
	@Override
	public void mouseMoved(MouseEvent me) {
		Point2D.Float p1 = new Point2D.Float();
		p1.x = me.getX();
		p1.y = me.getY();
		gridHighlight = new Point2D.Float();
		for(int i = 0; i<grid.size();i++){
			if(((grid.get(i).x) >= (((p1.x-gridOffsetX)/gridDistance+1)-0.1) 
					&& grid.get(i).x <= ((p1.x-gridOffsetX)/gridDistance+1)+0.1)
					&&((grid.get(i).y) >= (((p1.y-gridOffsetY)/gridDistance+1)-0.1)
					&& grid.get(i).y <= ((p1.y-gridOffsetY)/gridDistance+1)+0.1)){
				gridHighlight = grid.get(i);
			}	
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent me) {
		gridOffsetX += me.getX()- gridDragX;
		gridOffsetY += me.getY()- gridDragY;
		gridDragX = me.getX();
		gridDragY = me.getY();
		
	}
	

}
