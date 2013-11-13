import java.util.Scanner;


public class Floor {

	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int x3;
	private int y3;
	private int x4;
	private int y4;
	
	String texture;
	
	/**
	 * Creates a new Floor with the given coordinates
	 * @param xx1 x of coordinate 1
	 * @param yy1 y of coordinate 1
	 * @param xx2 x of coordinate 2
	 * @param yy2 y of coordinate 2
	 * @param xx3 x of coordinate 3
	 * @param yy3 y of coordinate 3
	 * @param xx4 x of coordinate 4
	 * @param yy4 y of coordinate 4
	 * @param tex Name of the texture
	 */
	public Floor(int xx1, int yy1, int xx2, int yy2, int xx3, int yy3, int xx4, int yy4, String tex)
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
	
	/**
	 * Creates format for filewriting
	 * @return String with correct format
	 */
	public String toFileFormat()
	{
		
		return x1 + " " + y1 + "; " + x2 + " " + y2 + "; " + x3 + " " + y3 + "; " + x4 + " " + y4 + "; " + texture + ";\r\n";
		
	}
	
	
	/**
	 * Reads a floor from the scanner
	 * @param s The Scanner with start Floor
	 * @return The Floor object that is read
	 */
	public static Floor Read(Scanner s)
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

		return new Floor(x1, y1, x2, y2, x3, y3, x4, y4, tex);	
		
	}
	
}
