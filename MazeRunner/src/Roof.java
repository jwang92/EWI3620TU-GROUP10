import java.util.Scanner;


public class Roof {

	int x1;
	int y1;
	int x2;
	int y2;
	int x3;
	int y3;
	int x4;
	int y4;
	
	String texture;
	
	public Roof(int xx1, int yy1, int xx2, int yy2, int xx3, int yy3, int xx4, int yy4, String tex)
	{
		
		x1 = xx1;
		y1 = yy1;
		x2 = xx2;
		y2 = yy2;
		x3 = xx3;
		y3 = yy3;
		x4 = xx4;
		y4 = yy4;
		
		texture = tex;
		
	}
	
	public String toFileFormat()
	{
		
		return x1 + " " + y1 + "; " + x2 + " " + y2 + "; " + x3 + " " + y3 + "; " + x4 + " " + y4 + "; " + texture + ";\r\n";
		
	}	
	
	public static Roof Read(Scanner s)
	{
		
		s.useDelimiter(" |; |;\r\n");
		
		int x1 = s.nextInt();
		int y1 = s.nextInt();
		int x2 = s.nextInt();
		int y2 = s.nextInt();
		int x3 = s.nextInt();
		int y3 = s.nextInt();
		int x4 = s.nextInt();
		int y4 = s.nextInt();
		String tex = s.next();

		return new Roof(x1, y1, x2, y2, x3, y3, x4, y4, tex);	
		
	}
	
}
