import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class PickupList {
	
	private ArrayList<Pickup> pickups;
	
	public PickupList(){
		
		pickups = new ArrayList<Pickup>();
		
	}
	
	public ArrayList<Pickup> getPickups(){
		return pickups;
	}

	public void addPickup(Pickup p){
		pickups.add(p);
	}
	
	public void Read(String FileName) throws FileNotFoundException{
		
		pickups.clear();
		Scanner s = new Scanner(new File(FileName));
		
		String tempNumber = s.nextLine();
		
		int numPickups = Integer.parseInt(tempNumber.substring(17, tempNumber.length()-1));

		for(int i = 0; i < numPickups; i++)
		{
			
			pickups.add(Pickup.Read(s));
			
		}
				
	}
	
}
