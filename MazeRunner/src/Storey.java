import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Storey {
	private int xSize;
	private int ySize;
	private int floorHeight;
	private int roofHeight;
	private WallList walls;
	private FloorList floors;
	private RoofList roofs;
	private ObjectList objects;
	private PickupList pickups;
	private LevelExitList exits;
	
	public Storey(int sizeX, int sizeY, int floorh,int heightOfStorey, WallList wall, FloorList floor, RoofList roof, ObjectList object, PickupList pickup){
		xSize = sizeX;
		ySize = sizeY;
		floorHeight = floorh;
		roofHeight = floorHeight + heightOfStorey;
		walls = wall;
		floors = floor;
		roofs = roof;
		objects = object;
		pickups = pickup;
		
		exits = new LevelExitList();
		LevelExit exit = new LevelExit(new Point2D.Float(8, 8));
		exits.addExit(exit);
	}
	
	public Storey(){
		xSize = 0;
		ySize = 0;
		floorHeight = 0;
		roofHeight = 0;
		walls = new WallList();
		floors = new FloorList();
		roofs = new RoofList();
		objects = new ObjectList();
		pickups = new PickupList();
		
		exits = new LevelExitList();
	}
	
	public Storey(int sizeX, int sizeY, int floorh,int heightOfStorey){
		xSize = sizeX;
		ySize = sizeY;
		floorHeight = floorh;
		roofHeight = floorHeight + heightOfStorey;
		walls = new WallList();
		floors = new FloorList();
		roofs = new RoofList();
		objects = new ObjectList();
		pickups = new PickupList();
		
		exits = new LevelExitList();
		LevelExit exit = new LevelExit(new Point2D.Float(8, 8));
		exits.addExit(exit);
	}
	
	public int getSizeX(){
		
		return xSize;
		
	}

	public int getSizeY(){
		
		return ySize;
		
	}
	
	public int getFloorHeight(){
		return floorHeight;
	}
	
	public int getRoofHeight(){
		return roofHeight;
	}
	
	public WallList getWallList(){
		return walls;
	}
	
	public FloorList getFloorList(){
		return floors;
	}
	
	public ObjectList getObjectList(){
		return objects;
	}
	
	public PickupList getPickupList(){
		return pickups;
	}
	
	public LevelExitList getLevelExitList(){
		return exits;
	}
	
	public RoofList getRoofList(){
		return roofs;
	}
		
	public void setSizeX(int x){
		xSize = x;
	}
	
	public void setSizeY(int y){
		ySize = y;
	}
	
	public void setFloorHeight(int floorh){
		floorHeight = floorh;
	}
	
	public void setRoofHeight(int roofh){
		roofHeight = roofh;
	}
	
	public void setWalls(WallList w){
		walls = w;
	}
	
	public void setFloors(FloorList f){
		floors = f;
	}
	
	public void setRoofs(RoofList r){
		roofs = r;
	}
	
	public void setObjects(ObjectList o){
		objects = o;
	}
	
	public static Storey Read(String FileName) throws FileNotFoundException{
		Scanner s = new Scanner(new File(FileName + "/Storey.txt"));
		s.next();
		int sizeX = s.nextInt();
		String tempNumber = s.next();
		int sizeY = Integer.parseInt(tempNumber.substring(0, tempNumber.length()-1));
		s.next();
		tempNumber = s.next();
		int floorh = Integer.parseInt(tempNumber.substring(0, tempNumber.length()-1));
		s.next();
		tempNumber = s.next();
		int storeyh = Integer.parseInt(tempNumber.substring(0, tempNumber.length()-1));
		s.close();
		WallList w = new WallList();
		FloorList f = new FloorList();
		RoofList r = new RoofList();
		ObjectList o = new ObjectList();
		PickupList p = new PickupList();
		w.Read(FileName+"/Walls.txt");
		f.Read(FileName+"/Floor.txt");
		r.Read(FileName+"/Roof.txt");
		o.Read(FileName+"/Objects.txt");
		p.Read(FileName+"/Pickups.txt");
		Storey res = new Storey(sizeX, sizeY,floorh,storeyh,w,f,r,o,p);
		return res; 
	}
	
	public void WriteToFile(String FileName) throws IOException{
		
		String r = "Size: " + xSize + " " + ySize + ";\r\n";
		r += "Floorheight: " + floorHeight + ";\r\n"; 
		r += "StoreyHeight: " + (roofHeight - floorHeight) + ";\r\n";
		r += "End;";

		File f = new File(FileName + "/Storey.txt");
		// Create new file with the given name if it doesn't exist yet
		if(!f.exists())
		{
			f.createNewFile();
		}
		FileWriter w = new FileWriter(f);
		w.write(r);
		w.close();
		walls.WriteToFile(FileName + "/Walls.txt");
		floors.WriteToFile(FileName + "/Floor.txt");
		roofs.WriteToFile(FileName + "/Roof.txt");
		objects.WriteToFile(FileName + "/Objects.txt");
		pickups.WriteToFile(FileName + "/Pickups.txt");
	} 

}
