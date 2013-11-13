import java.io.File;
import java.io.FileNotFoundException;
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
	
	public static void main(String[] args) throws FileNotFoundException {
		
		FloorList f = new FloorList();
		f.Read("Floor.txt");
		
		System.out.println(f.floors.size());
				
	}
	
}
