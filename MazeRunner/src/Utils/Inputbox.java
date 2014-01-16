package Utils;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.media.opengl.GL;

import Main.MainClass;

import com.sun.opengl.util.j2d.TextRenderer;


public class Inputbox {
	private MainClass main;
	
	protected String defText;		//default text of the inputbox
	protected String text;			//text in the inputbox
	protected int screenWidth, screenHeight;
	protected int x, y;				//xy coordinates
	protected int margin;
	protected int sH, sL;			//max StringHeight and -Length
	protected int bH, bL;			//boxheight and -length
	protected Font f;
	protected boolean selected;		//selected
	
	public Inputbox(int xCoord, int yCoord, int FontSize, int StringLength, String defText, MainClass mclass){
		main = mclass;
		x = xCoord;
		y = yCoord;
		
		screenHeight = main.screenHeight;
		screenWidth = main.screenWidth;
		
		sH = FontSize;
		sL = StringLength;
		this.defText = defText;
		
		settup();
	}
	
	private void settup(){
		margin = 2;
		text = "";
		selected = false;
		bH = sH + 2*margin;
		bL = sL*sH + 2*margin;
		
	}
	
	public void DrawInputbox(GL gl, int scrH, int scrW, TextRenderer tr){

		adjustToReshape(scrH, scrW);
		
		DrawBox(gl);
		
		DrawText(gl, tr);
		
		gl.glLineWidth(1);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
	}
	
	public void adjustToReshape(int scrH, int scrW){
		double SX = (double) scrW / (double) screenWidth;
		double SY = (double) scrH / (double) screenHeight;
				
		if(SX != 1 || SY != 1){
			System.out.println(screenHeight);
			System.out.println(screenWidth);
			
			screenHeight = scrH;
			screenWidth = scrW;
			
			x = (int) Math.round(SX*x);
			y = (int) Math.round(SY*y);
			sH = (int) Math.round(SY*sH);
			bH = sH + 2*margin;
			bL = sL*sH + 2*margin;
		}		
	}
	
	public void DrawBox(GL gl){
		
		gl.glBegin(GL.GL_QUADS);
			gl.glColor3f(1.0f, 1.0f, 1.0f);
			gl.glVertex2f(x, y);
			gl.glVertex2f(x+bL, y);
			gl.glVertex2f(x+bL, y+bH);
			gl.glVertex2f(x, y+bH);
		gl.glEnd();

		if(selected)
			gl.glLineWidth(2);
		
		gl.glBegin(GL.GL_LINE_LOOP);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glVertex2f(x, y);
			gl.glVertex2f(x+bL, y);
			gl.glVertex2f(x+bL, y+bH);
			gl.glVertex2f(x, y+bH);
		gl.glEnd();

	}

//	public TextRenderer setTR(int sH){
//		float size = (float) sH;
//		Font f = this.f.deriveFont(size);
//	
//		return new TextRenderer(f);
//	}
	
	public void DrawText(GL gl, TextRenderer tr){
		
		String text = this.text;

		tr.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		if(text.equals("") && !selected){
			text = defText;
			tr.setColor(0.0f, 0.0f, 0.0f, 0.3f);
		}
		
		tr.beginRendering(screenWidth, screenHeight);
		tr.draw(text, x+margin, (int) (y+margin+sH/4.0));
		tr.endRendering();

	}
	
	public String getText(){
		return text;
	}
	
	public void setSelect(boolean selected){
		this.selected = selected;
	}
	
	public boolean getSelect(){
		return selected;
	}
	
	public void clickedOn(MouseEvent me){
		int Xin = me.getX();
		int Yin = screenHeight-me.getY();
		
		selected = ( x <= Xin && Xin <= x+bL ) && ( y <= Yin && Yin <= y+bH );
		
	}
	
	public void enteredKey(KeyEvent ke){
		char Key = ke.getKeyChar();
		
		boolean num = '0' <= Key && Key <= '9';
		boolean letter = 'a' <= Key && Key <= 'z';
		boolean capletter = 'A' <= Key && Key <= 'Z';
		boolean space = Key == ' ';
		if((num || letter || capletter || space) && text.length()<sL){
			text = text + Key;
		}
		
		if(ke.getKeyCode()==8 && text.length()>0){
			text = text.substring(0, text.length()-1);
		}
	}
	
}
