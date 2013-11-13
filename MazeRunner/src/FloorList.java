import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FloorList {

	ArrayList<Floor> floors;
	
	public FloorList()
	{
		
		floors = new ArrayList<Floor>();
		
	}
	
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
	
	public void addFloor(Floor f)
	{
		
		floors.add(f);
		
	}

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
	
	public static void main(String[] args) throws IOException {
		
		FloorList f = new FloorList();
		f.Read("Floor.txt");
		
		f.WriteToFile("asdasd.txt");
				
	}
	
}
