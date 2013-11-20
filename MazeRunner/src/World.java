import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class World {
	
	private int xSize;
	private int ySize;
	
	public void Read(String FileName) throws FileNotFoundException
	{
		
		Scanner s = new Scanner(new File(FileName));
		
		s.next();
		
		xSize = s.nextInt();
		
		String tempNumber = s.next();
		ySize = Integer.parseInt(tempNumber.substring(0, tempNumber.length()-1));
	
	
	}
	
	public int getSizeX(){
		
		return xSize;
		
	}

	public int getSizeY(){
		
		return ySize;
		
	}
	
	public void setSizeX(int x){
		xSize = x;
	}
	
	public void setSizeY(int y){
		ySize = y;
	}
	
	public void WriteToFile(String FileName) throws IOException{
		
		String r = "Size: " + xSize + " " + ySize + ";\r\nEnd;";

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
