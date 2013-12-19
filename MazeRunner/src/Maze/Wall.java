package Maze;
import java.util.Scanner;


public class Wall {

	private int startx;
	private int starty;
	private int endx;
	private int endy;
	private String texture;
	
	/**
	 * Creates new Wall
	 * @param sx Start x coordinate
	 * @param sy Start y coordinate
	 * @param ex End x coordinate
	 * @param ey End y coordinate
	 * @param tex Texture name
	 */
	public Wall(int sx, int sy, int ex, int ey, String tex)
	{
		
		startx = sx;
		starty = sy;
		endx = ex;
		endy = ey;
		texture = tex;
		
	}
	
	public Wall()
	{
		
		startx = -1;
		starty = -1;
		endx = -1;
		endy = -1;
		texture = "";
		
	}
	
	/**
	 * Returns the x of the starting point of this wall
	 * @return Returns the starting x of the wall
	 */
	public int getStartx(){
		return startx;
	}
	
	/**
	 * Returns the y of the starting point of this wall
	 * @return Returns the starting y of the wall
	 */
	public int getStarty(){
		return starty;
	}
	
	/**
	 * Returns the x of the ending point of this wall
	 * @return Returns the ending x of the wall
	 */
	public int getEndx(){
		return endx;
	}
	
	/**
	 * Returns the y of the ending point of this wall
	 * @return Returns the ending y of the wall
	 */
	public int getEndy(){
		return endy;
	}
	
	/**
	 * Returns the texture of this wall
	 * @return Returns the name of this texture
	 */
	public String getTexture(){
		return texture;
	}
	
	/**
	 * Creates format for filewriting
	 * @return String with correct format
	 */
	public String toFileFormat()
	{
		
		return startx + " " + starty + "; " + endx + " " + endy + "; "+ texture + ";\r\n";
		
	}
	
	/**
	 * Creates a new object Wall from scanner
	 * @param s Scanner with the start on the new line with the wall
	 * @return Wall that is read
	 */
	public static Wall Read(Scanner s)
	{
		
		s.useDelimiter(" |; |;\r\n");
		
		int sx = s.nextInt();
		int sy = s.nextInt();
		int ex = s.nextInt();
		int ey = s.nextInt();
		String tex = s.next();

		return new Wall(sx, sy, ex, ey, tex);		
		
	}
	
	/**
	 * Checks if the other Object is equal
	 */
	public boolean equals(Object other){
		
		
		if(other instanceof Wall)
		{
			
			Wall that = (Wall) other;
			
			if(this.startx == that.startx && this.starty == that.starty && this.endx == that.endx && this.endy == that.endy){
				return true;
			}
			else if(this.startx == that.endx && this.starty == that.endy && this.endx == that.startx && this.endy == that.starty){
				return true;
			}

			
		}
		
		return false;
		
	}
	
}
