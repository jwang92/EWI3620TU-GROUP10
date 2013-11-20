import java.io.File;
import java.io.FileNotFoundException;
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
				
				ObjectRamp temp = new ObjectRamp();
				temp.Read(s);
				objects.add(temp);
				temp = null;
				
			}
			
		}
		
	}
	
}
