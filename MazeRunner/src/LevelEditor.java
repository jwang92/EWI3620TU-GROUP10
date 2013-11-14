import java.awt.*;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLJPanel;
import javax.swing.*;

public class LevelEditor {
	
	public static void main(String[] args) {
		Editor();
	}
	  
	public static void Editor(){
		
		JFrame f = new JFrame("Testert");
		f.setSize(800, 700);
		
		Container content = f.getContentPane();
		
		JPanel controlArea = new JPanel(new GridLayout(3, 1));		
		
		JPanel opties1 = new JPanel(new GridLayout(2, 2));
		opties1.setBorder(BorderFactory.createTitledBorder("Tekenmodus"));
	    ButtonGroup bg = new ButtonGroup();
		JRadioButton option;
	    option = new JRadioButton("Muur");
	    bg.add(option);
	    option.setSelected(true);
	    opties1.add(option);
	    option = new JRadioButton("Plafond");
	    bg.add(option);
	    opties1.add(option);
	    option = new JRadioButton("Vloer");
	    bg.add(option);
	    opties1.add(option);
	    option = new JRadioButton("Iets anders");
	    bg.add(option);
	    opties1.add(option);
	    controlArea.add(opties1);
	
		content.add(controlArea, BorderLayout.WEST);
		
		// The OpenGL capabilities should be set before initializing the
		// GLCanvas. We use double buffering and hardware acceleration.
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		GLJPanel drawingArea = new GLJPanel(caps);

		drawingArea.setPreferredSize(new Dimension(700, 700));
	    drawingArea.setBorder(BorderFactory.createLineBorder (Color.white, 2));

	    LevelEditorFrame le = new LevelEditorFrame(drawingArea);
	    le.setDrawMode();
	  
	    content.add(drawingArea, BorderLayout.EAST);
	    f.pack();
	    f.setVisible(true);
		
	}
	
}