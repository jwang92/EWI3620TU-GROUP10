package Utils;
import javax.media.opengl.GL;

import com.sun.opengl.util.j2d.TextRenderer;


public class pwInputbox extends Inputbox {

	public pwInputbox(int xCoord, int yCoord, int FontSize, int StringLength, String defText){
		super(xCoord, yCoord, FontSize, StringLength, defText);
	}
	
	public void DrawText(GL gl){
				
		TextRenderer tr = setTR(sH);

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