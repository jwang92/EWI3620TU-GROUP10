import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RoofList {

	private ArrayList<Roof> roofs;
	
	/**
	 * Creates a new rooflist as ArrayList
	 */
	public RoofList()
	{
		
		roofs = new ArrayList<Roof>();
		
	}
	
	/**
	 * Return the roofs of this list in an ArrayList
	 * @return Returns the ArrayList with the roofs
	 */
	public ArrayList<Roof> getRoofs(){
		return roofs;
	}
	
	/**
	 * Fills the rooflist with roofs found in the file
	 * @param FileName The name of the file to be read
	 * @throws FileNotFoundException
	 */
	public void Read(String FileName) throws FileNotFoundException
	{
		roofs.clear();
		Scanner s = new Scanner(new File(FileName));
		
		String tempNumber = s.nextLine();
		
		int numRoofs = Integer.parseInt(tempNumber.substring(15, tempNumber.length()-1));

		for(int i = 0; i < numRoofs; i++)
		{
			
			roofs.add(Roof.Read(s));
			
		}
				
	}
	
	/**
	 * Adds a roof to the list
	 * @param r The Roof to be added
	 */
	public void addRoof(Roof r)
	{
		
		roofs.add(r);
		
	}
	
	/**
	 * Writes the complete list to a string
	 * @param FileName The name of the file where to list has to written to
	 * @throws IOException
	 */
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
