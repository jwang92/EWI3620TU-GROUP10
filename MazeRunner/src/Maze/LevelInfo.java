package Maze;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import Utils.Point3D;


public class LevelInfo {
	
	private Point3D startpos;
	
	public LevelInfo(){
		
		startpos = new Point3D(10, 10, 1);
		
	}
	
	public void setPlayerPos(Point3D newPos){
		startpos = newPos;				
	}
	
	public Point3D getPlayerPos(){
		return startpos;
	}
	
	public void Read(String FileName){
		
		try {
			
			Scanner s = new Scanner(new File(FileName));
			s.useDelimiter(" |; |;\r\n");
			s.next();
			
			String type = s.next();
			
			if(type.equals("PlayerPos:")){
				
				startpos.x = s.nextInt();
				startpos.y = s.nextInt();
				startpos.z = s.nextInt();
				
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void WriteToFile(String FileName) throws IOException{
		
		String r = "LevelInfo;\r\n";
		
		r += "PlayerPos: " + (int) startpos.x + " " + (int) startpos.y + " " + (int) startpos.z + ";\r\n";
		
		r += "End;";
		
		File f = new File(FileName);
		
		// Create new file with the given name if it doesn't exist yet
		if(!f.exists())
		{
			
			f.createNewFile();
			
		}
		
		FileWriter w = new FileWriter(f);
		w.write(r);
		w.close();
		
	}

}
