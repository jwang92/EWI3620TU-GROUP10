import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class ObjectList {

	private ArrayList<Object> objects;
	
	public ObjectList(){
		
		objects = new ArrayList<Object>();
		
	}
	
	public ArrayList<Object> getObjects(){
		return objects;
	}
	
	
	
	public void Read(String filename) throws FileNotFoundException{
		
		Scanner s = new Scanner(new File(filename));
		
		String tempNumber = s.nextLine();
		
		int numObjects = Integer.parseInt(tempNumber.substring(17, tempNumber.length()-1));
		
				
		for(int i = 0; i < numObjects; i++){
			
			String nextObj = s.next();
			
			if(nextObj.equals("Ramp:")){
				objects.add(ObjectRamp.Read(s));				
			}
			if(nextObj.equals("Enemy:")){
				objects.add(ObjectEnemy.Read(s));				
			}
			
		}
		
	}
	
	public void addObject(Object o)
	{
		if(!objects.contains(o)){
			objects.add(o);
		}
		
	}
	
	public void WriteToFile(String FileName) throws IOException{
		
		String r = "NumberOfObjects: " + objects.size() + ";\r\n";
		
		for(int i = 0; i < objects.size(); i++)
		{
			if(objects.get(i) instanceof ObjectRamp){
				r += ((ObjectRamp) objects.get(i)).toFileFormat();		
			}
			else if(objects.get(i) instanceof ObjectEnemy){
				r += ((ObjectEnemy) objects.get(i)).toFileFormat();
			}
	
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
