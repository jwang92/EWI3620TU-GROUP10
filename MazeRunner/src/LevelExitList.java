import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class LevelExitList {
	private ArrayList<LevelExit> exits;
	
	public LevelExitList(){
		
		exits = new ArrayList<LevelExit>();
		
	}
	
	public ArrayList<LevelExit> getExits(){
		return exits;
	}

	public void addExit(LevelExit e){
		exits.add(e);
	}
	
	public void Read(String FileName) throws FileNotFoundException{
//		
//		pickups.clear();
//		Scanner s = new Scanner(new File(FileName));
//		
//		String tempNumber = s.nextLine();
//		
//		int numPickups = Integer.parseInt(tempNumber.substring(17, tempNumber.length()-1));
//
//		for(int i = 0; i < numPickups; i++)
//		{
//			
//			pickups.add(Pickup.Read(s));
//			
//		}
//				
	}

}
