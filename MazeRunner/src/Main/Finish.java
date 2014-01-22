package Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLException;

import Utils.Buttonbox;

import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

public class Finish  implements MouseListener, MouseMotionListener {
	/*
	 * **********************************************
	 * *			Local Variables					*
	 * **********************************************
	 */
	private MainClass main;
	
	boolean stop = false;
	private boolean startup = true;
	
	private Texture BGTexture;
	
	private ArrayList<Buttonbox> buttons;
	
	private int timer, timer2;
	private int score;

	
	public Finish(MainClass m){
		main = m;
		timer = 0;
		timer2 = 0;
		score = 0;
		
		setButtons();
				
		// Also add this class as mouse motion listener, allowing this class to
		// react to mouse events that happen inside the GLCanvas.
		main.canvas.addMouseMotionListener(this);

	}
	
	public void setButtons(){
		buttons = new ArrayList<Buttonbox>();

		int buttonSizeX = (int) (main.screenWidth/7);
		int buttonSizeY = (int) (main.screenHeight/13);

		int x = (int) (main.screenWidth/6.0f - buttonSizeX/2.0f);
		int y1 = (int) (main.screenHeight/1.2f - 0.5f*buttonSizeY);
		int y2 = (int) (main.screenHeight/1.2f - 1.6f*buttonSizeY);
		
		buttons.add( new Buttonbox(x, y1, buttonSizeX, buttonSizeY, "start", main));
		buttons.add( new Buttonbox(x, y2, buttonSizeX, buttonSizeY, "exit", main));
		
	}
		
	public void render(GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		// Set the clear color and clear the main.screen.
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// Draw background
		drawBackground(gl);
		
		// Draw the buttons.
		drawButtons(gl);

		// Draw the endingtext
		drawText(gl);
		
		gl.glFlush();
	

	}
	
	public void drawUsername(GL gl){
		
		float fontSize = main.screenWidth / 60f;
		
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glBegin(GL.GL_QUADS);
			gl.glColor4f(0f, 0f, 0f, 0.5f);
			gl.glVertex2f(0, 0);
			gl.glVertex2f(0, main.screenHeight * 0.07f);
			gl.glVertex2f(main.screenWidth, main.screenHeight * 0.07f);
			gl.glVertex2f(main.screenWidth, 0);
		gl.glEnd();
		
		gl.glDisable(GL.GL_BLEND);
		
		TextRenderer t = main.trenderers.get(0);
		t.setColor(1.0f, 1.0f, 1.0f, 1.0f);

		t.beginRendering(main.screenWidth, main.screenHeight);
		t.draw("Ingelogd als " + main.username, (int) (main.screenWidth * 0.02f), (int) (main.screenHeight * 0.02f));
		t.endRendering();
		
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

			addScoretoDB();
			
			startup = false;
			
		}

		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		BGTexture.getTarget();
		BGTexture.bind();
				
		gl.glBegin(GL.GL_POLYGON);
			gl.glTexCoord2f(0, 1); gl.glVertex2f(0, 0);
			gl.glTexCoord2f(0, 0); gl.glVertex2f(0, main.screenHeight);
			gl.glTexCoord2f(1, 0); gl.glVertex2f(main.screenWidth, main.screenHeight);
			gl.glTexCoord2f(1, 1); gl.glVertex2f(main.screenWidth, 0);
		gl.glEnd();
		
		BGTexture.disable();
		
	}
	
	private void drawButtons(GL gl) {
		// Draw the background boxes
		for( Buttonbox button : buttons)
			button.drawButtonbox(gl, main.screenHeight, main.screenWidth);
		
	}
	
	private void drawText(GL gl){
		
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			
		int x0 = (int) (main.screenWidth *20.0f/600.0f);
		int x1 = (int) (main.screenWidth *580.0f/600.0f);
		int y0 = (int) (main.screenHeight *20.0f/600.0f);
		int y1 = (int) (main.screenHeight *400.0f/600.0f);
		
		gl.glColor4f(0, 0, 0, 0.7f);
		gl.glBegin(GL.GL_QUADS);
			gl.glVertex2f(x0, y0);
			gl.glVertex2f(x0, y1);
			gl.glVertex2f(x1, y1);
			gl.glVertex2f(x1, y0);
		gl.glEnd();
		
		gl.glDisable(GL.GL_BLEND);

		String text = "";
		String text2 = "Developers: ";
		
		if(100 < timer && timer < 300)
			text = "Our kingdom has been saved!";
		else if(400 < timer && timer < 600)
			text = "The realm thanks you young knight..";
		else if(700 < timer)
			text = "Score: ";
		
		if(timer > 750){
			text = text + score;
			if(timer > 800 && score < main.player.getScore())
				score++;
		}
		
		TextRenderer t = main.trenderers.get(0);
		t.setColor(1.0f, 1.0f, 1.0f, 1.0f);

		t.beginRendering(main.screenWidth, main.screenHeight);
		t.draw(text, (int) (main.screenWidth * 0.1f), (int) (main.screenHeight * 0.45f));
		
		if(timer2 > 100){
			t.draw("Thank you for playing Medival Invasion", (int) (main.screenWidth * 0.1f), (int) (main.screenHeight * 0.35f));
		}
		if(timer2 > 200){
			if(timer2 > 250)
				text2 = text2 + "Guido";
			if(timer2 > 300)
				text2 = text2 + ", Ruben";
			if(timer2 > 350)
				text2 = text2 + ", Hassan";
			if(timer2 > 400)
				text2 = text2 + " en Johnny";
			
			t.draw(text2, (int) (main.screenWidth * 0.1f), (int) (main.screenHeight * 0.25f));
		}
		
		t.endRendering();
		
		
		if(timer < 801)
			timer++;
		if(timer2 < 401 && score>=main.player.getScore())
			timer2++;
		
//		System.out.println(timer + " <timers> " + timer2 + " + score: " + score);
	}
	
/*
 * **********************************************
 * *			DB methods						*
 * **********************************************
 */
	
	private void addScoretoDB(){
		//TODO: add player with score to the database
	}
	
	
/*
 * **********************************************
 * *		OpenGL event handlers				*
 * **********************************************
 */

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
		 * matching the viewing frustum to the MainClass.screen size.
		 */
		gl.glOrtho(0, main.screenWidth, 0, main.screenHeight, -1, 1);

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
		
//		MainClass.state.setStopMainGame(true);
		main.state.setStopFinish(true);
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
		if(timer>200){
			int Xin = me.getX();
			int Yin = me.getY();
			
			if (buttons.get(0).OnBox(Xin, Yin) ){
				main.initObjects();
				main.state.GameStateUpdate(GameState.MAINGAME_STATE);
				main.state.setStopFinish(true);
				main.input.setDefMouse(me);
				main.state.setStopMainGame(false);
			}
			else if (buttons.get(1).OnBox(Xin, Yin) ){
				main.initObjects();
				main.state.GameStateUpdate(GameState.TITLE_STATE);
				main.state.setStopFinish(true);
				main.state.setStopTitle(false);
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
		if(main.state.getState()==7 && timer > 200){
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
				main.cursor.setCursor(-2);
			}
			else{
				main.cursor.setCursor(-1);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
