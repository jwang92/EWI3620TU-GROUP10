package Main;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;

import UserInput.CursorHandler;
import Utils.Buttonbox;
import Utils.Inputbox;
import Utils.SQLHandler;
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
	
	private SQLHandler sql;
//	private CursorHandler c;
	
	public Login(int screenHeight, int screenWidth){
		message = 0;
		
		initWindowSize(screenHeight, screenWidth);
				
		setButtons();
	
		setInputboxes();
		
		sql = new SQLHandler();
//		c = new CursorHandler(MainClass.canvas);
		
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

		buttons.add( new Buttonbox((int)(ScreenWidth/6.0f), (int)(ScreenHeight*0.2f), buttonSizeX, buttonSizeY, "go") );
		buttons.add( new Buttonbox((int)(ScreenWidth*4.0f/6.0f), (int)(ScreenHeight*0.2f), buttonSizeX, buttonSizeY, "go") );
		
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
		
		// Fonts
		float fontSize = ScreenWidth / 25f;
		
		Font f2 = null;
		try {
			f2 = Font.createFont(Font.TRUETYPE_FONT, new File("GeosansLight.ttf"));
		} catch (Exception e){
			System.out.println("foutje");
		}
		Font f = f2.deriveFont(fontSize);
		TextRenderer tr = new TextRenderer(f);

		tr.setColor(0.0f, 0.0f, 0.0f, 1.0f);
				
		
		
		for(int i = 0; i < 2; i++){
			float x = ScreenWidth/2.0f;
			float y = ScreenHeight / 1.3f;
			
			String text = "Login";
			if(i == 1)
				text = "Register";
			
			tr.beginRendering(ScreenWidth, ScreenHeight);
			tr.draw(text, (int) (i*x + (0.1f + margin)*x), (int) ((1-margin - 0.13f)*y));
			tr.endRendering();
			
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
		float fontSize = ScreenWidth / 60f;
		
		Font f2 = null;
		try {
			f2 = Font.createFont(Font.TRUETYPE_FONT, new File("GeosansLight.ttf"));
		} catch (Exception e){
			System.out.println("foutje");
		}
		Font f = f2.deriveFont(fontSize);
		TextRenderer tr = new TextRenderer(f);

		tr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
				
		tr.beginRendering(ScreenWidth, ScreenHeight);
		
		String text = "";
		switch(message){
		case 1: 
			tr.draw("Combination of username", (int) (ScreenWidth*0.25), (int) (ScreenHeight*0.71+fontSize));
			text = "and password doesn't exist.";
			break;
		case 2:
			text = "Username already exists.";
		case 3:
			tr.draw("The username can only contain letters.", (int) (ScreenWidth*0.25), (int) (ScreenHeight*0.71+fontSize));
			text = "The password has to be at least 6 characters long.";
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
		
		
		ResultSet s = sql.query("SELECT 1 FROM users WHERE username = '" + username + "'");
		try {
			
			if(!s.next()){
				return false;	
			} else {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	private boolean inDB(String username, String password) {

		ResultSet s = sql.query("SELECT 1 FROM users WHERE username = '" + username + "' AND password = MD5('" + password + "')");
		try {
			
			if(!s.next()){
				return false;	
			} else {
				MainClass.username = username;
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	private boolean addToDB(String username, String password) {
		
		if(username.matches("[a-zA-Z]+") && password.length() > 6){
			
			sql.updatequery("INSERT INTO users (username, password) VALUES ('" + username + "', MD5('" + password + "'))");
			MainClass.username = username;
			return true;
			
		} else {
			return false;
		}
		
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
		
		// Temporary cheat
		if(Xin > 0 && Xin < 200 && Yin > 0 && Yin < 200){
			MainClass.canvas.removeGLEventListener(this);
			MainClass.initObjects();
			MainClass.state.GameStateUpdate(GameState.TITLE_STATE);
			MainClass.state.setStopTitle(false);
			MainClass.username = "test";
			sql.close();			
		}
		//
		
		for(Inputbox input : inputs)
			input.clickedOn(me);
		
		if (buttons.get(0).OnBox(Xin, Yin) ){
			if (inDB(inputs.get(0).getText(), inputs.get(1).getText())){
				MainClass.canvas.removeGLEventListener(this);
				MainClass.initObjects();
				MainClass.state.GameStateUpdate(GameState.TITLE_STATE);
				MainClass.state.setStopTitle(false);
				sql.close();
			}
			else{
				message = 1;
			}
		}
		else if(buttons.get(1).OnBox(Xin, Yin) ){
			if(!inDB(inputs.get(2).getText()) ){
				
				if(!addToDB(inputs.get(2).getText(), inputs.get(3).getText())){
					message = 3;
				} else{
					MainClass.canvas.removeGLEventListener(this);
					MainClass.initObjects();
					MainClass.state.GameStateUpdate(GameState.TITLE_STATE);
					MainClass.state.setStopTitle(false);
					sql.close();
				}
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
			
			boolean onBox = false;
			
			for(Buttonbox button : buttons){
				if(button.OnBox(Xin, Yin)){
					button.ChangeTexture( true );
					onBox = true;
				}
				else{
					button.ChangeTexture( false );
				}
			}
		
			if(onBox){
				MainClass.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			else{
				MainClass.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

}
