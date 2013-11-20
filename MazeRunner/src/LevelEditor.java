import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;

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
		
		String xMap = (String)JOptionPane.showInputDialog(
                f,
                "Hoe breed moet de map zijn?",
                "Breedte map",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "10");
		
		String yMap = (String)JOptionPane.showInputDialog(
                f,
                "Hoe lang moet de map zijn?",
                "Lengte map",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "10");
		
		int xMapInt = 10;
		int yMapInt = 10;
		
		xMapInt = Integer.parseInt(xMap);
		yMapInt = Integer.parseInt(yMap);
		
		JPanel controlArea = new JPanel(new GridLayout(5, 1));		
		
		JPanel opties1 = new JPanel(new GridLayout(2, 2));
		opties1.setBorder(BorderFactory.createTitledBorder("Tekenmodus:"));
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
	    
	    //System.out.println();
	    
	    File folder = new File("textures/");
	    File[] tList = folder.listFiles();
	    String[] textures = new String[tList.length-1];
	    
	    int i = 0;
	    for (File file : tList)
	    {
	    	
            if(!file.getName().equals("Thumbs.db"))
            {
            	
            	textures[i] = file.getName();
            	i++;
            }
            	
        }
	    
	    JPanel opties2 = new JPanel(new GridLayout(4, 1));
	    opties2.setBorder(BorderFactory.createTitledBorder("Texture:"));
	    //String[] textures = {"brick.png", "wood.png", "trees.png", "water.png"};
	    
		JComboBox c = new JComboBox(textures);
		c.addActionListener(this);
		c.setActionCommand("textures");
		opties2.add(c);
		
		controlArea.add(opties2);
		
		JPanel opties3 = new JPanel(new GridLayout(4,1));
	    opties3.setBorder(BorderFactory.createTitledBorder("Opslaan/Laden:"));
	    JButton b = new JButton("Sla map op");
	    b.setActionCommand("save");
	    b.addActionListener(this);
	    opties3.add(b);
	    b = new JButton("Laad map");
	    b.setActionCommand("load");
	    b.addActionListener(this);
	    opties3.add(b);
	    
	    controlArea.add(opties3);
	    
	
		content.add(controlArea, BorderLayout.WEST);
		
		// The OpenGL capabilities should be set before initializing the
		// GLCanvas. We use double buffering and hardware acceleration.
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		GLJPanel drawingArea = new GLJPanel(caps);

		drawingArea.setPreferredSize(new Dimension(700, 700));
	    drawingArea.setBorder(BorderFactory.createLineBorder (Color.white, 2));

	    le = new LevelEditorFrame(drawingArea, xMapInt, yMapInt);
	    le.setDrawMode(1);
	    
	    content.add(drawingArea, BorderLayout.EAST);
	    f.pack();
	    f.setVisible(true);
	    
	    f.addWindowListener(new WindowAdapter() {
	    	@Override
	    	  public void windowClosing(WindowEvent e) {
	    	    int confirmed = JOptionPane.showConfirmDialog(null, 
	    	        "Weet je zeker dat je de editor wilt verlaten?", "LevelEditor verlaten",
	    	        JOptionPane.YES_NO_OPTION);

	    	    if (confirmed == JOptionPane.YES_OPTION) {
	    	    	System.exit(0);
	    	    }
	    	  }
	    	});
		
	}

	public void actionPerformed(ActionEvent evt) {

		String cmd = evt.getActionCommand();
		
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
		else if(cmd.equals("save")){
			
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Map opslaan");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retrival = fc.showSaveDialog(null);
			
			if (retrival == JFileChooser.APPROVE_OPTION) {
				try {            
					le.getWallList().WriteToFile(fc.getSelectedFile()+"/Walls.txt");
					le.getRoofList().WriteToFile(fc.getSelectedFile()+"/Roof.txt");
					le.getFloorList().WriteToFile(fc.getSelectedFile()+"/Floor.txt");
					
					FileWriter fw = new FileWriter(fc.getSelectedFile()+"/World.txt");
					fw.write(le.getWorldFileFormat());
					fw.close();
					    
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		    }
			
		}
		else if(cmd.equals("load")){
			
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Map openen");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retrival = fc.showSaveDialog(null);
			
			if (retrival == JFileChooser.APPROVE_OPTION) {
				try {     
					
					le.loadFromFolder(fc.getSelectedFile().getPath());
					    
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		    }
			
		}
		else if(cmd.equals("textures"))
		{
			JComboBox type = (JComboBox) evt.getSource();
			le.setTexture((String)type.getSelectedItem());
			
		}
		
	}


	
}