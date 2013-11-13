import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class RoofList {

	ArrayList<Roof> roofs;
	
	public RoofList()
	{
		
		roofs = new ArrayList<Roof>();
		
	}
	
	public void Read(String FileName) throws FileNotFoundException
	{
		
		Scanner s = new Scanner(new File(FileName));
		
		String tempNumber = s.nextLine();
		
		int numRoofs = Integer.parseInt(tempNumber.substring(15, tempNumber.length()-1));

		for(int i = 0; i < numRoofs; i++)
		{
			
			roofs.add(Roof.Read(s));
			
		}
				
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		RoofList r = new RoofList();
		r.Read("Roof.txt");
			
	}
	
}
