import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WallList {

	ArrayList<Wall> walls;
	
	public WallList()
	{
		
		walls = new ArrayList<Wall>();
		
	}
	
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
	
	public static void main(String[] args) throws FileNotFoundException {
		
		WallList w = new WallList();
		w.Read("Walls.txt");
		
	}
	
}
