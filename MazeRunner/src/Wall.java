import java.util.Scanner;


public class Wall {

	int startx;
	int starty;
	int endx;
	int endy;
	String texture;
	
	public Wall(int sx, int sy, int ex, int ey, String tex)
	{	
		startx = sx;
		starty = sy;
		endx = ex;
		endy = ey;
		texture = tex;
		
	}
	
	public String toFileFormat()
	{
		
		return startx + " " + starty + "; " + endx + " " + endy + "; "+ texture + ";\r\n";
		
	}
	
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
	
	public boolean equals(Object other){
		if(other instanceof Wall){
			Wall that = (Wall)other;
			if(this.startx==that.startx&&this.starty==that.starty&&this.endx==that.endx&&this.endy==that.endy){
				return true;
			}
			else if(this.startx==that.endx&&this.starty==that.endy&&this.endx==that.startx&&this.endy==that.starty){
				return true;
			}
		}
		return false;
	}
	
}
