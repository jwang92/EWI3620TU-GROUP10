package LevelEditor;

import java.util.ArrayList;

import javax.media.opengl.GL;

import Maze.Storey;
import Utils.Point3D;
import poly2tri.triangulation.TriangulationPoint;
import poly2tri.triangulation.delaunay.DelaunayTriangle;

public class NavMesh {
	public ArrayList<DelaunayTriangle> triangles;
	
	/**
	 * Create a navmesh of a storey
	 * @param storeys Storeys
	 * @param storeyID ID of the storey to load
	 */
	public NavMesh(ArrayList<Storey> storeys, int storeyID){
		NavMeshGeneration navMeshGen = new NavMeshGeneration(storeys,storeyID);
		triangles = navMeshGen.getResult();
	}
	
	/**
	 * Create a navmesh for multiple storeys
	 * @param storeys storeys to load
	 * @return return the navmesh
	 */
	public static ArrayList<NavMesh> createMultiLayerNavMesh(ArrayList<Storey> storeys){
		ArrayList<NavMesh> res = new ArrayList<NavMesh>();
		for(int i =0; i<storeys.size();i++){
			res.add(new NavMesh(storeys,i));			
		}
		return res;
	}
	
	/**
	 * Find a point route between s and e
	 * @param sx
	 * @param sy
	 * @param ex
	 * @param ey
	 * @return return the route
	 */
	public synchronized ArrayList<Point3D> findRoute(float sx, float sy, float ex, float ey){
		sx = sx*1000;
		sy = sy*1000;
		ex = ex*1000;
		ey = ey*1000;
		int firstTriangleID = findTriangle(sx,sy);
		int endTriangleID = findTriangle(ex,ey);
		
		//System.out.println(firstTriangleID +", " + endTriangleID);
		
		if(firstTriangleID < 0 || endTriangleID <0){
			resetNavMesh();
			return null;
		}
		
		ArrayList<DelaunayTriangle> closed = findTriangleRoute(sx,sy,ex,ey,firstTriangleID,endTriangleID);
		
		if(closed == null){
			resetNavMesh();
			return null;
		}
		ArrayList<Point3D> res = findPointRoute(sx,sy,ex,ey,firstTriangleID,endTriangleID,closed);
		
		resetNavMesh();
		return res;
	}
	
