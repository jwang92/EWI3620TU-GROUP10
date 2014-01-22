package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import GameObject.Enemy;
import LevelEditor.NavMesh;
import Main.MainClass;
import Maze.LevelExit;
import Maze.Maze;
import Maze.Storey;
import Utils.Point3D;

public class MazeTest {
	MainClass main;
	Maze maze;
	
	@Before
	public void setUp() throws Exception {
		//main = new MainClass(600,600,false);
		main = null;
		maze = new Maze("savefiles/JunitTestLevel",main);
		maze.createMaze();
	}

	@After
	public void tearDown() throws Exception {
		main = null;
		maze = null;
	}

	@Test
	public void CreateMazeTest() {
		maze.createMaze();
		ArrayList<Storey> temp = maze.getStoreys();
		assertEquals(temp.size(), 2);
	}
	
	@Test
	public void GenerateNavMeshTest() {
		ArrayList<NavMesh> temp = maze.getNavMesh();
		assertEquals(temp.size(), 2);
	}
	
	@Test
	public void LoadEnemiesTest(){
		main = new MainClass(600,600,false);;
		maze = new Maze("savefiles/JunitTestLevel",main);
		ArrayList<Enemy> enemies = maze.loadEnemies();
		assertEquals(enemies.size(),4);
		
	}
	
	@Test
	public void getLevelTest(){
		assertEquals(maze.getLevel(),"JunitTestLevel");
	}
	
	@Test
	public void getStoreyTest1() {
		assertEquals(maze.getStorey(1), 0);
		assertEquals(maze.getStorey(14), -1);
		assertEquals(maze.getStorey(-14), -1);
	}
	
	@Test
	public void isWallTest() {
		assertTrue(maze.isWall(65.47492333368965, 2.5, 29.444739729333154));
		assertFalse(maze.isWall(64.47492333368965, 2.5, 28.444739729333154));
		assertFalse(maze.isWall(64.47492333368965, -2.5, 28.444739729333154));
		assertFalse(maze.isWall(64.47492333368965, 20, 28.444739729333154));
	}
	
	@Test
	public void isDoorTest() {
		main = new MainClass(600,600,false);;
		maze = new Maze("savefiles/JunitTestLevel",main);
		assertTrue(maze.isDoor(55.47665365626652, 2.5, 28.714174813040593));
		assertFalse(maze.isDoor(55.55134142706395, 2.5, 28.894487042243163));
		assertFalse(maze.isDoor(55.47665365626652, -2.5, 28.714174813040593));
		assertFalse(maze.isDoor(55.47665365626652, 20, 28.714174813040593));
	}
	
	@Test
	public void isPickupTest() {
		assertEquals(maze.isPickup(53.30743438731379, 51.609460022599876, 2.5), 3);
		assertEquals(maze.isPickup(52.91353151382229, 52.101131179223025, 2.5), 0);
	}
	
	@Test
	public void isExitTest(){
		LevelExit exit = (LevelExit)maze.getStoreys().get(0).getObjectList().getObjects().get(2);
		assertEquals(maze.isExit(42.764520874198105, 2.5, 22.797268112536816),exit);
		assertEquals(maze.isExit(59.55410191252914, 2.5, 34.52357291657581),null);
		
		//x
		assertEquals(maze.isExit(-59.55410191252914, 2.5, 11134.52357291657581),null);
		
		//z
		assertEquals(maze.isExit(42.764520874198105, 2.5, -34.52357291657581),null);
		assertEquals(maze.isExit(42.764520874198105, 2.5, 11134.52357291657581),null);
		
		//y
		assertEquals(maze.isExit(42.764520874198105, -2.5, 22.797268112536816),null);
		assertEquals(maze.isExit(42.764520874198105, 20, 22.797268112536816),null);
		
	}
	
