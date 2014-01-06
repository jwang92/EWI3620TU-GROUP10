package Main;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;

import Utils.Buttonbox;
import Utils.Inputbox;
import Utils.pwInputbox;

import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;


public class Login implements GLEventListener, MouseListener, MouseMotionListener {
	/*
	 * **********************************************
	 * *			Local Variables					*
	 * **********************************************
	 */
	
	//frame setup
	public int ScreenWidth, ScreenHeight;
	
	boolean stop = false;
	private boolean startup = true;
	
	private Texture BGTexture;
	private int message;
	
	private ArrayList<Buttonbox> buttons;
	public ArrayList<Inputbox> inputs;
	
	
	public Login(int screenHeight, int screenWidth){
		message = 0;
		
		initWindowSize(screenHeight, screenWidth);
				
		setButtons();
	
		setInputboxes();
		
		// Add this class as mouse motion listener, allowing this class to
		// react to mouse events that happen inside the GLCanvas.
		MainClass.canvas.addMouseMotionListener(this);
//		MainClass.canvas.addGLEventListener(this);

	}
	
	public void initWindowSize(int screenHeight, int screenWidth){
		ScreenWidth = screenWidth;
		ScreenHeight = screenHeight;
	}
	
	public void setButtons(){
		buttons = new ArrayList<Buttonbox>();

		int buttonSizeX = (int) (ScreenWidth/7);
		int buttonSizeY = (int) (ScreenHeight/13);

		buttons.add( new Buttonbox((int)(ScreenWidth/6.0f), (int)(ScreenHeight*0.2f), buttonSizeX, buttonSizeY, "start") );
		buttons.add( new Buttonbox((int)(ScreenWidth*4.0f/6.0f), (int)(ScreenHeight*0.2f), buttonSizeX, buttonSizeY, "start") );
		
	}
	
	public void setInputboxes(){
		inputs = new ArrayList<Inputbox>();
		
		inputs.add( new Inputbox((int)(ScreenWidth*0.1), (int)(ScreenHeight*0.5), 20, 8, "login") );
		inputs.add( new pwInputbox((int)(ScreenWidth*0.1), (int)(ScreenHeight*0.4), 20, 8, "password") );
		inputs.add( new Inputbox((int)(ScreenWidth*0.6), (int)(ScreenHeight*0.5), 20, 8, "new user") );
		inputs.add( new pwInputbox((int)(ScreenWidth*0.6), (int)(ScreenHeight*0.4), 20, 8, "password") );

	}
		
	public void render (GLAutoDrawable drawable){		

		GL gl = drawable.getGL();
		// Set the clear color and clear the screen.
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// Draw the background
		drawBackground(gl);
		
		// Draw the inputs.
		drawInputboxes(gl);
		
		// Draw the buttons.
		drawButtons(gl);
		
		// Draw the textmessages
		drawText(gl);
				
		gl.glFlush();

	}
	
