package Utils;
import javax.media.opengl.GL;

import Main.MainClass;

import com.sun.opengl.util.j2d.TextRenderer;


public class pwInputbox extends Inputbox {

	public pwInputbox(int xCoord, int yCoord, int FontSize, int StringLength, String defText, MainClass m){
		super(xCoord, yCoord, FontSize, StringLength, defText, m);
	}
	
	public void DrawText(GL gl, TextRenderer tr){
				
		String text = "";
		for(int i = 0; i < this.text.length(); i++){
			text = text + "*";		
		}
		tr.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		if(text.equals("") && !selected){
			text = defText;
			tr.setColor(0.0f, 0.0f, 0.0f, 0.3f);
		}
		
		tr.beginRendering(screenWidth, screenHeight);
		tr.draw(text, x+margin, (int) (y+margin+sH/4.0));
		tr.endRendering();

	}
	
}
