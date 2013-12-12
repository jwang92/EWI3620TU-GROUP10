import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import com.sun.opengl.util.GLUT;



public class Maze  implements VisibleObject {
	
	public final double SQUARE_SIZE = 5;
	private int height = 5;
	private Storey storey;
	private ArrayList<Storey> storeys;
	private String loadfolder = "savefiles/kasteel";
	private int numberOfStoreys;
	private LevelInfo lvlinfo;
	private double pickupColor = 0.0;
	private boolean colorUp = true;
	
	public Maze(){
		createMaze();
	}
	
	public Maze(String loadfolder){
		this.loadfolder = loadfolder;
		createMaze();
	}
	
	public void createMaze(){
		storeys = new ArrayList<Storey>();
		try {
			
		    File folder = new File(loadfolder);
		    File[] tList = folder.listFiles();
		    numberOfStoreys = tList.length - 1; // -1 for LevelInfo.txt
		    
		    for(int i = 0; i<tList.length;i++){
			    if(tList[i].getName().equals("Thumbs.db")){
			    	numberOfStoreys -= 1;
			    } 
		    }
		    
			for(int i =1;i<numberOfStoreys+1;i++){
				storey = Storey.Read(loadfolder + "/Floor " + i);
				storeys.add(storey);
			}
			
			lvlinfo = new LevelInfo();
			lvlinfo.Read(loadfolder + "/LevelInfo.txt");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public LevelInfo getLevelInfo(){
		return lvlinfo;
	}
	
	public void display(GL gl){
		gl.glDisable(GL.GL_CULL_FACE);
		for(int i = 0; i<numberOfStoreys;i++){
			storey = storeys.get(i);
			ArrayList<Wall> w1 = storey.getWallList().getWalls();
			ArrayList<Floor> f1 = storey.getFloorList().getFloors();
			ArrayList<Roof> r1 = storey.getRoofList().getRoofs();
			ArrayList<Object> o1 = storey.getObjectList().getObjects();
			ArrayList<Pickup> p1 = storey.getPickupList().getPickups();
			ArrayList<LevelExit> e1 = storey.getLevelExitList().getExits();
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
			for(int j = 0; j < p1.size(); j++){
				drawPickup(gl, p1.get(j).getPoint(), storey.getRoofHeight(), p1.get(j).getType());				
			}
			for(LevelExit exit : e1){
				drawLevelExit(gl, exit, storey.getRoofHeight());
			}

		}
	}
	
	public void drawPickup(GL gl, Point2D.Float p, int z2, int type){
		String texture = "";
		if(type == 1)
			texture = "textures/upgrade_speed.png";
		else if(type == 2)
			texture = "textures/upgrade_sword.png";
		else if(type == 3)
			texture = "textures/upgrade_health.png";
		else if(type == 4)
			texture = "textures/upgrade_gun.png";

		int textureID = MainClass.textureNames.lastIndexOf(texture);
		
		double x = p.x * SQUARE_SIZE - 0.5;
		double y = p.y * SQUARE_SIZE - 0.5;
		double z = z2 - 3.5;
		double s = 1;
		
		if(colorUp){
			if(pickupColor >= 1){
				colorUp = false;
				pickupColor = 1;
			}else{
				pickupColor += 0.001;				
			}
		}
		else{
			if(pickupColor <= 0){
				colorUp = true;
				pickupColor = 0;
			}else{
				pickupColor -= 0.001;				
				}
			}
				
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		gl.glColor3d(pickupColor, 1f, 1f);

		gl.glEnable(GL.GL_TEXTURE_2D);
		MainClass.textures.get(textureID).bind();
		
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(x, z, y);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x, z, y + s);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x, z + s, y +s);
			gl.glTexCoord2f(1, 0); gl.glVertex3d(x, z + s, y);
		gl.glEnd();
					
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x, z, y);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(x + s, z, y);
			gl.glTexCoord2f(1, 0); gl.glVertex3d(x + s, z + s, y);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x, z + s, y);
		gl.glEnd();
				
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x + s, z, y);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(x + s, z, y + s);
			gl.glTexCoord2f(1, 0); gl.glVertex3d(x + s, z + s, y + s);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x + s, z + s, y);
		gl.glEnd();
		
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x, z + s, y);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(x + s, z + s, y);
			gl.glTexCoord2f(1, 0); gl.glVertex3d(x + s, z + s, y + s);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x, z + s, y + s);
		gl.glEnd();
		
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(x, z, y + s);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x + s, z , y + s);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x + s, z + s, y + s);
			gl.glTexCoord2f(1, 0); gl.glVertex3d(x , z + s, y + s);
		gl.glEnd();
				
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0, 1); gl.glVertex3d(x, z, y);
			gl.glTexCoord2f(1, 1); gl.glVertex3d(x + s, z, y);
			gl.glTexCoord2f(1, 0); gl.glVertex3d(x + s, z, y + s);
			gl.glTexCoord2f(0, 0); gl.glVertex3d(x, z, y + s);
		gl.glEnd();
		
		MainClass.textures.get(textureID).disable();
		
		gl.glColor3f(1f, 1f, 1f);
		gl.glDisable(GL.GL_COLOR_MATERIAL);

	}

	public void drawLevelExit(GL gl, LevelExit exit, int z2){
		Point2D.Float p = exit.getPoint();
		double s = exit.exitsize;
		double x = p.x * SQUARE_SIZE + 0.5*(SQUARE_SIZE-s);
		double z = p.y * SQUARE_SIZE + 0.5*(SQUARE_SIZE-s);
		double y = z2 - 3.5;

		gl.glPushMatrix();
		gl.glTranslated(x, y, z);
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glLineWidth(20);
		
		gl.glBegin(GL.GL_LINE_LOOP);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(s, 0, 0);
			gl.glVertex3d(s, 0, s);
			gl.glVertex3d(0, 0, s);
		gl.glEnd();
		
		gl.glDisable(GL.GL_COLOR_MATERIAL);
		gl.glLineWidth(1);

		gl.glPopMatrix();
		
	}

	public void drawWall2(GL gl, float sx, float sy, float ex, float ey,String texture, float zfloor, float zroof){
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
	
	public void drawWall(GL gl, float sx, float sy, float ex, float ey,String texture, float zfloor, float zroof){
		if(Math.abs(ex-sx)==0){
			float c=1.0f;
			if(sy>ey){
				c=1.0f;
			}
			else{
				c=-1.0f;
			}
			drawWall2(gl, sx-0.02f, sy+0.019f*c, ex-0.02f, ey-0.019f*c, texture, zfloor, zroof);
			drawWall2(gl, sx+0.02f, sy+0.019f*c, ex+0.02f, ey-0.019f*c, texture, zfloor, zroof);
			drawWall2(gl, sx-0.02f, sy+0.019f*c, sx+0.02f, sy+0.019f*c, texture, zfloor, zroof);
			drawWall2(gl, ex-0.02f, ey-0.019f*c, ex+0.02f, ey-0.019f*c, texture, zfloor, zroof);
		}
		else if(Math.abs(ey-sy)/Math.abs(ex-sx)<1){
			float c = 1.0f;
			if(sx>ex){
				c=1.0f;
			}
			else{
				c=-1.0f;
			}
			drawWall2(gl, sx+0.019f*c, sy-0.02f, ex-0.019f*c, ey-0.02f, texture, zfloor, zroof);
			drawWall2(gl, sx+0.019f*c, sy+0.02f, ex-0.019f*c, ey+0.02f, texture, zfloor, zroof);
			drawWall2(gl, sx+0.019f*c, sy+0.02f, sx+0.019f*c, sy-0.02f, texture, zfloor, zroof);
			drawWall2(gl, ex-0.019f*c, ey+0.02f, ex-0.019f*c, ey-0.02f, texture, zfloor, zroof);
		}
		else if(Math.abs(ey-sy)/Math.abs(ex-sx)>=1){
			float c=1.0f;
			if(sy>ey){
				c=1.0f;
			}
			else{
				c=-1.0f;
			}
			drawWall2(gl, sx-0.02f, sy+0.019f*c, ex-0.02f, ey-0.019f*c, texture, zfloor, zroof);
			drawWall2(gl, sx+0.02f, sy+0.019f*c, ex+0.02f, ey-0.019f*c, texture, zfloor, zroof);
			drawWall2(gl, sx-0.02f, sy+0.019f*c, sx+0.02f, sy+0.019f*c, texture, zfloor, zroof);
			drawWall2(gl, ex-0.02f, ey-0.019f*c, ex+0.02f, ey-0.019f*c, texture, zfloor, zroof);
		}
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
		
		float numTex2 = (float) Math.ceil(disPoints(p.get(0), p.get(1)) / 2);
		float numTex1 = (float) Math.ceil(disPoints(p.get(1), p.get(2)) / 2);

		
		MainClass.textures.get(textureID).bind();		
		gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0, numTex1);
		    gl.glVertex3d(p.get(0).x,p.get(0).y,p.get(0).z);
		    gl.glTexCoord2f(numTex2, numTex1);
		    gl.glVertex3d(p.get(1).x,p.get(1).y,p.get(1).z);
		    gl.glTexCoord2f(numTex2, 0);
		    gl.glVertex3d(p.get(2).x,p.get(2).y,p.get(2).z);
		    gl.glTexCoord2f(0, 0);
			gl.glVertex3d(p.get(3).x,p.get(3).y,p.get(3).z);		
		gl.glEnd();
		
		//Disable texture
		MainClass.textures.get(textureID).disable();
	}
	
	
	public boolean isWall(double x, double y, double z)
	{
		
		ArrayList<Wall> w = new ArrayList<Wall>();
		
		for(int i=0;i<storeys.size();i++){
			storey = storeys.get(i);
			if(y>storey.getFloorHeight()&&y<storey.getRoofHeight()){
				w = storey.getWallList().getWalls();
			}
			
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
	
	public int isPickup(double x, double y, double z){
		
		for(int i = 0; i < storeys.size(); i++){
			
			for(int j = 0; j < storeys.get(i).getPickupList().getPickups().size(); j++){
				
				double xcor = storeys.get(i).getPickupList().getPickups().get(j).getPoint().x;
				double ycor = storeys.get(i).getPickupList().getPickups().get(j).getPoint().y;
				int ret = storeys.get(i).getPickupList().getPickups().get(j).getType();
				
				if(disPoints(new Point3D(x, y, z), new Point3D(xcor * SQUARE_SIZE, ycor * SQUARE_SIZE, 
						storeys.get(i).getFloorHeight() + 2.5)) <= 1){
					storeys.get(i).getPickupList().getPickups().remove(j);
					return ret;
				}
								
			}
			
		}
		
		return 0;
		
	}

	public LevelExit isExit(double x, double y, double z){
		
		for(int i = 0; i < storeys.size(); i++){
			
			for(LevelExit exit : storeys.get(i).getLevelExitList().getExits()){
				
				double s = exit.exitsize;
				double xcor = exit.getPoint().x * SQUARE_SIZE + 0.5*(SQUARE_SIZE-s);
				double zcor = exit.getPoint().y * SQUARE_SIZE + 0.5*(SQUARE_SIZE-s);
				double dy = storeys.get(i).getRoofHeight() - y;

//				System.out.print(x + " , " + y + "	|	");
//				System.out.println(i+ " : "+ xcor + " , " + zcor + " , " + dy);
				
				if( xcor < x && x < xcor+s 
						&& zcor < z && z < zcor+s
						&&	0 < dy && dy < SQUARE_SIZE ){
					return exit;
				}

			}
			
		}

		return null;
	}

	public boolean isFloor(double x, double y, double z){
		ArrayList<Floor> f = new ArrayList<Floor>();
		float floory = Float.MIN_VALUE;
		for(int i=0;i<storeys.size();i++){
			storey = storeys.get(i);
			if(y>storey.getFloorHeight()&&y<storey.getRoofHeight()){
				f = storey.getFloorList().getFloors();
				floory = storey.getFloorHeight();
			}
		}
		for(int i = 0; i < f.size(); i++){
			
			ArrayList<Point2D.Float> points = f.get(i).getPoints();
			ArrayList<Point3D> p3d = new ArrayList<Point3D>();
			for(int k = 0; k < points.size(); k++){
				p3d.add(new Point3D((float)(points.get(k).x * SQUARE_SIZE), floory,(float)(points.get(k).y*SQUARE_SIZE)));
			}
			double distance = distToSurfaceSegment(p3d,x,y-2.5,z);
			if(distance < 0.5 && distance > -0.0001){
				return true;	
			}
		}
		return false;
	}
	
	public boolean throughFloor(double x, double yNew, double yOld,double z){
		ArrayList<Floor> f = new ArrayList<Floor>();
		float floory = Float.MIN_VALUE;
		for(int i=0;i<storeys.size();i++){
			storey = storeys.get(i);
			if(yOld>storey.getFloorHeight()&&yOld<storey.getRoofHeight()){
				f = storey.getFloorList().getFloors();
				floory = storey.getFloorHeight();
			}
		}
		for(int i = 0; i < f.size(); i++){
			
			ArrayList<Point2D.Float> points = f.get(i).getPoints();
			ArrayList<Point3D> p3d = new ArrayList<Point3D>();
			for(int k = 0; k < points.size(); k++){
				p3d.add(new Point3D((float)(points.get(k).x * SQUARE_SIZE), floory,(float)(points.get(k).y*SQUARE_SIZE)));
			}
			double distance = distToSurfaceSegment(p3d,x,yNew-2.5,z);
			if(distance > 0 && distance < Double.MAX_VALUE -1){
				return true;
			}
		}
		return false;
	}
	
	public boolean throughRoof(double x, double yNew, double yOld,double z){
		ArrayList<Roof> r = new ArrayList<Roof>();
		float roofy = Float.MIN_VALUE;
		for(int i=0;i<storeys.size();i++){
			storey = storeys.get(i);
			if(yOld>storey.getFloorHeight()&&yOld<storey.getRoofHeight()){
				r = storey.getRoofList().getRoofs();
				roofy = storey.getRoofHeight();
			}
		}
		for(int i = 0; i < r.size(); i++){
			ArrayList<Point2D.Float> points = r.get(i).getPoints();
			ArrayList<Point3D> p3d = new ArrayList<Point3D>();
			for(int k = 0; k < points.size(); k++){
				p3d.add(new Point3D((float)(points.get(k).x * SQUARE_SIZE), roofy,(float)(points.get(k).y*SQUARE_SIZE)));
			}
			double distance = distToSurfaceSegment(p3d,x,yNew+0.1,z);
			if(distance < 0 && distance < Double.MAX_VALUE-1){
				return true;
			}
		}
		return false;
	}
	
	public double isRamp(double x, double y, double z){
		
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
			
			// Check if on ramp
			double distance = distToSurfaceSegment(p3d,x,y-2.5,z);
			if(distance < 0.5 && distance > -0.5){
				double dy = (- surface[3]- surface[0]*x - surface[2]*z)/surface[1] - (y -2.5);
				return dy;				
			}
			else if(distance < 2.5 && distance >= 0.5){
				return Double.MIN_VALUE;
			}
			
		}
		return Double.MAX_VALUE;
		
		
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
	
	
	public double distToSegment(double px, double py, double vx, double vy, double wx, double wy) 
	{ 
		return Math.sqrt(distToSegmentSquared(px, py, vx, vy, wx, wy)); 
	}
	
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
	
	public float getFloorHeight(double y){
		for(int i=0;i<storeys.size();i++){
			storey = storeys.get(i);
			if(y>storey.getFloorHeight()&&y<storey.getRoofHeight()){
				return storey.getFloorHeight();
			}
		}
		return Float.MIN_VALUE;
	}
	
	private double disPoints(Point3D p1, Point3D p2){
			
		double r = Math.sqrt(Math.pow((p2.y - p1.y), 2) + Math.pow((p2.x - p1.x), 2) + Math.pow((p2.z - p1.z), 2));	
		r = Math.round(r);
		return r;		
	}
	
	public ArrayList<Enemy> loadEnemies(){
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		for(int i = 0; i<numberOfStoreys;i++){
			storey = storeys.get(i);
			ArrayList<Object> o1 = storey.getObjectList().getObjects();
			for(int j = 0; j < o1.size(); j++){	
				if(o1.get(j) instanceof ObjectEnemy){
					
					ObjectEnemy t = (ObjectEnemy) o1.get(j);
					Point2D.Float p = t.getPoints().get(0); 
					enemies.add(new Enemy(	p.x * MainClass.maze.SQUARE_SIZE + MainClass.maze.SQUARE_SIZE / 2, 	// x-position
							storey.getFloorHeight()+ 0.01 * MainClass.maze.SQUARE_SIZE,							// y-position
							p.y * MainClass.maze.SQUARE_SIZE + MainClass.maze.SQUARE_SIZE / 2,0, true, t.getModel()));	// z-position, texture boolean				 				
				}
			}
		}
		
		
		return enemies;
	}
}