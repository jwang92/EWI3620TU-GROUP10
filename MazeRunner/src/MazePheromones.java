import java.util.ArrayList;


public class MazePheromones {
	private ArrayList<Pheromone> pheromones;
	
	public MazePheromones(){
		pheromones = new ArrayList<Pheromone>();
	}
	
	public void addPher(double x, double y, double z){
		Pheromone newpheromone = new Pheromone(x, y, z);
		
		if(pheromones.contains(newpheromone)){
			int index = pheromones.indexOf(newpheromone);
			pheromones.remove(index);
		}
		
		pheromones.add(newpheromone);
		
//		System.out.println("player: " + newpheromone.x + " , " + newpheromone.z);
	}
	
	public void evapPheromones(){
		for( Pheromone pher : pheromones ){
			pher.evapPher();
		}
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
		Pheromone highestPher = new Pheromone(0,0,0);
		highestPher.setPher(0);									//intial highest pheromone with pheromone = 0
		
		for( Pheromone pher : pheromones){
			double dx = pher.x - x;
			double dy = pher.y+1.25 - y;
			double dz = pher.z - z;
			double distance = Math.sqrt(dx*dx + dz*dz);			//calculate distance to current pheromone
			
			if( distance <= vision /*&& Math.abs(dy) <= 1.25*/ ){	//pheromone within vision?
				if( pher.pheromone > highestPher.pheromone ){		//current pheromone higher than highest?
					boolean obstructed = false; 
					for( int i = 0; i < 50; i++ ){
						double tx = x + dx*i/50.0;
						double tz = z + dz*i/50.0;
						if( MainClass.maze.isWall(tx, y, tz) )
							obstructed = true;
					}
//					System.out.println(obstructed);
					if(!obstructed)
						highestPher = pher;
				}
			}
		}
		
		return highestPher;
	}
	
}
