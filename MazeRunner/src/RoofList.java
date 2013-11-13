import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public void addRoof(Roof r)
	{
		
		roofs.add(r);
		
	}
	
	public void WriteToFile(String FileName) throws IOException{
		
		String r = "NumberOfRoofs: " + roofs.size() + ";\r\n";
		
		for(int i = 0; i < roofs.size(); i++)
		{
			
			r += roofs.get(i).toFileFormat();			
			
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
