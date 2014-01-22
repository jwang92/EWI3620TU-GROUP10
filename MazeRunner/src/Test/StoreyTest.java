package Test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Maze.Floor;
import Maze.FloorList;
import Maze.ObjectList;
import Maze.PickupList;
import Maze.Roof;
import Maze.RoofList;
import Maze.Storey;
import Maze.Wall;
import Maze.WallList;

public class StoreyTest {
	Storey emptystorey;
	
	@Before
	public void setUp() throws Exception {
		emptystorey = new Storey();
	}

	@After
	public void tearDown() throws Exception {
		emptystorey = null;
	}

	@Test
	public void storeyEmptyConstructTest(){
		Storey s = new Storey();
		assertEquals(s.getFloorHeight(),0);
		assertEquals(s.getRoofHeight(),0);
		assertEquals(s.getSizeX(),0);
		assertEquals(s.getSizeY(),0);
	}
	
	@Test
	public void storeyNewLevelConstructTest() {
		Storey s = new Storey(8,12,0,5);
		assertEquals(s.getFloorHeight(),0);
		assertEquals(s.getRoofHeight(),5);
		assertEquals(s.getSizeX(),8);
		assertEquals(s.getSizeY(),12);
	}
	
	@Test
	public void storeyExistingLevelConstructTest() {
		WallList w = new WallList();
		FloorList f = new FloorList();
		RoofList r = new RoofList();
		ObjectList o = new ObjectList();
		PickupList p = new PickupList();
		Storey s = new Storey(8,12,0,5,w,f,r,o,p);
		assertEquals(s.getFloorHeight(),0);
		assertEquals(s.getRoofHeight(),5);
		assertEquals(s.getSizeX(),8);
		assertEquals(s.getSizeY(),12);
		assertEquals(s.getWallList(),w);
		assertEquals(s.getFloorList(),f);
		assertEquals(s.getRoofList(),r);
		assertEquals(s.getObjectList(),o);
		assertEquals(s.getPickupList(),p);
	}
	
	@Test
	public void setSizeTest(){
		emptystorey.setSizeX(1);
		assertEquals(emptystorey.getSizeX(),1);
		emptystorey.setSizeY(2);
		assertEquals(emptystorey.getSizeY(),2);
	}
	
	@Test
	public void setHeightsTest(){
		emptystorey.setFloorHeight(1);
		assertEquals(emptystorey.getFloorHeight(),1);
		emptystorey.setRoofHeight(5);
		assertEquals(emptystorey.getRoofHeight(),5);
	}
	
	@Test
	public void setListsTest(){
		WallList w = new WallList();
		w.addWall(new Wall());
		FloorList f = new FloorList();
		f.addFloor(new Floor());
		RoofList r = new RoofList();
		r.addRoof(new Roof());
		ObjectList o = new ObjectList();
		o.addObject(new Object());
		emptystorey.setWalls(w);
		assertEquals(emptystorey.getWallList(),w);
		emptystorey.setFloors(f);
		assertEquals(emptystorey.getFloorList(),f);
		emptystorey.setRoofs(r);
		assertEquals(emptystorey.getRoofList(),r);
		emptystorey.setObjects(o);
		assertEquals(emptystorey.getObjectList(),o);
	}
	
	@Test
	public void readStoreyTest(){
		Storey s = null;
		try {
			s= Storey.Read("savefiles/JunitTestLevel/Floor 1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(s.getFloorHeight(),0);
		assertEquals(s.getRoofHeight(),5);
		assertEquals(s.getSizeX(),15);
		assertEquals(s.getSizeY(),15);
		
	}
	
	@Test
	public void writeToFileTest(){
		Storey s = null;
		try {
			s= Storey.Read("savefiles/JunitTestLevel/Floor 1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			s.WriteToFile("savefiles/JunitTestLevel2/Floor 1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Storey s1 = null;
		try {
			s1 = Storey.Read("savefiles/JunitTestLevel/Floor 1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(s.getFloorHeight(),0);
		assertEquals(s.getRoofHeight(),5);
		assertEquals(s.getSizeX(),15);
		assertEquals(s.getSizeY(),15);
	}

}
