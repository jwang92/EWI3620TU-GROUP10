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
	
}