	@Test
	public void isFloorTest(){
		assertEquals(maze.isFloor(42.764520874198105, 2.5, 22.797268112536816),true);
		assertEquals(maze.isFloor(-59.55410191252914, 2.5, 34.52357291657581),false);
		assertEquals(maze.isFloor(42.764520874198105, -2.5, 22.797268112536816),false);
		assertEquals(maze.isFloor(42.764520874198105, 20, 22.797268112536816),false);
		assertEquals(maze.isFloor(42.764520874198105, 4.5, 22.797268112536816),false);
	}
	@Test
	public void isThroughFloorTest(){
		assertEquals(maze.throughFloor(42.764520874198105, -0.2,2.5,22.797268112536816),true);
		assertEquals(maze.throughFloor(42.764520874198105, 2.7, 2.9,22.797268112536816),false);
		assertEquals(maze.throughFloor(59.55410191252914, -4.8, -2.5,34.52357291657581),false);
		assertEquals(maze.throughFloor(59.55410191252914, 2.5, 20,34.52357291657581),false);
		assertEquals(maze.throughFloor(-59.55410191252914, 2.5, 2.7,34.52357291657581),false);
	}
	
	@Test
	public void isThroughRoofTest(){
		assertEquals(maze.throughRoof(39.10421402018012, 20.0,2.7,37.62276090460826),true);
		assertEquals(maze.throughRoof(42.764520874198105, 3.1, 2.9,22.797268112536816),false);
		assertEquals(maze.throughRoof(59.55410191252914, -0.2, -2.5,34.52357291657581),false);
		assertEquals(maze.throughRoof(59.55410191252914, 2.5, 20,34.52357291657581),false);
		assertEquals(maze.throughRoof(-59.55410191252914, 2.5, 2.7,34.52357291657581),false);
		assertEquals(maze.throughRoof(39.10421402018012, 3.0,2.7,37.62276090460826),false);
	}
	
	@Test
	public void isRampTest(){
		assertEquals(maze.isRamp(38.10325179885769, 4.954731447763898, 62.4547314477639), -4.440892098500626E-16, 0.1);
		assertEquals(maze.isRamp(38.10325179885769, 8, 62.4547314477639),Double.MAX_VALUE, 1);
		assertEquals(maze.isRamp(28.10325179885769, 4.954731447763898, 62.4547314477639),Double.MAX_VALUE, 1);
		assertEquals(maze.isRamp(38.10325179885769, 4.954731447763898, 52.4547314477639),Double.MAX_VALUE, 1);
		assertEquals(maze.isRamp(38.10325179885769, 2.954731447763898, 62.4547314477639),Double.MIN_VALUE,0.1);
	}
	
	@Test
	public void visionBlockedTest(){
		assertEquals(maze.visionBlocked(61.91392069301465, 2.5, 30.001431460481577,62.70550601855932, 31.293181309478022 ),false);
		assertEquals(maze.visionBlocked(61.91392069301465, 2.5, 1.001431460481577,62.70550601855932, 31.293181309478022 ),true);
		assertEquals(maze.visionBlocked(61.91392069301465, 20.5, 30.001431460481577,62.70550601855932, 31.293181309478022 ),false);
		assertEquals(maze.visionBlocked(61.91392069301465, 2.5, 30.001431460481577,61.91392069301465, 30.001431460481577 ),false);
	}
	
	@Test
	public void getFloorHeightTest(){
		assertEquals(maze.getFloorHeight(2.5), 0, 0.1);
		assertEquals(maze.getFloorHeight(8.5), 5, 0.1);
		assertEquals(maze.getFloorHeight(-8.5), Double.MIN_VALUE, 0.1);
	}
	
	@Test
	public void calcAngleSumTest(){
		Point3D temp = new Point3D(0,0,0);
		Point3D temp1 = new Point3D(1,0,0);
		Point3D temp2 = new Point3D(1,0,1);
		Point3D temp3 = new Point3D(0,0,1);
		ArrayList<Point3D> points = new ArrayList<Point3D>();
		points.add(temp);
		points.add(temp1);
		points.add(temp2);
		points.add(temp3);
		assertEquals(maze.CalcAngleSum(temp, points),2*Math.PI,0.1);
		
	}
	
	@Test
	public void distToSegmentSquaredTest(){
		assertEquals(maze.distToSegmentSquared(1, 1, 0, 0, 0, 0), maze.dist2(0, 0, 1, 1),0.1);
	}
	

	
	
	
	
	
	

}
