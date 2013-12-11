import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLJPanel;
import javax.swing.*;

public class LevelEditor implements ActionListener{
	
	private LevelEditorFrame le;
	private String savefolder = "savefiles/kasteel";
	private String defaultLoadFolder = "savefiles";
	private int verdiepingNummer = 1;
	private int numberOfStoreys = 4;
	private JPanel opties1;
	private JPanel opties2;
	private JPanel opties2b;
	private JPanel opties2c;
	private JPanel opties3;
	private JPanel opties4;
	private JPanel controlArea;
	private JComboBox cOptie4;
	private JFrame f;
	
	
	public static void main(String[] args) {
		new LevelEditor();
	}
	  
	public LevelEditor(){
		
		f = new JFrame("Testert");
		f.setSize(800, 700);
		
		Container content = f.getContentPane();
		
//		String xMap = (String)JOptionPane.showInputDialog(
//				f,
//                "Hoe breed moet de map zijn?",
//                "Breedte map",
//                JOptionPane.PLAIN_MESSAGE,
//                null,
//                null,
//                "10");
//		
//		String yMap = (String)JOptionPane.showInputDialog(
//                f,
//                "Hoe lang moet de map zijn?",
//                "Lengte map",
//                JOptionPane.PLAIN_MESSAGE,
//                null,
//                null,
//                "10");
//		
//		int xMapInt = 10;
//		int yMapInt = 10;
//		
//		xMapInt = Integer.parseInt(xMap);
//		yMapInt = Integer.parseInt(yMap);
	
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints cons = new GridBagConstraints();
        
        cons.fill = GridBagConstraints.BOTH;
        
	    content.setLayout(gridBag);
	    
	    
        
		controlArea = new JPanel(gridBag);	
		
		
		
		opties1 = new JPanel(new GridLayout(1, 2));
		opties1.setBorder(BorderFactory.createTitledBorder("Modus:"));
	    ButtonGroup bg1 = new ButtonGroup();
		JRadioButton  option1a = new JRadioButton("Tekenen");
	    bg1.add(option1a);
	    option1a.setSelected(true);
	    opties1.add(option1a);
	    JRadioButton option2a = new JRadioButton("Gummen");
	    bg1.add(option2a);
	    opties1.add(option2a);
	    
	    cons.ipady = 0;
        cons.ipadx = 0;
        cons.weighty = 0.1;
        cons.gridx = 1;
        cons.gridy = 1;
        gridBag.setConstraints(opties1, cons);
	    controlArea.add(opties1);
		
	    option1a.setActionCommand("Tekenen");
	    option1a.addActionListener(this);
	    
	    option2a.setActionCommand("Gummen");
	    option2a.addActionListener(this);
	    
		
		opties1 = new JPanel(new GridLayout(3, 2));
		opties1.setBorder(BorderFactory.createTitledBorder("Wat:"));
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
	    JRadioButton option5 = new JRadioButton("Object");
	    bg.add(option5);
	    opties1.add(option5);
	    JRadioButton option6 = new JRadioButton("LevelInfo");
	    bg.add(option6);
	    opties1.add(option6);
	    JRadioButton option7 = new JRadioButton("Upgrades");
	    bg.add(option7);
	    opties1.add(option7);
	    
	    cons.ipady = 0;
        cons.ipadx = 0;
        cons.weighty = 0.1;
        cons.gridx = 1;
        cons.gridy = 2;
        gridBag.setConstraints(opties1, cons);
	    controlArea.add(opties1);
	    
	    option1.setActionCommand("Muur");
	    option1.addActionListener(this);
	    
	    option2.setActionCommand("Plafond");
	    option2.addActionListener(this);
	    
	    option3.setActionCommand("Vloer");
	    option3.addActionListener(this);
	    
	    option5.setActionCommand("Object");
	    option5.addActionListener(this);
	    
	    option6.setActionCommand("LevelInfo");
	    option6.addActionListener(this);
	    
	    option7.setActionCommand("Pickups");
	    option7.addActionListener(this);
	    
	    //System.out.println();
	    
	    File folder = new File("textures/");
	    File[] tList = folder.listFiles();
	    int numberOfTextures = tList.length;
		for(int j = 0; j<tList.length;j++){
		    if(tList[j].getName().equals("Thumbs.db")){
		    	numberOfTextures -= 1;
		    }  
		}
		String[] textures = new String[numberOfTextures];
	    int i = 0;
	    for (File file : tList)
	    {
	    	
            if(!file.getName().equals("Thumbs.db"))
            {
            	
            	textures[i] = file.getName();
            	i++;
            }
            	
        }
	    
	    opties2 = new JPanel(new GridLayout(1, 1));
	    opties2.setBorder(BorderFactory.createTitledBorder("Texture:"));
	    //String[] textures = {"brick.png", "wood.png", "trees.png", "water.png"};
	    
		JComboBox c = new JComboBox(textures);
		c.addActionListener(this);
		c.setActionCommand("textures");
		opties2.add(c);
		
		cons.ipady = 0;
        cons.ipadx = 0;
        cons.weighty = 0.1;
        cons.gridx = 1;
        cons.gridy = 3;
        gridBag.setConstraints(opties2, cons);
		
		controlArea.add(opties2);
		
		opties2b = new JPanel(new GridLayout(1, 1));
	    opties2b.setBorder(BorderFactory.createTitledBorder("Level info:"));
	    
	    String[] opties = {"Startpositie"};
	    
		c = new JComboBox(opties);
		c.addActionListener(this);
		c.setActionCommand("LevelInfo");
		opties2b.add(c);
		
		cons.ipady = 0;
        cons.ipadx = 0;
        cons.weighty = 0.1;
        cons.gridx = 1;
        cons.gridy = 4;
        gridBag.setConstraints(opties2b, cons);
		
		controlArea.add(opties2b);
		
		opties2c = new JPanel(new GridLayout(1, 1));
	    opties2c.setBorder(BorderFactory.createTitledBorder("Pickups:"));
	    
	    String[] opties2 = {"Speed", "Zwaard", "Health"};
	    
		c = new JComboBox(opties2);
		c.addActionListener(this);
		c.setActionCommand("setPickups");
		opties2c.add(c);
		
		cons.ipady = 0;
        cons.ipadx = 0;
        cons.weighty = 0.1;
        cons.gridx = 1;
        cons.gridy = 5;
        gridBag.setConstraints(opties2c, cons);
		
		controlArea.add(opties2c);
		
		opties3 = new JPanel(new GridLayout(4,1));
	    opties3.setBorder(BorderFactory.createTitledBorder("Opslaan/Laden:"));
	    JButton b = new JButton("Sla map op");
	    b.setActionCommand("save");
	    b.addActionListener(this);
	    opties3.add(b);
	    b = new JButton("Laad map");
	    b.setActionCommand("load");
	    b.addActionListener(this);
	    opties3.add(b);
	    b = new JButton("Nieuwe map");
	    b.setActionCommand("newMap");
	    b.addActionListener(this);
	    opties3.add(b);
	    
	    cons.ipady = 0;
        cons.ipadx = 0;
        cons.weighty = 0.3;
        cons.gridx = 1;
        cons.gridy = 6;
        gridBag.setConstraints(opties3, cons);
	    controlArea.add(opties3);
	    
	    opties4 = new JPanel(new GridLayout(4, 1));
	    opties4.setBorder(BorderFactory.createTitledBorder("Verdieping:"));
	    b = new JButton("Nieuwe verdieping");
	    b.setActionCommand("newStorey");
	    b.addActionListener(this);
	    opties4.add(b);
	    createVerdiepingList(savefolder);
	    opties4.add(cOptie4);
	    
	    cons.ipady = 0;
        cons.ipadx = 0;
        cons.weighty = 1;
        cons.gridx = 1;
        cons.gridy = 7;
        gridBag.setConstraints(opties4, cons);
	    controlArea.add(opties4);
		
	    gridBag = new GridBagLayout();
        cons = new GridBagConstraints();
        
        cons.fill = GridBagConstraints.BOTH;
        
	    content.setLayout(gridBag);
	    cons.ipady = 0;
        cons.ipadx = 0;
        cons.weighty = 1.0;
        cons.gridx = 0;
        cons.gridy = 0;
	    
        gridBag.setConstraints(controlArea, cons);
        
	    content.add(controlArea);
		
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
	    try {
			le.loadFromFolder(savefolder);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    le.initGrid();
	    cons.ipady = 100;
        cons.ipadx = 100;
        cons.weightx = 1.0;
        cons.weighty = 1.0;
        cons.gridx = 150;
        cons.gridy = 0;
	    gridBag.setConstraints(drawingArea, cons);
	    content.add(drawingArea);
	    f.pack();
	    f.setVisible(true);
	    f.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);

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
		else if(cmd.equals("Tekenen")){
			le.setMode(1);
		}
		else if(cmd.equals("Gummen")){
			le.setMode(2);
		}
		else if(cmd.equals("Object")){
			le.setDrawMode(5);
			le.setWhatObject(1);
		}
		else if(cmd.equals("LevelInfo")){
			le.setDrawMode(6);
			le.setWhatLevelinfo(1);
		}
		else if(cmd.equals("Pickups")){
			le.setDrawMode(7);
		}
		else if(cmd.equals("setPickups")){
			JComboBox type = (JComboBox) evt.getSource();
			String pu =(String)type.getSelectedItem();
			
			int whatPickup = 0;
			if(pu.equals("Speed"))
				whatPickup = 1;
			else if(pu.equals("Zwaard"))
				whatPickup = 2;
			else if(pu.equals("Health"))
				whatPickup = 3;
			
			le.setWhatPickup(whatPickup);
		}
		else if(cmd.equals("save")){
			
			JFileChooser fc = new JFileChooser(defaultLoadFolder);
			fc.setDialogTitle("Map opslaan");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retrival = fc.showSaveDialog(null);
			
			if (retrival == JFileChooser.APPROVE_OPTION) {
				try {            	    
					saveToFile(fc.getSelectedFile().getPath());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		    }
			
		}
		else if(cmd.equals("load")){
			
			JFileChooser fc = new JFileChooser(defaultLoadFolder);
			fc.setDialogTitle("Map openen");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retrival = fc.showOpenDialog(null);
			
			if (retrival == JFileChooser.APPROVE_OPTION) {
				try {
					savefolder = fc.getSelectedFile().getPath();
					verdiepingNummer = 1;
					changeVerdieping(verdiepingNummer);
					opties4.remove(cOptie4);
					createVerdiepingList(savefolder);
					opties4.add(cOptie4);
					controlArea.updateUI();
					boolean loaded = le.loadFromFolder(savefolder);	
					if(!loaded){
						System.out.println("Loading failed, probably the wrong file format");
						opties4.remove(cOptie4);
						controlArea.updateUI();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		    }
			
		}
		else if(cmd.equals("newMap")){
			
			JFileChooser fc = new JFileChooser(defaultLoadFolder);
			fc.setDialogTitle("Nieuwe map");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retrival = fc.showSaveDialog(null);
			
			if (retrival == JFileChooser.APPROVE_OPTION) {
				try {
					savefolder = fc.getSelectedFile().getPath();
					createNewSaveMap(savefolder);
					le.initGrid();
					verdiepingNummer = 1;
					changeVerdieping(verdiepingNummer);
					opties4.remove(cOptie4);
					createVerdiepingList(le.getStoreys());
					opties4.add(cOptie4);
					controlArea.updateUI();
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
		
		else if(cmd.equals("newStorey")){
			ArrayList<Storey> storeys = le.getStoreys();
			Storey s = newStorey(le.getStoreys().get(le.getStoreys().size()-1).getRoofHeight());
			storeys.add(s);
			le.setStoreys(storeys);
			numberOfStoreys += 1;
			File f = new File(savefolder + "/Floor " + (numberOfStoreys));
			f.mkdirs();
			
			le.initGrid();
			verdiepingNummer = numberOfStoreys;
			changeVerdieping(verdiepingNummer);
			opties4.remove(cOptie4);
			createVerdiepingList(le.getStoreys());
			cOptie4.setSelectedIndex(verdiepingNummer-1);
			opties4.add(cOptie4);
			controlArea.updateUI();
		}
		
		else if(cmd.equals("verdieping"))
		{
			JComboBox type = (JComboBox) evt.getSource();
			String verdiepingtest =(String)type.getSelectedItem();
			String verdiepingCheck = verdiepingtest.substring(0,verdiepingtest.length() - 2);
			String newVerdiepingNummer = verdiepingtest.substring(verdiepingtest.length() - 1);
			if(verdiepingCheck.equals("Verdieping")){
				if(verdiepingNummer != Integer.parseInt(newVerdiepingNummer)){
					changeVerdieping(Integer.parseInt(newVerdiepingNummer));
					verdiepingNummer = Integer.parseInt(newVerdiepingNummer);
				}
				
			}
		}
	}

	private void changeVerdieping(int verdiepingChange){
		le.changeStorey(verdiepingChange);
	}
	
	private void saveToFile(String selectedFolder) throws IOException{
		ArrayList<Storey> storeys = le.getStoreys();
		for(int i = 0;i<numberOfStoreys - 1;i++){
			File f = new File(selectedFolder + "/Floor " + (i+1));
			f.mkdirs();
			storeys.get(i).WriteToFile(selectedFolder + "/Floor " + (i+1));
		}	
		
		// Levelinfo wegschrijven
		le.getLevelInfo().WriteToFile(selectedFolder + "/LevelInfo.txt");
		
		savefolder = selectedFolder;
	}
	
	private void createVerdiepingList(String loadfolder){
		File folder = new File(loadfolder);
		File[] tList = folder.listFiles();
		
		numberOfStoreys = tList.length - 1;

		for(int j = 0; j < tList.length;j++){
		    if(tList[j].getName().equals("Thumbs.db")){
		    	numberOfStoreys -= 1;
		    }  
		}
		String[] verdiepingList = new String[numberOfStoreys];
		


		for(int j =0;j<numberOfStoreys;j++){
			String verdieping = "Verdieping " + (j+1);
			verdiepingList[j] = verdieping;
		}	
		cOptie4 = new JComboBox(verdiepingList);
		cOptie4.addActionListener(this);
		cOptie4.setActionCommand("verdieping");
	}
	
	private void createVerdiepingList(ArrayList<Storey> storeys){
		numberOfStoreys = storeys.size();
		String[] verdiepingList = new String[numberOfStoreys];
		for(int j =0;j<numberOfStoreys;j++){
			String verdieping = "Verdieping " + (j+1);
			verdiepingList[j] = verdieping;
		}	
		cOptie4 = new JComboBox(verdiepingList);
		cOptie4.addActionListener(this);
		cOptie4.setActionCommand("verdieping");
	}
	
	private void createNewSaveMap(String selectedFolder) throws IOException{
		ArrayList<Storey> storeys = new ArrayList<Storey>();
		Storey s = newStorey(0);
		storeys.add(s);
		le.setStoreys(storeys);
		File f = new File(selectedFolder + "/Floor " + (1));
		f.mkdirs();
	}
	
	private Storey newStorey(int floorHeight){
		String xMap = (String)JOptionPane.showInputDialog(
				f,
                "Hoe breed moet de verdieping zijn?",
                "Breedte map",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "10");
		
		String yMap = (String)JOptionPane.showInputDialog(
                f,
                "Hoe lang moet de verdieping zijn?",
                "Lengte map",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "10");
		String height = (String)JOptionPane.showInputDialog(
				f,
                "Hoe hoog moet de verdieping zijn?",
                "Breedte map",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "5");

		
		int xMapInt = 10;
		int yMapInt = 10;
		int heightInt = 5;
		
		xMapInt = Integer.parseInt(xMap);
		yMapInt = Integer.parseInt(yMap);
		heightInt = Integer.parseInt(height);
		
		return new Storey(xMapInt,yMapInt,floorHeight,heightInt);	
	}
	
}