	/**
	 *  Find a point route between s and e with given triangles according to funnel algorithm
	 * @param sx
	 * @param sy
	 * @param ex
	 * @param ey
	 * @param firstTriangleID triangle containing s
	 * @param endTriangleID triangle containing e
	 * @param closed triangle route
	 * @return
	 */
	public synchronized ArrayList<Point3D> findPointRoute(float sx, float sy, float ex, float ey, int firstTriangleID, int endTriangleID, ArrayList<DelaunayTriangle> closed){
		ArrayList<Point3D> route = new ArrayList<Point3D>();
		
		Point3D end = new Point3D(ex/1000,0,ey/1000);
		route.add(end);
		
		DelaunayTriangle firstTriangle = triangles.get(firstTriangleID);
		DelaunayTriangle endTriangle = triangles.get(endTriangleID);
		
		DelaunayTriangle previousTriangle = endTriangle;
		DelaunayTriangle currentTriangle = endTriangle.parent;
		currentTriangle.setChild(endTriangle);
		
		TriangulationPoint p1 = null;
		TriangulationPoint p2 = null;
		
		while(!currentTriangle.equals(firstTriangle)){
			boolean updateCorner = false;
			ArrayList<TriangulationPoint> sharingPoints = currentTriangle.getSharingPoints(previousTriangle);
			
			TriangulationPoint temp1 = sharingPoints.get(0);
			TriangulationPoint temp2 = sharingPoints.get(1);

			Point3D currentCorner = route.get(route.size()-1);
			if(p1 == null && p2 == null){
				
				//If temp2 is on the correct side of the line temp1, currentcorner --> p1 = temp1, p2 = temp2
				//If temp2 is on the other side, the points should be swapped --> p1 = temp2, p2 = temp1
				//Result: p2 is on the correct side of the line p1, currentcorner
				if(SideOfLine(currentCorner.getX(), currentCorner.getZ(), temp1.getX()/1000, temp1.getY()/1000, temp2.getX()/1000, temp2.getY()/1000)){
					p1 = temp1;
					p2 = temp2;
				}
				else{
					p1 = temp2;
					p2 = temp1;
				}
			}
			else{
				boolean sideOfLine1Temp1;
				boolean sideOfLine2Temp1;
				boolean sideOfLine1Temp2;
				boolean sideOfLine2Temp2;
				sideOfLine1Temp1 = SideOfLine(currentCorner.getX(), currentCorner.getZ(), p1.getX()/1000, p1.getY()/1000, temp1.getX()/1000, temp1.getY()/1000);
				sideOfLine2Temp1 = SideOfLine(currentCorner.getX(), currentCorner.getZ(), p2.getX()/1000, p2.getY()/1000, temp1.getX()/1000, temp1.getY()/1000);
				sideOfLine1Temp2 = SideOfLine(currentCorner.getX(), currentCorner.getZ(), p1.getX()/1000, p1.getY()/1000, temp2.getX()/1000, temp2.getY()/1000);
				sideOfLine2Temp2 = SideOfLine(currentCorner.getX(), currentCorner.getZ(), p2.getX()/1000, p2.getY()/1000, temp2.getX()/1000, temp2.getY()/1000);
				
				boolean outsideTemp1 = (sideOfLine1Temp1 == sideOfLine2Temp1);
				boolean outsideTemp2 = (sideOfLine1Temp2 == sideOfLine2Temp2);
				
				//Both temp1 and temp2 are outside
				if(outsideTemp1 && outsideTemp2){
					
					if(sideOfLine1Temp1 == sideOfLine1Temp2){
						//Update one of the corners
						if(sideOfLine1Temp1 == true){
							//Update p2 to be a corner
							Point3D tempCorner = new Point3D(p2.getX()/1000,0,p2.getY()/1000);
							route.add(tempCorner);
							boolean found = false;
							while(!found){
								if(currentTriangle.contains(p2)){
									found = true;
								}
								else{
									currentTriangle = currentTriangle.getChild();
								}			
							}
							
							p1 = null;
							p2 = null;

						}
						
						else{
							//Update P1 to be a corner
							Point3D tempCorner = new Point3D(p1.getX()/1000,0,p1.getY()/1000);
							route.add(tempCorner);
							
							boolean found = false;
							while(!found){
								if(currentTriangle.contains(p1)){
									found = true;
								}
								else{
									currentTriangle = currentTriangle.getChild();
								}			
							}
							
							p1 = null;
							p2 = null;
						}
					}
					else{
						//do nothing
					}
				}
				//Temp1 is outside so update with temp2
				else if(outsideTemp1){
					if(sideOfLine1Temp1 == true){
						p1 = temp2;
					}
					else{
						p2 = temp2;
					}
				}
				//Temp2 is outside so update with temp1
				else if(outsideTemp2){
					if(sideOfLine1Temp2 == true){
						p1 = temp1;
					}
					else{
						p2 = temp1;
					}
				}
				//Both are inside so update with both temp1 and temp2
				else{
					if(SideOfLine(currentCorner.getX(), currentCorner.getZ(), temp1.getX()/1000, temp1.getY()/1000, temp2.getX()/1000, temp2.getY()/1000)){
						p1 = temp1;
						p2 = temp2;
					}
					else{
						p1 = temp2;
						p2 = temp1;
					}
				}
				
				
			}
			if(!updateCorner){
				previousTriangle = currentTriangle;
				currentTriangle = currentTriangle.parent;
				currentTriangle.setChild(previousTriangle);
			}

		}
		
		Point3D start = new Point3D(sx/1000,0,sy/1000);
		route.add(start);
		return route;
	}
	
