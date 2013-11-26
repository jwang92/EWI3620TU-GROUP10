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
	private String loadfolder = "savefiles/MazeLevel";
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
									 
					drawRamp(gl, t.getPoints(), storey.getFloorHeight(),storey.getRoofHeight());
						
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
		
		ArrayList<ObjectRamp> o = new ArrayList<ObjectRamp>();
		ArrayList<Integer> oFloor = new ArrayList<Integer>();
		ArrayList<Object> tempo;
		
		for(int i = 0; i < storeys.size(); i++){
			
			tempo = storeys.get(i).getObjectList().getObjects();
			
			for(int j = 0; j < tempo.size(); j++){
				
				if(tempo.get(j) instanceof ObjectRamp){
				
					o.add((ObjectRamp) tempo.get(j));
					oFloor.add(i);
					
				}
				
			}
			
		}

		//System.out.println("X: "+x+" Z: "+z+" Y: "+y);
		
		for(int i = 0; i < o.size(); i++){
				
			ArrayList<Point2D.Float> points = o.get(i).getPoints();
			ArrayList<Point3D> p3d = new ArrayList<Point3D>();
			for(int k = 0; k < points.size(); k++){
				float floory = Float.MIN_VALUE;
				if(k<2){
					floory = storeys.get(oFloor.get(i)).getFloorHeight();
				}
				else{
					floory = storeys.get(oFloor.get(i)).getRoofHeight();
					
				}
				p3d.add(new Point3D((float)(points.get(k).x * SQUARE_SIZE), floory,(float)(points.get(k).y*SQUARE_SIZE)));
			}
			double[] surface = getSurface(p3d);
			
//			float hiX = Integer.MIN_VALUE;
//			float hiZ = Integer.MIN_VALUE;
//			
//			float loX = Integer.MAX_VALUE;
//			float loZ = Integer.MAX_VALUE;
//			
//			for(int j = 0; j < points.size(); j++){
//				
//				if(points.get(j).x > hiX)
//					hiX = points.get(j).x;
//				
//				if(points.get(j).y > hiZ)
//					hiZ = points.get(j).y;
//				
//				if(points.get(j).x < loX)
//					loX = points.get(j).x;
//				
//				if(points.get(j).y < loZ)
//					loZ = points.get(j).y;
//					
//			}
//			
//			//System.out.println("HiX: "+hiX+" LoX: "+loX+" HiZ: "+hiZ+" LoZ: "+loZ);
//			
//			hiX = hiX * (float) SQUARE_SIZE;
//			loX = loX * (float) SQUARE_SIZE;
//			hiZ = hiZ * (float) SQUARE_SIZE;
//			loZ = loZ * (float) SQUARE_SIZE;
			
			// Check if on ramp
			//System.out.println(distToSurface(surface[0],surface[1],surface[2],surface[3],x,y-2.5,z));
			//System.out.println(surface[0] +" " + surface[1] +" " + surface[2] +" " + surface[3] +" " + x +" " + y + " " +z);
			//double distance = distToSurface(surface[0],surface[1],surface[2],surface[3],x,y-2.5,z);
			double distance = distToSurfaceSegment(p3d,x,y-2.5,z);
			if(distance < 1.5 && distance > -0.5){
				double dy = (- surface[3]- surface[0]*x - surface[2]*z)/surface[1] - (y -2.5);
				//System.out.println(" OnRamp: "+ y);
				return dy;
								
			}
			//System.out.println("OffRamp: "+ y);
			
		}
		return 0;
		
		
	}

	public void drawRamp(GL gl, ArrayList<Point2D.Float> points, int floorHeight,int roofHeight){
		
		ArrayList<Point3D> p3D = new ArrayList<Point3D>();
		
		for(int i = 0; i < points.size(); i++){
			
			Point3D point = new Point3D();
			point.x = (float) (points.get(i).x * SQUARE_SIZE);
			point.z = (float) (points.get(i).y * SQUARE_SIZE);
			point.y = (float) floorHeight;
			if(i > 1)
				point.y = (float) roofHeight;
			
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
	
	public double[] getSurface(ArrayList<Point3D> p){
		double[] surface = new double[4];
		Point3D vector1 = new Point3D(p.get(1).x - p.get(0).x,p.get(1).y - p.get(0).y,p.get(1).z - p.get(0).z);
		Point3D vector2 = new Point3D(p.get(2).x - p.get(0).x,p.get(2).y - p.get(0).y,p.get(2).z - p.get(0).z);
		surface[0] = vector1.y*vector2.z - vector1.z*vector2.y;
		surface[1] = vector1.z*vector2.x - vector1.x*vector2.z;
		surface[2] = vector1.x*vector2.y - vector1.y*vector2.x;
		surface[3] = - surface[0]*p.get(0).x - surface[1]*p.get(0).y - surface[2]*p.get(0).z;
		return surface;
	}
	
	
	public double distToSegment(double px, double py, double vx, double vy, double wx, double wy) { return Math.sqrt(distToSegmentSquared(px, py, vx, vy, wx, wy)); }
	
	/**
	 * Returns the distance to the surface with formula nx*X + ny*Y + nz*Z + d with point(px,py,pz)
	 * @param nx X coordinate of the normal vector
	 * @param ny Y coordinate of the normal vector
	 * @param nz Z coordinate of the normal vector
	 * @param d Height of the plane
	 * @param px X coordinate of the point
	 * @param py Y coordinate of the point
	 * @param pz Z coordinate of the point
	 * @return
	 */
	public double distToSurface(double nx, double ny, double nz, double d, double px, double py, double pz){
		
		double teller = nx * px + ny * py + nz * pz + d;  
		double noemer = Math.sqrt(Math.pow(nx, 2) + Math.pow(ny, 2) + Math.pow(nz, 2));
		double dist = teller/noemer;
		return dist;
	}
	
	public double distToSurfaceSegment(ArrayList<Point3D> p, double qx, double qy, double qz){
		double[] surface = getSurface(p);
		double teller = surface[0] * qx + surface[1] * qy + surface[2] * qz + surface[3];
		double noemer = Math.pow(surface[0], 2) + Math.pow(surface[1], 2) + Math.pow(surface[2], 2);
		double t0 = -teller/noemer;
		double x0 = qx + surface[0]*t0;
		double y0 = qy + surface[1]*t0;
		double z0 = qz + surface[2]*t0;
		
		//Is the projection of the player inside the polygon
		Point3D q= new Point3D(x0,y0,z0);
		double angleSum = CalcAngleSum(q,p);
		double twoPI = 2*Math.PI;
				
		if(angleSum>(twoPI-0.2)&&angleSum<(twoPI+0.2)){
			return distToSurface(surface[0],surface[1],surface[2],surface[3],qx,qy,qz);
		}

//		double xmax = Double.MIN_VALUE;
//		double xmin = Double.MAX_VALUE;
//		double ymax = Double.MIN_VALUE;
//		double ymin = Double.MAX_VALUE;
//		double zmax = Double.MIN_VALUE;
//		double zmin = Double.MAX_VALUE;
//		for(int i=0;i<p.size();i++){
//			xmax = Math.max(xmax, p.get(i).x);
//			xmin = Math.min(xmin, p.get(i).x);
//			ymax = Math.max(xmax, p.get(i).y);
//			ymin = Math.min(ymin, p.get(i).y);
//			zmax = Math.max(zmax, p.get(i).z);
//			zmin = Math.min(zmin, p.get(i).z);
//		}
//		if(qx <= xmax+0.1 && qx >=xmin-0.1 && qy <= ymax+0.1 && qy >=ymin-0.1 && qz <= zmax+0.1 && qz >=zmin-0.1){
//
//			return distToSurface(surface[0],surface[1],surface[2],surface[3],qx,qy,qz);
//		}
		return Double.MAX_VALUE;
	}
	
	public double Modulus(Point3D p){
		return Math.sqrt(p.x*p.x + p.y*p.y + p.z*p.z);
	}
	
	public double CalcAngleSum(Point3D q,ArrayList<Point3D> p)
	{
	   int i;
	   int n = p.size();
	   double m1,m2;
	   double epsilon = 0.0000001;
	   double anglesum=0,costheta;

	   for (i=0;i<n;i++) {
		  Point3D p1 = new Point3D();
		  Point3D p2 = new Point3D();
	      p1.x = p.get(i).x - q.x;
	      p1.y = p.get(i).y - q.y;
	      p1.z = p.get(i).z - q.z;
	      p2.x = p.get((i+1)%n).x - q.x;
	      p2.y = p.get((i+1)%n).y - q.y;
	      p2.z = p.get((i+1)%n).z - q.z;

	      m1 = Modulus(p1);
	      m2 = Modulus(p2);
	      if (m1*m2 <= epsilon)
	         return 2*Math.PI; /* We are on a node, consider this inside */
	      else
	         costheta = (p1.x*p2.x + p1.y*p2.y + p1.z*p2.z) / (m1*m2);

	      anglesum += Math.acos(costheta);
	   }
	   return(anglesum);
	} 
	
}