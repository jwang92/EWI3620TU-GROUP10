package Maze;
import Utils.Point3D;


public class Pheromone extends Point3D implements Comparable<Pheromone> {
	protected double pheromone;
	private double evapconst = 0.99;
	
	public Pheromone(double x, double y, double z){
		super(x, y, z);
		pheromone = 100;
	}

	/**
	 * evaporates the pheromone
	 */
	public void evapPher(){
		pheromone = pheromone * evapconst;
	}
	
	/**
	 * checks if pheromone equals given object
	 */
	public boolean equals(Object that){
		if(that instanceof Pheromone){
			Pheromone other = (Pheromone) that;
			
			return (this.x == other.x && this.y == other.y && this.z == other.z);
		}
		
		return false;
	}
	
	public void setPher(double pher){
		pheromone = pher;
	}

	@Override
	/**
	 * compares 2 pheromones
	 */
	public int compareTo(Pheromone pher) {
		if(this.pheromone < pher.pheromone){
			return 1;
		}
		if(this.pheromone > pher.pheromone){
			return -1;
		}
		return 0;
	}
}
