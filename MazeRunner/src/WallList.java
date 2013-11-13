import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class WallList {

	ArrayList<Wall> walls;
	
	/**
	 * Creates the walllist as ArrayList
	 */
	public WallList()
	{
		
		walls = new ArrayList<Wall>();
		
	}
	
	/**
	 * Reads a file to a walllist
	 * @param FileName The filename with walls
	 * @throws FileNotFoundException
	 */
	public void Read(String FileName) throws FileNotFoundException
	{
		
		Scanner s = new Scanner(new File(FileName));
		
		String tempNumber = s.nextLine();
		
		int numWalls = Integer.parseInt(tempNumber.substring(15, tempNumber.length()-1));

		for(int i = 0; i < numWalls; i++)
		{
			
			walls.add(Wall.Read(s));
			
		}
				
	}
	
	/**
	 * Adds a wall to the list
	 * @param w The wall to be added
	 */
	public void addWall(Wall w)
	{
		
		walls.add(w);
		
	}
	
	/**
	 * Writes the complete list to a file
	 * @param FileName The name of the file
	 * @throws IOException
	 */
	public void WriteToFile(String FileName) throws IOException{
		
		String r = "NumberOfWalls: " + walls.size() + ";\r\n";
		
		for(int i = 0; i < walls.size(); i++)
		{
			
			r += walls.get(i).toFileFormat();			
			
		}
		
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
