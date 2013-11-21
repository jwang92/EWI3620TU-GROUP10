import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;



public class Maze  implements VisibleObject {
	
	public final double SQUARE_SIZE = 5;
	private int height = 5;
	private Storey storey;
	private ArrayList<Storey> storeys;
	private String loadfolder = "MazeLevel";
	private int numberOfStoreys;
	
	public Maze(){
		storeys = new ArrayList<Storey>();
		try {
		    File folder = new File(loadfolder);
		    File[] tList = folder.listFiles();
		    numberOfStoreys = tList.length;
		    for(int i = 0; i<tList.length;i++){
			    if(tList[i].getName().equals("Thumbs.db")){
			    	numberOfStoreys -= 1;
			    }  
		    }
			for(int i =1;i<numberOfStoreys+1;i++){
				storey = Storey.Read(loadfolder + "/Floor " + i);
				storeys.add(storey);
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void display(GL gl){
		gl.glDisable(GL.GL_CULL_FACE);
		for(int i = 0; i<numberOfStoreys;i++){
			storey = storeys.get(i);
			ArrayList<Wall> w1 = storey.getWallList().getWalls();
			ArrayList<Floor> f1 = storey.getFloorList().getFloors();
			ArrayList<Roof> r1 = storey.getRoofList().getRoofs();
			ArrayList<Object> o1 = storey.getObjectList().getObjects();
			for(int j = 0; j < w1.size(); j++){
				drawWall(gl, w1.get(j).getStartx(), w1.get(j).getStarty(), w1.get(j).getEndx(), w1.get(j).getEndy(),w1.get(j).getTexture(), storey.getFloorHeight(),storey.getRoofHeight());
			}
			for(int j = 0; j< f1.size(); j++){
				drawFloor(gl,f1.get(j).getPoints(),f1.get(j).getTexture(), storey.getFloorHeight());
			}
			for(int j = 0; j <r1.size(); j++){
				drawRoof(gl,r1.get(j).getPoints(),r1.get(j).getTexture(), storey.getRoofHeight());
			}
			for(int j = 0; j < o1.size(); j++){
					
				if(o1.get(j) instanceof ObjectRamp){
						
					ObjectRamp t = (ObjectRamp) o1.get(j);
									 
					drawRamp(gl, t.getPoints(), storey.getFloorHeight());
						
				}
				
			}

		}
	}
	
	public void drawWall(GL gl, int sx, int sy, int ex, int ey,String texture, int zfloor, int zroof){
		ArrayList<Point3D> p = new ArrayList<Point3D>();
		Point3D p1 = new Point3D(); Point3D p2 = new Point3D(); Point3D p3 = new Point3D(); Point3D p4 = new Point3D();
			p1.x = (float) (sx * SQUARE_SIZE);
			p1.y = (float) (zroof);
			p1.z = (float) (sy * SQUARE_SIZE);
			p.add(p1);
			p2.x = (float) (ex * SQUARE_SIZE);
			p2.y = (float) (zroof);
			p2.z = (float) (ey * SQUARE_SIZE);
			p.add(p2);
			p3.x = (float) (ex * SQUARE_SIZE);
			p3.y = (float) zfloor;
			p3.z = (float) (ey * SQUARE_SIZE);
			p.add(p3);
			p4.x = (float) (sx * SQUARE_SIZE);
			p4.y = (float) zfloor;
			p4.z = (float) (sy * SQUARE_SIZE);
			p.add(p4);
		int textureID = MainClass.textureNames.lastIndexOf(texture);
	
		polygonOnScreen(gl,p,textureID);
	}
	
	public void drawFloor(GL gl, ArrayList<Point2D.Float> p2D, String texture, int z){
		ArrayList<Point3D> p3D = new ArrayList<Point3D>();
		for(int i =0; i<p2D.size();i++){
			Point3D point = new Point3D();
			point.x = (float) (p2D.get(i).x * SQUARE_SIZE);
			point.y = (float) z;
			point.z = (float) (p2D.get(i).y * SQUARE_SIZE);
			p3D.add(point);
		}
		int textureID = MainClass.textureNames.lastIndexOf(texture);
		polygonOnScreen(gl,p3D,textureID);		
	}
	
	public void drawRoof(GL gl, ArrayList<Point2D.Float> p2D, String texture, int z){
		ArrayList<Point3D> p3D = new ArrayList<Point3D>();
		for(int i =0; i<p2D.size();i++){
			Point3D point = new Point3D();
			point.x = (float) (p2D.get(i).x * SQUARE_SIZE);
			point.y = (float) z;
			point.z = (float) (p2D.get(i).y * SQUARE_SIZE);
			p3D.add(point);
		}
		int textureID = MainClass.textureNames.lastIndexOf(texture);
		polygonOnScreen(gl,p3D,textureID);		
	}
	
	
	
	private void polygonOnScreen(GL gl, ArrayList<Point3D> p, int textureID){
		//Set the color
		float wallColour[] = { 1.0f, 1.0f, 1.0f};				
        gl.glMaterialfv( GL.GL_FRONT, GL.GL_DIFFUSE, wallColour, 0);	// Set the materials used by the floor.
		
        gl.glNormal3d(0, 1, 0);
        
		//Enable textures
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		//Apply texture
		MainClass.textures.get(textureID).getTarget();
		//brickTexture.enable();
		MainClass.textures.get(textureID).bind();		
		gl.glBegin(GL.GL_QUADS);
    		gl.glTexCoord2f(0, 0);
    		gl.glVertex3d(p.get(0).x,p.get(0).y,p.get(0).z);
    		gl.glTexCoord2f(0, 10);
    		gl.glVertex3d(p.get(1).x,p.get(1).y,p.get(1).z);
    		gl.glTexCoord2f(10, 10);
    		gl.glVertex3d(p.get(2).x,p.get(2).y,p.get(2).z);
    		gl.glTexCoord2f(10, 0);
    		gl.glVertex3d(p.get(3).x,p.get(3).y,p.get(3).z);		
		
		gl.glEnd();
		
		//Disable texture
		MainClass.textures.get(textureID).disable();
	}
	
	
	public boolean isWall( double x, double z, double y)
	{
		
		ArrayList<Wall> w;
		
		if(y < 5){
			storey = storeys.get(0);
			w = storey.getWallList().getWalls();
		}
		else{
			storey = storeys.get(1);
			w = storey.getWallList().getWalls();
		}			
		double distance;
		
		for(int i = 0; i < w.size(); i++){
			
			distance = distToSegment(x / SQUARE_SIZE, z / SQUARE_SIZE, w.get(i).getStartx(), w.get(i).getStarty(), w.get(i).getEndx(), w.get(i).getEndy());
		
			if(distance < 0.1){
				return true;
			}
			
		}
		
		return false;		
		
	}
	
public double isRamp(double x, double z, double y){
		
		ArrayList<Object> o;
		
		if(y < 7.3){
			storey = storeys.get(0);
			o = storey.getObjectList().getObjects();
		}
		else{
			storey = storeys.get(1);
			o = storey.getObjectList().getObjects();
		}	
		
		//System.out.println("X: "+x+" Z: "+z+" Y: "+y);
		
		for(int i = 0; i < o.size(); i++){
			
			if(o.get(i) instanceof ObjectRamp){
				
				
				ObjectRamp tempRamp = (ObjectRamp) o.get(i);
				ArrayList<Point2D.Float> points = tempRamp.getPoints();
				
				float hiX = Integer.MIN_VALUE;
				float hiZ = Integer.MIN_VALUE;
				
				float loX = Integer.MAX_VALUE;
				float loZ = Integer.MAX_VALUE;
				
				for(int j = 0; j < points.size(); j++){
					
					if(points.get(j).x > hiX)
						hiX = points.get(j).x;
					
					if(points.get(j).y > hiZ)
						hiZ = points.get(j).y;
					
					if(points.get(j).x < loX)
						loX = points.get(j).x;
					
					if(points.get(j).y < loZ)
						loZ = points.get(j).y;
						
				}
				
				//System.out.println("HiX: "+hiX+" LoX: "+loX+" HiZ: "+hiZ+" LoZ: "+loZ);
				
				hiX = hiX * (float) SQUARE_SIZE;
				loX = loX * (float) SQUARE_SIZE;
				hiZ = hiZ * (float) SQUARE_SIZE;
				loZ = loZ * (float) SQUARE_SIZE;
				
				// Check if on ramp
				if(hiX > x && x > loX && hiZ > z && z > loZ){
					
					if(points.get(0).x == points.get(3).x){
						
						// Hij gaat omhoog over de z
						double onRamp = 0;
						
						if(points.get(0).y < points.get(3).y){
							onRamp = z - loZ;
						}
						else if(points.get(0).y > points.get(3).y){
							onRamp = hiZ - z;
						}
						
						double dY = Math.tan(0.25*Math.PI) * onRamp + 2.5;
						System.out.println(dY - y);
						return dY - y;
						
						
					}
					else if(points.get(0).y == points.get(3).y){
						
						// Hij gaat omhoog over de x
						double onRamp = 0;
						
						if(points.get(0).x < points.get(3).x){
							onRamp = x - loX;
						}
						else if(points.get(0).x > points.get(3).x){
							System.out.println("loX: "+loX+" hiX: "+hiX+" loZ: "+loZ+" hiZ: "+hiZ+" z: "+z+" x: "+x+" y:"+y);
							onRamp = hiX - x;
						}
						
						double dY = Math.tan(0.25*Math.PI) * onRamp + 2.5;
						System.out.println(dY - y);
						return dY - y;
						
					}
						
					
				}
				
			}
			
		}
		return 0;
		
		
	}

	public void drawRamp(GL gl, ArrayList<Point2D.Float> points, int z){
		
		ArrayList<Point3D> p3D = new ArrayList<Point3D>();
		
		for(int i = 0; i < points.size(); i++){
			
			Point3D point = new Point3D();
			point.x = (float) (points.get(i).x * SQUARE_SIZE);
			point.z = (float) (points.get(i).y * SQUARE_SIZE);
			point.y = (float) z;
			if(i > 1)
				point.y = (float) z + 5;
			
			p3D.add(point);
			
		}
		//int textureID = MainClass.textureNames.lastIndexOf(texture);
		polygonOnScreen(gl,p3D, 1);	
		
	}

		
	public double dist2(double vx, double vy, double wx, double wy) { return Math.pow(vx - wx, 2) + Math.pow(vy - wy, 2); }
	
	public double distToSegmentSquared(double px, double py, double vx, double vy, double wx, double wy) {
	  double l2 = dist2(vx, vy, wx, wy);
	  if (l2 == 0) return dist2(px, py, vx, vy);
	  double t = ((px - vx) * (wx - vx) + (py - vy) * (wy - vy)) / l2;
	  if (t < 0) return dist2(px, py, vx, vy);
	  if (t > 1) return dist2(px, py, wx, wy);
	  return dist2(px, py, vx + t * (wx - vx), vy + t * (wy - vy));
	}
	
	public double distToSegment(double px, double py, double vx, double vy, double wx, double wy) { return Math.sqrt(distToSegmentSquared(px, py, vx, vy, wx, wy)); }
	
	
}