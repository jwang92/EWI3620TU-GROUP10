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
	
	//Savefolders
	private String savefolder = "savefiles/kasteel";
	private String defaultLoadFolder = "savefiles";
	private String nextLevel = "";
	
	//Storeys
	private int verdiepingNummer = 1;
	private int numberOfStoreys = 4;
	
	//Components
	private JPanel opties1;
	private JPanel opties2;
	private JPanel opties3;
	private JPanel opties4;
	private JPanel opties5;
	private JPanel opties6a;
	private JPanel opties6b;
	private JPanel opties6c;
	private JPanel empty;
	private JPanel controlArea;
	private JComboBox cOptie4;
	
	//Frame
	private final JFrame f;
	
	//Layout
	GridBagLayout gridBag = new GridBagLayout();
	GridBagConstraints cons = new GridBagConstraints();
	
	public static void main(String[] args) {
		new LevelEditor();
	}
	  
	public LevelEditor(){
		
		f = new JFrame("Testert");
		f.setSize(800, 700);
		
		Container content = f.getContentPane();
	
		gridBag = new GridBagLayout();
		cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.BOTH;
	    content.setLayout(gridBag);
		controlArea = new JPanel(gridBag);	
		
		//Create the componenents
	    createComponentOpties1();
	    createComponentOpties2();
	    createComponentOpties3();
	    createComponentOpties4();
	    createComponentOpties5();
	    createComponentOpties6a();
	    createComponentOpties6b();
	    createComponentOpties6c();
	    createComponentEmpty();
        
        
        GridBagLayout gridBag2 = new GridBagLayout();
        cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.BOTH;
	    content.setLayout(gridBag2);
	    cons.ipady = 0;
        cons.ipadx = 0;
        cons.weighty = 1.0;
        cons.gridx = 0;
        cons.gridy = 0;
        gridBag2.setConstraints(controlArea, cons);
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
			changeOptie5(0);
		}
		else if(cmd.equals("Plafond")){
			le.setDrawMode(2);
			changeOptie5(0);
		}
		else if(cmd.equals("Vloer")){
			le.setDrawMode(3);
			changeOptie5(0);
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
			changeOptie5(3);
		}
		else if(cmd.equals("LevelInfo")){
			le.setDrawMode(6);
			le.setWhatLevelinfo(1);
			changeOptie5(1);
		}
		else if(cmd.equals("Pickups")){
			le.setDrawMode(7);
			changeOptie5(2);
		}
		else if(cmd.equals("setPickups")){
			JComboBox type = (JComboBox) evt.getSource();
			setPickups(type);
		}
		else if(cmd.equals("setObjects")){
			JComboBox type = (JComboBox) evt.getSource();
			setObjects(type);
		}
		else if(cmd.equals("save")){
			save();
		}
		else if(cmd.equals("load")){
			load();		
		}
		else if(cmd.equals("newMap")){
			newMap();			
		}
		
		else if(cmd.equals("textures"))
		{
			JComboBox type = (JComboBox) evt.getSource();
			le.setTexture((String)type.getSelectedItem());
		}
		
		else if(cmd.equals("newStorey")){
			newStorey();
		}
		
		else if(cmd.equals("verdieping"))
		{
			JComboBox type = (JComboBox) evt.getSource();
			verdieping(type);
		}
		else if(cmd.equals("nextLevel"))
		{
			nextLevel();
		}
	}
	
	private void setPickups(JComboBox type){
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
	
	private void setObjects(JComboBox type){
		String pu =(String)type.getSelectedItem();
		
		int whatObject = 0;
		if(pu.equals("Ramp"))
			whatObject = 1;
		else if(pu.equals("Predator"))
			whatObject = 2;
		else if(pu.equals("Lion"))
			whatObject = 3;
		else if(pu.equals("Exit"))
			whatObject = 4;
		
		le.setWhatObject(whatObject);
	}
	
	private void save(){
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
	
	private void load(){
		JFileChooser fc = new JFileChooser(defaultLoadFolder);
		fc.setDialogTitle("Map openen");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retrival = fc.showOpenDialog(null);
		
		if (retrival == JFileChooser.APPROVE_OPTION) {
			try {
				savefolder = fc.getSelectedFile().getPath();
				verdiepingNummer = 1;
				changeVerdieping(verdiepingNummer);
				opties5.remove(cOptie4);
				createVerdiepingList(savefolder);
				opties5.add(cOptie4);
				controlArea.updateUI();
				boolean loaded = le.loadFromFolder(savefolder);	
				if(!loaded){
					System.out.println("Loading failed, probably the wrong file format");
					opties5.remove(cOptie4);
					controlArea.updateUI();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	    }
	}
	
	private void newMap(){
		JFileChooser fc = new JFileChooser(defaultLoadFolder);
		fc.setDialogTitle("Nieuwe map");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retrival = fc.showOpenDialog(null);
		
		if (retrival == JFileChooser.APPROVE_OPTION) {
			try {
				savefolder = fc.getSelectedFile().getPath();
				createNewSaveMap(savefolder);
				le.initGrid();
				verdiepingNummer = 1;
				changeVerdieping(verdiepingNummer);
				opties5.remove(cOptie4);
				createVerdiepingList(le.getStoreys());
				opties5.add(cOptie4);
				controlArea.updateUI();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	    }
	}
	
	private void newStorey(){
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
		opties5.remove(cOptie4);
		createVerdiepingList(le.getStoreys());
		cOptie4.setSelectedIndex(verdiepingNummer-1);
		opties5.add(cOptie4);
		controlArea.updateUI();
	}
	
	private void verdieping(JComboBox type){
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
	
	private void changeVerdieping(int verdiepingChange){
		le.changeStorey(verdiepingChange);
	}
	
	private void saveToFile(String selectedFolder) throws IOException{
		ArrayList<Storey> storeys = le.getStoreys();
		for(int i = 0;i<numberOfStoreys;i++){
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
		
		numberOfStoreys = tList.length;

		for(int j = 0; j < tList.length;j++){
		    if(tList[j].getName().equals("Thumbs.db")){
		    	numberOfStoreys -= 1;
		    }  
		    else if(tList[j].getName().equals("LevelInfo.txt")){
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
	
	private void nextLevel(){
		JFileChooser fc = new JFileChooser(defaultLoadFolder);
		fc.setDialogTitle("Next Level");
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retrival = fc.showDialog(null,"Select");
		
		if (retrival == JFileChooser.APPROVE_OPTION) {
			try {
				String tempString = fc.getSelectedFile().getPath();
				int tempLocation = tempString.indexOf("savefiles");
				if(tempLocation >= 0){
					nextLevel = "savefiles/" + tempString.substring(tempLocation+10);
					le.setNextLevel(nextLevel);		
				}
				else{
					JOptionPane.showConfirmDialog(null, "Selecteer een level uit de map savefiles", "Error",JOptionPane.OK_CANCEL_OPTION);
					le.setNextLevel("");
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	    }
	}
	
	private void changeOptie5(int type){
		switch(type){
		case 0:
			opties6a.setVisible(true);
			opties6b.setVisible(false);
			opties6c.setVisible(false);
			opties6a.setEnabled(false);
			opties6b.setEnabled(false);
			opties6c.setEnabled(false);
			break;
		case 1:
			opties6a.setVisible(true);
			opties6b.setVisible(false);
			opties6c.setVisible(false);
			opties6a.setEnabled(true);
			opties6b.setEnabled(false);
			opties6c.setEnabled(false);
			break;
		case 2:
			opties6a.setVisible(false);
			opties6b.setVisible(true);
			opties6c.setVisible(false);
			opties6a.setEnabled(false);
			opties6b.setEnabled(true);
			opties6c.setEnabled(false);
			break;
		case 3:
			opties6a.setVisible(false);
			opties6b.setVisible(false);
			opties6c.setVisible(true);
			opties6a.setEnabled(false);
			opties6b.setEnabled(false);
			opties6c.setEnabled(true);
			break;
		default:
			opties6a.setVisible(true);
			opties6b.setVisible(false);
			opties6c.setVisible(false);
			opties6a.setEnabled(false);
			opties6b.setEnabled(false);
			opties6c.setEnabled(false);
			break;
		}
		controlArea.validate();
		controlArea.repaint();
	}
	
	private void createComponentOpties1(){

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
	    
	    setConstraints(opties1,0,0,0.1,1,1);
        gridBag.setConstraints(opties1, cons);
	    controlArea.add(opties1);
		
	    option1a.setActionCommand("Tekenen");
	    option1a.addActionListener(this);
	    
	    option2a.setActionCommand("Gummen");
	    option2a.addActionListener(this);
	}
	
	private void createComponentOpties2(){
		opties2 = new JPanel(new GridLayout(3, 2));
		opties2.setBorder(BorderFactory.createTitledBorder("Wat:"));
	    ButtonGroup bg = new ButtonGroup();
		JRadioButton  option2a = new JRadioButton("Muur");
	    bg.add(option2a);
	    option2a.setSelected(true);
	    opties2.add(option2a);
	    JRadioButton option2b = new JRadioButton("Plafond");
	    bg.add(option2b);
	    opties2.add(option2b);
	    JRadioButton option2c = new JRadioButton("Vloer");
	    bg.add(option2c);
	    opties2.add(option2c);
	    JRadioButton option2d = new JRadioButton("Object");
	    bg.add(option2d);
	    opties2.add(option2d);
	    JRadioButton option2e = new JRadioButton("LevelInfo");
	    bg.add(option2e);
	    opties2.add(option2e);
	    JRadioButton option2f = new JRadioButton("Upgrades");
	    bg.add(option2f);
	    opties2.add(option2f);
	    
	    setConstraints(opties2,0,0,0.5,1,2);
	    controlArea.add(opties2);
	    
	    option2a.setActionCommand("Muur");
	    option2a.addActionListener(this);
	    
	    option2b.setActionCommand("Plafond");
	    option2b.addActionListener(this);
	    
	    option2c.setActionCommand("Vloer");
	    option2c.addActionListener(this);
	    
	    option2d.setActionCommand("Object");
	    option2d.addActionListener(this);
	    
	    option2e.setActionCommand("LevelInfo");
	    option2e.addActionListener(this);
	    
	    option2f.setActionCommand("Pickups");
	    option2f.addActionListener(this);
	}
	
	private void createComponentOpties3(){
		String[] textures = loadTextureNames();
	    opties3 = new JPanel(new GridLayout(1, 1));
	    opties3.setBorder(BorderFactory.createTitledBorder("Texture:"));
	    
		JComboBox c = new JComboBox(textures);
		c.addActionListener(this);
		c.setActionCommand("textures");
		opties3.add(c);
		
		setConstraints(opties3,0,0,0.1,1,3);
	
		controlArea.add(opties3);
	}
	
	private String[] loadTextureNames(){
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
	    return textures;
	}
	
	private void createComponentOpties4(){
		opties4 = new JPanel(new GridLayout(3,1));
	    opties4.setBorder(BorderFactory.createTitledBorder("Opslaan/Laden:"));
	    
	    JButton b = new JButton("Sla map op");
	    b.setActionCommand("save");
	    b.addActionListener(this);
	    opties4.add(b);
	    
	    b = new JButton("Laad map");
	    b.setActionCommand("load");
	    b.addActionListener(this);
	    opties4.add(b);
	    
	    b = new JButton("Nieuwe map");
	    b.setActionCommand("newMap");
	    b.addActionListener(this);
	    opties4.add(b);
	    
	    setConstraints(opties4,0,0,0.3,1,4);
	    
	    controlArea.add(opties4);
	}
	
	private void createComponentOpties5(){
		opties5 = new JPanel(new GridLayout(2, 1));
	    opties5.setBorder(BorderFactory.createTitledBorder("Verdieping:"));
	    
	    JButton b = new JButton("Nieuwe verdieping");
	    b.setActionCommand("newStorey");
	    b.addActionListener(this);
	    opties5.add(b);
	    
	    createVerdiepingList(savefolder);
	    opties5.add(cOptie4);
	    
	    setConstraints(opties5,0,0,0.2,1,5);
        
	    controlArea.add(opties5);
	}
	
	private void createComponentOpties6a(){
		opties6a = new JPanel(new GridLayout(1, 1));
	    opties6a.setBorder(BorderFactory.createTitledBorder("Level info:"));
	    
	    String[] opties5aString = {"Startpositie"};
	    
	    JComboBox c = new JComboBox(opties5aString);
		c.addActionListener(this);
		c.setActionCommand("LevelInfo");
		opties6a.add(c);
		
		setConstraints(opties6a,0,0,0.1,1,6);
        
		opties6a.setVisible(true);
		opties6a.setEnabled(false);
		
		controlArea.add(opties6a);
	}
	
	private void createComponentOpties6b(){
		opties6b = new JPanel(new GridLayout(1, 1));
	    opties6b.setBorder(BorderFactory.createTitledBorder("Pickups:"));
	    
	    String[] opties5bString = {"Speed", "Zwaard", "Health"};
	    
	    JComboBox c = new JComboBox(opties5bString);
		c.addActionListener(this);
		c.setActionCommand("setPickups");
		opties6b.add(c);
		
		setConstraints(opties6b,0,0,0.1,1,6);
		
        opties6b.setVisible(false);
        opties6b.setEnabled(false);
        
		controlArea.add(opties6b);
	}
	
	private void createComponentOpties6c(){
		opties6c = new JPanel(new GridLayout(2, 1));
	    opties6c.setBorder(BorderFactory.createTitledBorder("Objects:"));
	    
	    String[] opties5cString = {"Ramp", "Predator", "Lion", "Exit"};
	    
	    JComboBox c = new JComboBox(opties5cString);
		c.addActionListener(this);
		c.setActionCommand("setObjects");
		opties6c.add(c);
		
	    JButton b = new JButton("Choose next Level");
	    b.setActionCommand("nextLevel");
	    b.addActionListener(this);
	    opties6c.add(b);
		
		setConstraints(opties6c,0,0,0.2,1,6);
		
        opties6c.setVisible(false);
        opties6c.setEnabled(false);
        
		controlArea.add(opties6c);
	}
	
	private void createComponentEmpty(){
		empty = new JPanel(new GridLayout(1, 1));
        empty.setBorder(BorderFactory.createTitledBorder(""));
        
        setConstraints(empty,0,0,6,1,7);
        
        controlArea.add(empty);
	}
	
	private void setConstraints(JPanel optie, int ipadX,int ipadY,double weightY, int gridX, int gridY){
	    cons.ipady = ipadX;
        cons.ipadx = ipadY;
        cons.weighty = weightY;
        cons.gridx = gridX;
        cons.gridy = gridY;
        gridBag.setConstraints(optie, cons);
	}
	
}