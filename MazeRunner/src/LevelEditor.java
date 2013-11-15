import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLJPanel;
import javax.swing.*;

public class LevelEditor implements ActionListener{
	
	private LevelEditorFrame le;
	
	
	public static void main(String[] args) {
		new LevelEditor();
	}
	  
	public LevelEditor(){
		
		JFrame f = new JFrame("Testert");
		f.setSize(800, 700);
		
		Container content = f.getContentPane();
		
		JPanel controlArea = new JPanel(new GridLayout(5, 1));		
		
		JPanel opties1 = new JPanel(new GridLayout(2, 2));
		opties1.setBorder(BorderFactory.createTitledBorder("Tekenmodus"));
	    ButtonGroup bg = new ButtonGroup();
		JRadioButton  option1 = new JRadioButton("Muur");
	    bg.add(option1);
	    option1.setSelected(true);
	    opties1.add(option1);
	    JRadioButton option2 = new JRadioButton("Plafond");
	    bg.add(option2);
	    opties1.add(option2);
	    JRadioButton option3 = new JRadioButton("Vloer");
	    bg.add(option3);
	    opties1.add(option3);
	    JRadioButton option4 = new JRadioButton("Textures tekenen");
	    bg.add(option4);
	    opties1.add(option4);
	    controlArea.add(opties1);
	    
	    option1.setActionCommand("Muur");
	    option1.addActionListener(this);
	    
	    option2.setActionCommand("Plafond");
	    option2.addActionListener(this);
	    
	    option3.setActionCommand("Vloer");
	    option3.addActionListener(this);
	    
	    option4.setActionCommand("Texture");
	    option4.addActionListener(this);
	    
	    JPanel opties2 = new JPanel(new GridLayout(4, 1));
	    opties2.setBorder(BorderFactory.createTitledBorder("Texture:"));
	    String[] textures = {"brick.png", "wood.png", "trees.png", "water.png"};
	    
		JComboBox c = new JComboBox(textures);
		opties2.add(c);
		
		controlArea.add(opties2);
	
		content.add(controlArea, BorderLayout.WEST);
		
		// The OpenGL capabilities should be set before initializing the
		// GLCanvas. We use double buffering and hardware acceleration.
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		GLJPanel drawingArea = new GLJPanel(caps);

		drawingArea.setPreferredSize(new Dimension(700, 700));
	    drawingArea.setBorder(BorderFactory.createLineBorder (Color.white, 2));

	    le = new LevelEditorFrame(drawingArea);
	    le.setDrawMode(1);
	    
	    content.add(drawingArea, BorderLayout.EAST);
	    f.pack();
	    f.setVisible(true);
		
	}

	public void actionPerformed(ActionEvent evt) {

		String cmd = evt.getActionCommand();
		System.out.println(cmd);
		if(cmd.equals("Muur")){
			le.setDrawMode(1);
		}
		else if(cmd.equals("Plafond")){
			le.setDrawMode(2);
		}
		else if(cmd.equals("Vloer")){
			le.setDrawMode(3);
		}
		else if(cmd.equals("Texture")){
			le.setDrawMode(4);
		}
		
		
	}


	
}