	public void loadBackground(GL gl, String textureName, String textureFileType){
		try {
		    File folder = new File("menu_files/");
		    File[] tList = folder.listFiles();
		    
		    for (File file : tList)
		    {
	            if( file.getName().equals(textureName+"."+textureFileType) )
	            {
	            	//Get the name of the texture
	            	String textureFileName = "menu_files/" + file.getName();
	            	
	            	//Load the texture
	            	File filetexture = new File(textureFileName);
	    			TextureData data;
	    			data = TextureIO.newTextureData(filetexture, false, textureFileType);
	    			BGTexture = TextureIO.newTexture(data);
	    			
	    			//Set the the texture parameters
	    			BGTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
	    			BGTexture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
	    			BGTexture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
	    			BGTexture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
	    			
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
	
	private void drawBackground(GL gl) {
		
		if(startup){
			loadBackground(gl, "background_ufos", "png");
			
			for( Buttonbox button : buttons)
				button.loadTextures(gl);
			
			startup = false;
			
		}

		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		BGTexture.getTarget();
		BGTexture.bind();
				
		gl.glBegin(GL.GL_POLYGON);
			gl.glTexCoord2f(0, 1); gl.glVertex2f(0, 0);
			gl.glTexCoord2f(0, 0); gl.glVertex2f(0, ScreenHeight);
			gl.glTexCoord2f(1, 0); gl.glVertex2f(ScreenWidth, ScreenHeight);
			gl.glTexCoord2f(1, 1); gl.glVertex2f(ScreenWidth, 0);
		gl.glEnd();
		
		BGTexture.disable();
		
	}
	
	private void drawInputboxes(GL gl){
		
		float margin = 0.1f;
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE);
		
		for(int i = 0; i < 2; i++){
			float x = ScreenWidth/2.0f;
			float y = ScreenHeight;
			gl.glBegin(GL.GL_QUADS);
				gl.glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
				gl.glVertex2f(i*x + margin*x, margin*y);
				gl.glVertex2f(i*x + (1-margin)*x, margin*y);
				gl.glVertex2f(i*x + (1-margin)*x, (1-margin)*y);
				gl.glVertex2f(i*x + margin*x, (1-margin)*y);
			gl.glEnd();
		}
		gl.glDisable(GL.GL_BLEND);
		
		for( Inputbox input : inputs)
			input.DrawInputbox(gl, ScreenHeight, ScreenWidth);
	}
	
	private void drawButtons(GL gl) {
		// Draw the background boxes
		for( Buttonbox button : buttons)
			button.drawButtonbox(gl, ScreenHeight, ScreenWidth);
		
	}
	
	private void drawText(GL gl) {
		// Box
		int x = (int) (ScreenWidth*0.23);
		int y = (int) (ScreenHeight*0.69);
		int dx = (int) (ScreenWidth*0.54);
		int dy = (int) (ScreenHeight*0.08);
		
		if(message!=0){
			gl.glBegin(GL.GL_QUADS);
				gl.glColor3f(1.0f, 1.0f, 1.0f);
				gl.glVertex2f(x, y);
				gl.glVertex2f(x+dx, y);
				gl.glVertex2f(x+dx, y+dy);
				gl.glVertex2f(x, y+dy);
			gl.glEnd();
		}
		
		// Fonts
		int fontsize = (int) Math.round(ScreenHeight*0.035);
		Font f = new Font("Courier New", Font.PLAIN, fontsize);
		TextRenderer tr = new TextRenderer(f);

		tr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
				
		tr.beginRendering(ScreenWidth, ScreenHeight);
		
		String text = "";
		switch(message){
		case 1: 
			tr.draw("Combination of username", (int) (ScreenWidth*0.25), (int) (ScreenHeight*0.7+fontsize));
			text = "and password doesn't exist.";
			break;
		case 2:
			text = "Username already exists.";
		}
		tr.draw(text, (int) (ScreenWidth*0.25), (int) (ScreenHeight*0.7));
		
		tr.endRendering();
		
		gl.glColor3f(1.0f, 1.0f, 1.0f);
	}
		
/*
 * **********************************************
 * *			DB methods						*
 * **********************************************
 */
	private boolean inDB(String username) {
		// TODO bestaat de username in DB?
		return false;
	}
	
	private boolean inDB(String username, String password) {
		// TODO bestaan combinatie van username en password in DB?
		inDB(username);
		
		return false;
	}
	
	private void addToDB(String username, String password) {
		// TODO voeg combinatie van username en password toe aan DB
	}
	
	
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
		
		MainClass.state.setStopMainGame(true);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();

		// Set the new screen size and adjusting the sizes
		initWindowSize(height, width);
//		setDrawButtons();

		
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
		
		for(Inputbox input : inputs)
			input.clickedOn(me);
		
		if (buttons.get(0).OnBox(Xin, Yin) ){
			if (inDB(inputs.get(0).getText(), inputs.get(1).getText())){
				MainClass.canvas.removeGLEventListener(this);
				MainClass.initObjects();
				MainClass.state.GameStateUpdate(GameState.TITLE_STATE);
				MainClass.state.setStopTitle(false);
			}
			else{
				message = 1;
			}
		}
		else if(buttons.get(1).OnBox(Xin, Yin) ){
			if(!inDB(inputs.get(2).getText()) ){
				addToDB(inputs.get(2).getText(), inputs.get(3).getText());
				MainClass.canvas.removeGLEventListener(this);
				MainClass.initObjects();
				MainClass.state.GameStateUpdate(GameState.TITLE_STATE);
				MainClass.state.setStopTitle(false);
			}
			else{
				message = 2;
			}
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
			
			for(Buttonbox button : buttons)
				button.ChangeTexture( button.OnBox(Xin, Yin) );				
		
//			System.out.println(mouseOnBox);
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

}
