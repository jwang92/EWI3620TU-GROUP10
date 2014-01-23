package Maze;
import java.util.ArrayList;
import java.util.PriorityQueue;

import Main.MainClass;


public class MazePheromones {
	private MainClass main;
	private PriorityQueue<Pheromone> pheromonesOrder;
	private ArrayList<Pheromone> pheromones;
	private Pheromone lastPher;
	
	
	public MazePheromones(MainClass mclass){
		main = mclass;
		pheromonesOrder = new PriorityQueue<Pheromone>();
		pheromones = new ArrayList<Pheromone>();
	}
	
	/**
	 * adds pheromones to arraylist
	 * @param x x coordinaat
	 * @param y y cordinaat
	 * @param z z coordinaat
	 */
	public void addPher(double x, double y, double z){
		Pheromone newpheromone = new Pheromone(x, y, z);
		
		//overwrite existing pheromone
		if(pheromones.contains(newpheromone)){
			int index = pheromones.indexOf(newpheromone);
			pheromones.remove(index);
		}

		pheromonesOrder = new PriorityQueue<Pheromone>(pheromones);
		
		//adds pheromone to ordered pheromonelist by pheromone amount
		if(pheromonesOrder.size() > 1){
			if(pherDistance(lastPher, newpheromone) > 1){
				pheromonesOrder.add(newpheromone);
				lastPher = newpheromone;
			}
		}
		else{
			pheromonesOrder.add(newpheromone);
			lastPher = newpheromone;
		}
		
		//write PriorityQueue to arraylist
		pheromones = new ArrayList<Pheromone>();
		while(pheromonesOrder.size() != 0){
			pheromones.add(pheromonesOrder.poll());
		}
	}
	
	/**
	 * evaporates and deletes pheromones
	 */
	public void evapPheromones(){
		for( Pheromone pher : pheromones ){
			pher.evapPher();
		}
		for(int i = pheromones.size()-1; i >= 0; i--){
			if(pheromones.get(i).pheromone < 3){
				pheromones.remove(i);
			}
		}
	}
	
	/**
	 * distance between pheromones
	 * @param pher1  pheromone 1
	 * @param pher2 pheromone 2
	 * @return
	 */
	public double pherDistance(Pheromone pher1, Pheromone pher2){
		double dx = Math.abs(pher1.x - pher2.x);
		double dy = Math.abs(pher1.z - pher2.z);
		
		return Math.sqrt(dx*dx + dy+dy);
	}
	
	/**
	 * checks if pheromone is visible
	 * @param x  x location object
	 * @param pherX  pheromone x location
	 * @param z  z location object
	 * @param pherZ pheromone z location 
	 * @param y y location object
	 * @return
	 */
	public boolean obstructed(double x, double pherX, double z, double pherZ, double y){
		if(main.maze.visionBlocked(x, y, z, pherX, pherZ)){
			return true;
		}
		return false;
	}
	
	/**
	 * returns highest pheromone within vision
	 * @param x
	 * @param y
	 * @param z
	 * @param vision
	 * @return
	 */
	public Pheromone Search(double x, double y, double z, double vision){
		Pheromone highestPher = new Pheromone(x,y,z);
		highestPher.setPher(0);									//intial highest pheromone with pheromone = 0
		
		for( Pheromone pher : pheromones){
			double dx = pher.x - x;
			double dy = pher.y - y;
			double dz = pher.z - z;
			double distance = Math.sqrt(dx*dx + dz*dz);			//calculate distance to current pheromone
			
			if( distance <= vision && Math.abs(dy) <= (main.maze.SQUARE_SIZE/2.0) ){	//pheromone within vision?
				if( pher.pheromone > highestPher.pheromone ){		//current pheromone higher than highest?
					if(!obstructed(x, pher.x, z, pher.z, y))
						highestPher = pher;
				}
			}
			
			if(highestPher.pheromone > 0.0)
				break;
		}
		
		return highestPher;
	}
	
}
