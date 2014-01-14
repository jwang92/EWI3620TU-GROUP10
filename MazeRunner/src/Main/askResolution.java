package Main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class askResolution  implements ActionListener {

	private JFrame f;
	private JPanel panel;
	
	private String[] sizes;
	private int screenWidth, screenHeight;
	private boolean fullscreen;
	
	protected static MainClass mainclass;
	
	public askResolution(){
		f = new JFrame( "Medieval Invasion (resolution)" );
		f.setSize( 500, 500 );
		
		Container content = f.getContentPane();
		
		panel = new JPanel();
		
//		panel.setLayout(null);
	    
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    addDropbox();
	    
	    addButton();
	    
	    panel.setPreferredSize(new Dimension(300, 50));
	    
	    content.add(panel);
	    
//	    // Display the window. 
	    f.setLocationRelativeTo(null); 
	    f.pack();
	    f.setVisible(true);

	}
	
	public void addDropbox(){
		JPanel dropdown = new JPanel(new GridLayout(1, 1));
		
		sizes = new String[4];
		sizes[0] = "600x600";
		sizes[1] = "800x800";
		sizes[2] = "1200x1000";
		sizes[3] = "Full screen";
		
		setScreensize(sizes[0]);
		
		JComboBox<String> c = new JComboBox<String>(sizes);
		c.addActionListener(this);
		c.setActionCommand("screensize");
		dropdown.add(c);

		panel.add(dropdown);
	}
	
	public void addButton(){
	    JButton b = new JButton("START GAME");
	    b.setActionCommand("start");
	    b.addActionListener(this);

	    panel.add(b);

	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		
		if(cmd.equals("screensize")){
			JComboBox type = (JComboBox) evt.getSource();
			setScreensize( (String) type.getSelectedItem() );
		}
		if(cmd.equals("start")){
			mainclass = new MainClass(screenWidth, screenHeight, fullscreen);
	    	f.dispose();
		}
		
	}
	
	public void setScreensize( String size){
		if(size.equals(sizes[0])){
			screenWidth = 600;
			screenHeight = 600;
			fullscreen = false;
		}
		else if(size.equals(sizes[1])){
			screenWidth = 800;
			screenHeight = 800;
			fullscreen = false;
		}
		else if(size.equals(sizes[2])){
			screenWidth = 1200;
			screenHeight = 1000;
			fullscreen = false;
		}
		else if(size.equals(sizes[3])){
			fullscreen = true;
		}
	}
}