	/**
	 * Find triangle route between s and e
	 * @param sx
	 * @param sy
	 * @param ex
	 * @param ey
	 * @param firstTriangleID triangle containing s
	 * @param endTriangleID triangle containing e
	 * @return return triangle route
	 */
	public synchronized ArrayList<DelaunayTriangle> findTriangleRoute(float sx, float sy, float ex, float ey, int firstTriangleID, int endTriangleID){
		
		ArrayList<DelaunayTriangle> closed = new ArrayList<DelaunayTriangle>();
		ArrayList<DelaunayTriangle> open = new ArrayList<DelaunayTriangle>();
		
		DelaunayTriangle firstTriangle = triangles.get(firstTriangleID);
		DelaunayTriangle endTriangle = triangles.get(endTriangleID);
		
		firstTriangle.setG(0);
		open.add(firstTriangle);
		
		int bestTriangle = 0;
		
		//While closed doesn't contain end triangle
		int result = 0;
		while(result == 0){
			DelaunayTriangle currentTriangle = open.get(bestTriangle);
			closed.add(currentTriangle);
			
			for(int i=0;i<currentTriangle.neighbors.length;i++){
				DelaunayTriangle neighbor = currentTriangle.neighbors[i];
				
				//If the open list doesn't already contains the neighbor, if the closed list doesn't already contain the neighbor
				//and if the neighbor is in triangles the next part should be executed 
				if(!open.contains(neighbor) && !closed.contains(neighbor) &&  triangles.indexOf(neighbor) >= 0){
					open.add(neighbor);					
					double g = currentTriangle.getG() + neighbor.distance(currentTriangle);
					
					//update h
					if(neighbor.getH()==0){
						double h = neighbor.distance(ex, ey);
						neighbor.setH(h);
					}
					
					//update g
					if(g < neighbor.getG()){
						neighbor.setParent(currentTriangle);
						neighbor.setG(g);
					}
					//refresh f
					neighbor.refreshF();
				}
				//If closed contains the neighbour only update g and f
				else if(!closed.contains(neighbor) &&  triangles.indexOf(neighbor) >= 0){
					double g = currentTriangle.getG() + neighbor.distance(currentTriangle);
					if(g < neighbor.getG()){
						neighbor.setParent(currentTriangle);
						neighbor.setG(g);
					}
					neighbor.refreshF();
				}
				
			}
			
			//Find the new triangle
			bestTriangle = -1;
			double minF = Double.MAX_VALUE;
			for(int i=0;i<open.size();i++){
				DelaunayTriangle t = open.get(i);
				if(t.getF() < minF && !closed.contains(t)){
					minF = t.getF();
					bestTriangle = i;
				}
				
			}
			
			if(closed.contains(endTriangle)){
				return closed;
			}
			
			else if(bestTriangle == -1){
				return null;
			}
		}
		
		return null;
		
	}
	
	/**
	 * Reset the navmesh so it can be used again
	 */
	public synchronized void resetNavMesh(){
		for(int i =0;i<triangles.size();i++){
			DelaunayTriangle t = triangles.get(i);
			t.setChild(null);
			t.setParent(null);
			t.setG(Double.MAX_VALUE);
			t.setH(0);
			t.refreshF();
		}
	}
	
	/**
	 * find a triangle containing point x,y
	 * @param x
	 * @param y
	 * @return return the id of the triangle
	 */
	public int findTriangle(float x,float y){
		for(int i =0; i<triangles.size();i++){
			DelaunayTriangle t = triangles.get(i);
			if(PointInPoly(t.points,x,y)){
				return i;
			}
		}
		return -1;
	}	
	
	/**
	 * Drawing of the navmesh in the levelEditor
	 * @param gl gl to draw the navMesh on
	 * @param gridOffsetX Offset of the grid in the x dimension in the leveleditor
	 * @param gridOffsetY Offset of the grid in the y dimension in the leveleditor
	 * @param gridDistance Distance between the gridpoints in the leveleditor
	 * @param screenHeight Height of the screen
	 */
	public void drawNavMeshEditor(GL gl,float gridOffsetX,float gridOffsetY,float gridDistance, float screenHeight){
		for(int i =0; i<triangles.size();i++){
			DelaunayTriangle t = triangles.get(i);
			float x1 = gridOffsetX + t.points[0].getXf()/1000*gridDistance;
			float y1 = screenHeight -gridOffsetY - t.points[0].getYf()/1000*gridDistance;
			float x2 = gridOffsetX + t.points[1].getXf()/1000*gridDistance;
			float y2 = screenHeight -gridOffsetY - t.points[1].getYf()/1000*gridDistance;
			float x3 = gridOffsetX + t.points[2].getXf()/1000*gridDistance;
			float y3 = screenHeight -gridOffsetY - t.points[2].getYf()/1000*gridDistance;
			triangleOnScreen2D(gl,x1,y1,x2,y2,x3,y3);
		}
	}
	
	/**
	 * Drawing of the navmesh in game to do visual testing
	 * @param gl gl to draw the navMesh on
	 * @param SQUARE_SIZE size of one square in the maze
	 */
	public void drawNavMeshGame(GL gl,float SQUARE_SIZE){
		for(int i =0; i<triangles.size();i++){
			DelaunayTriangle t = triangles.get(i);
			float x1 = (1+t.points[0].getXf()/1000)*SQUARE_SIZE;
			float z1 = (1+t.points[0].getYf()/1000)*SQUARE_SIZE;
			float x2 = (1+t.points[1].getXf()/1000)*SQUARE_SIZE;
			float z2 = (1+t.points[1].getYf()/1000)*SQUARE_SIZE;
			float x3 = (1+t.points[2].getXf()/1000)*SQUARE_SIZE;
			float z3 = (1+t.points[2].getYf()/1000)*SQUARE_SIZE;
			triangleOnScreen3D(gl,x1,z1,x2,z2,x3,z3,i);
		}
	}
	
