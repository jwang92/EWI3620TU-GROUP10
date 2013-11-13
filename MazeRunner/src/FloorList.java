import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FloorList {

	ArrayList<Floor> floors;
	
	/**
	 * Creates a new floorlist as ArrayList
	 */
	public FloorList()
	{
		
		floors = new ArrayList<Floor>();
		
	}
	
	/**
	 * Reads a file and creates a floorlist from the file
	 * @param FileName The file to be read
	 * @throws FileNotFoundException
	 */
	public void Read(String FileName) throws FileNotFoundException
	{
		
		Scanner s = new Scanner(new File(FileName));
		
		String tempNumber = s.nextLine();
		
		int numfloors = Integer.parseInt(tempNumber.substring(16, tempNumber.length()-1));

		for(int i = 0; i < numfloors; i++)
		{
			
			floors.add(Floor.Read(s));
			
		}
				
	}
	
	/**
	 * Adds a Floor to the ArrayList
	 * @param f The floor to be added
	 */
	public void addFloor(Floor f)
	{
		
		floors.add(f);
		
	}

	/**
	 * Writes the complete list to a file
	 * @param FileName The name of the file
	 * @throws IOException
	 */
	public void WriteToFile(String FileName) throws IOException{
		
		String r = "NumberOfFloors: " + floors.size() + ";\r\n";
		
		for(int i = 0; i < floors.size(); i++)
		{
			
			r += floors.get(i).toFileFormat();			
			
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
