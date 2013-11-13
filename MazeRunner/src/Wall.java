import java.util.Scanner;


public class Wall {

	int startx;
	int starty;
	int endx;
	int endy;
	String texture;
	
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