	/**
	 * Help method that uses Gl calls to draw a triangle in 2D
	 * @param gl gl to be drawn on
	 * @param x1 x coordinate of point1
	 * @param y1 y coordinate of point1
	 * @param x2 x coordinate of point2
	 * @param y2 y coordinate of point2
	 * @param x3 x coordinate of point3
	 * @param y3 y coordinate of point3
	 */
	private void triangleOnScreen2D(GL gl, float x1, float y1, float x2, float y2, float x3, float y3) {
		gl.glColor3f(0f, 1.0f,0f);
		gl.glBegin(GL.GL_LINE_LOOP);
			gl.glVertex2f(x1, y1);
			gl.glVertex2f(x2, y2);
			gl.glVertex2f(x3, y3);
		gl.glEnd();
		gl.glColor3f(1.0f, 0f,0f);
	}
	
	/**
	 *  Help method that uses Gl calls to draw a triangle in 3D
	 * @param gl gl to be drawn on
	 * @param x1 x coordinate of point1
	 * @param z1 z coordinate of point1
	 * @param x2 x coordinate of point2
	 * @param z2 z coordinate of point2
	 * @param x3 x coordinate of point3
	 * @param z3 z coordinate of point3
	 */
	private void triangleOnScreen3D(GL gl, float x1, float z1, float x2, float z2, float x3, float z3, int i) {
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		gl.glColor3f(0f, 1.0f,0f);
		
//		if(i == 4){
//			gl.glColor3f(1.0f, 0f, 0f);
//		}
		gl.glBegin(GL.GL_LINE_LOOP);
			gl.glVertex3f(x1, 0.1f,z1);
			gl.glVertex3f(x2, 0.1f,z2);
			gl.glVertex3f(x3, 0.1f,z3);
		gl.glEnd();
		gl.glColor3f(1.0f, 1.0f,1.0f);
		gl.glDisable(GL.GL_COLOR_MATERIAL);
	}
	
	/**
	 * Returns true if testx,testy is in the polygon build with walkable
	 * @param walkable
	 * @param testx
	 * @param testy
	 * @return Returns true if testx,testy is in the polygon build with walkable
	 */
	private boolean PointInPoly(TriangulationPoint[] walkable, float testx, float testy)
	{	
		int nvert = walkable.length;
		float[] vertx = new float[walkable.length];
		float[] verty = new float[walkable.length];
		
		float xMin = 0,xMax = 0,yMin = 0,yMax = 0;
		
		for(int i=0; i<walkable.length;i++){
			vertx[i] = walkable[i].getXf();
			verty[i] = walkable[i].getYf();
			if(i==0){
				xMin = vertx[i];
				xMax = vertx[i];
				yMin = verty[i];
				yMax = verty[i];
			}
			else{
				if(vertx[i]<xMin){
					xMin = vertx[i];
				}
				else if(vertx[i]>xMax){
					xMax = vertx[i];
				}
				
				if(verty[i]<yMin){
					yMin = verty[i];
				}
				else if(verty[i]>yMax){
					yMax = verty[i];
				}
				
			}
		}
		
		if (testx < xMin || testx > xMax || testy < yMin || testy > yMax) {
		    return false;
		}
		
		int i, j;
		boolean c = false;
		j = nvert-1;
		for (i = 0; i < nvert; i++) {
			if((verty[i]>testy) != (verty[j]>testy)){
				if(testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]){
					c = !c;
				}

			}
			j = i;
		}
	  return c;
	}
	
	/**
	 * Find on which side of the line the point is
	 * @param lineX1 
	 * @param lineY1
	 * @param lineX2
	 * @param lineY2
	 * @param pointX
	 * @param pointY
	 * @return true if the point is left (when it is a vertical line) 
	 */
	public boolean SideOfLine(double lineX1, double lineY1, double lineX2, double lineY2,double pointX, double pointY){
	     return ((lineX2 - lineX1)*(pointY - lineY1) - (lineY2 - lineY1)*(pointX - lineX1)) > 0;
	}
}
