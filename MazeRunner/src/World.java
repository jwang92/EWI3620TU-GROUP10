import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class World {
	
	private int xSize;
	private int ySize;
	
	public void Read(String FileName) throws FileNotFoundException
	{
		
		Scanner s = new Scanner(new File(FileName));
		
		s.next();
		
		xSize = s.nextInt();
		
		String tempNumber = s.next();
		ySize = Integer.parseInt(tempNumber.substring(0, tempNumber.length()-1));
	
	
	}
	
	public int getSizeX(){
		
		return xSize;
		
	}

	public int getSizeY(){
		
		return ySize;
		
	}
	
	public void setSizeX(int x){
		xSize = x;
	}
	
	public void setSizeY(int y){
		ySize = y;
	}

}
