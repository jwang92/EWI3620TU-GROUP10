package LevelEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import poly2tri.Poly2Tri;
import poly2tri.geometry.polygon.PolygonPoint;
import poly2tri.geometry.polygon.PolygonPoly2Tri;
import poly2tri.geometry.primitives.Edge;
import poly2tri.geometry.primitives.Point;
import poly2tri.triangulation.TriangulationPoint;
import poly2tri.triangulation.delaunay.DelaunayTriangle;
import poly2tri.triangulation.delaunay.sweep.DTSweepConstraint;
import poly2tri.triangulation.point.TPoint;
import clipper.ClipType;
import clipper.Clipper;
import clipper.IntPoint;
import clipper.PolyFillType;
import clipper.PolygonClipper;
import clipper.internal.JoinType;
import clipper.internal.PolyType;
import Maze.Floor;
import Maze.FloorList;
import Maze.Storey;
import Maze.Wall;
import Maze.WallList;

public class NavMeshGeneration {
	
	//Storey
	private Storey storey = new Storey();
	private ArrayList<Storey> storeys;
	
	//WalkablePolygons
	public ArrayList<PolygonClipper> walkablePolygonsClipper;
	public ArrayList<PolygonClipper> blockedPolygonsClipper;
	public ArrayList<PolygonClipper> resultClipper;
	public ArrayList<PolygonClipper> tempClipper;
	
	public ArrayList<PolygonPoly2Tri> walkablePolygonsPoly2Tri;
	public ArrayList<PolygonPoly2Tri> blockedPolygonsPoly2Tri;
	public ArrayList<PolygonPoly2Tri> resultPoly2Tri;
	public ArrayList<PolygonPoly2Tri> tempPoly2Tri;
	public ArrayList<DelaunayTriangle> triangleListPoly2Tri;
	
	//TriangleID
	public HashMap<Integer,DelaunayTriangle> triangleIDs;
	public HashMap<Integer,TriangulationPoint> vertexIDs;
	public HashMap<DTSweepConstraint, ArrayList<Integer>> edgeToTriangles;
	public ArrayList<Point> vertices;
	
	public ArrayList<DTSweepConstraint> edges;
	public ArrayList<ArrayList<Integer>> edgesToTriangles;
	
	//Clipper
	private Clipper clipper;
	
	//Clipper gridsize;
	private int gridSize = 1000;
	
	public NavMeshGeneration(ArrayList<Storey> storeys){
		this.storeys = storeys;
		walkablePolygonsClipper = new ArrayList<PolygonClipper>();
		blockedPolygonsClipper = new ArrayList<PolygonClipper>();
		tempClipper = new ArrayList<PolygonClipper>();
		clipper = new Clipper();
		
		generateWalkablePolygons();
		MergeWalkablePolygons();
		generateBlockAreas();
		increaseBlockedAreaSize();
		RemoveBlockedArea();
		Triangulate();
		getNeighbours();
	}
	
	public ArrayList<DelaunayTriangle> getResult(){
		return triangleListPoly2Tri;
	}
	
	public void generateWalkablePolygons(){
		if(storeys.size()>0){
			storey = storeys.get(0);
			FloorList floors = storey.getFloorList();
			int sizeX = storey.getSizeX();
			int sizeY = storey.getSizeY();
			for(int i =0;i<floors.getFloors().size(); i++){
				Floor f = floors.getFloors().get(i);
				PolygonClipper p = new PolygonClipper();
				for(int j = 0; j < 4;j++){
					int tempX = (int) (f.getPoints().get(j).x-1)*gridSize;
					int tempY = (int) (f.getPoints().get(j).y-1)*gridSize;
					if(tempX == 0){
						tempX += 350;
					}
					else if(tempX == sizeX * gridSize){
						tempX -= 350;
					}
					
					if(tempY == 0){
						tempY += 350;
					}
					else if(tempY == sizeY * gridSize){
						tempY -= 350;
					}
					
					
					if(j == 1 || j == 2){
						tempY += 1;
					}
					if(j == 2 || j == 3){
						tempX += 1;
					}
					p.add(new IntPoint(tempX,tempY));
				}

//				p.add(new IntPoint( (int) (f.getPoints().get(0).x-1)*gridSize, (int) (f.getPoints().get(0).y-1)*gridSize));
//				p.add(new IntPoint( (int) (f.getPoints().get(1).x-1)*gridSize, (int) (f.getPoints().get(1).y-1)*gridSize +1));
//				p.add(new IntPoint( (int) (f.getPoints().get(2).x-1)*gridSize +1, (int) (f.getPoints().get(2).y-1)*gridSize +1));
//				p.add(new IntPoint( (int) (f.getPoints().get(3).x-1)*gridSize +1, (int) (f.getPoints().get(3).y-1)*gridSize));
				walkablePolygonsClipper.add(p);			
			}
		}
	}
	
	public void MergeWalkablePolygons(){
		clipper.addPolygons(walkablePolygonsClipper, PolyType.ptClip);
		clipper.execute(ClipType.UNION, tempClipper, PolyFillType.NONZERO, PolyFillType.NONZERO);
	}
	
	public void generateBlockAreas(){
		if(storeys.size()>0){
			storey = storeys.get(0);
			WallList walls = storey.getWallList();
			for(int i=0;i<walls.getWalls().size();i++){
				Wall w = walls.getWalls().get(i);
				if((w.getStartx()-1 != 0) && w.getEndx()-1 != 0){
					PolygonClipper p = getWallTop(w.getStartx()-1,w.getStarty()-1,w.getEndx()-1,w.getEndy()-1);
					blockedPolygonsClipper.add(p);
				}
			}
		}
	}
	
	public void increaseBlockedAreaSize(){
		blockedPolygonsClipper= (ArrayList<PolygonClipper>) Clipper.offsetPolygons(blockedPolygonsClipper, 350, JoinType.jtSquare);
	}
	
	public void RemoveBlockedArea(){
		for(PolygonClipper p : blockedPolygonsClipper){
			clipper = new Clipper();
			clipper.addPolygons(tempClipper, PolyType.ptSubject);
			clipper.addPolygon(p, PolyType.ptClip);
			tempClipper.clear();
			clipper.execute(ClipType.DIFFERENCE, tempClipper);
		}
		resultClipper = tempClipper;
	}
	
	public void Triangulate(){
		walkablePolygonsPoly2Tri = new ArrayList<PolygonPoly2Tri>();
		blockedPolygonsPoly2Tri = new ArrayList<PolygonPoly2Tri>();
		triangleListPoly2Tri = new ArrayList<DelaunayTriangle>();
		
		for(int i = 0; i<resultClipper.size(); i++){
			PolygonClipper pClipper = resultClipper.get(i);
			List<PolygonPoint> points = new ArrayList<PolygonPoint>();
			for(int j =0; j<pClipper.size();j++){
				IntPoint intpoint = pClipper.get(j);
				points.add(new PolygonPoint(intpoint.x,intpoint.y));
			}
			PolygonPoly2Tri pPoly2Tri = new PolygonPoly2Tri(points);
			
			//Orientation of the polygon (winding order) from clipper determines if the polygon is walkable or blocked
			//This results in the input for poly2tri
			if(Clipper.orientation(pClipper)){
				//System.out.println(i + "walkable");
				walkablePolygonsPoly2Tri.add(pPoly2Tri);
			}
			else{
				//System.out.println(i + "blocked");
				blockedPolygonsPoly2Tri.add(pPoly2Tri);
			}
		}
		//System.out.println(walkablePolygonsPoly2Tri.size());
		for(int i =0; i<walkablePolygonsPoly2Tri.size();i++){
			PolygonPoly2Tri w = walkablePolygonsPoly2Tri.get(i);
				for(int j=0; j<blockedPolygonsPoly2Tri.size();j++){
					PolygonPoly2Tri b = blockedPolygonsPoly2Tri.get(j);
					List<TriangulationPoint> blocked = b.getPoints();
					List<TriangulationPoint> walkable = w.getPoints();
					if(PolyInPoly(walkable,blocked)){
						w.addHole(blockedPolygonsPoly2Tri.get(j));
					}
										
				}
			Poly2Tri.triangulate(w);
			triangleListPoly2Tri.addAll(w.getTriangles());
		}
		
	}
	
	public void getNeighbours(){
		groupTrianglesSharingEdges();
		getTriangleNeighbours();
	}

	private void groupTrianglesSharingEdges(){
		// group all triangles that share an edge
		edgeToTriangles = new HashMap<DTSweepConstraint,ArrayList<Integer>>();
		edges = new ArrayList<DTSweepConstraint>();
		edgesToTriangles = new ArrayList<ArrayList<Integer>>() ;
		ArrayList<DTSweepConstraint> tempedges;
		for(int i=0; i<triangleListPoly2Tri.size(); i++){
			
			DelaunayTriangle t = triangleListPoly2Tri.get(i);
			tempedges = getTriangleEdges(t);
			
			for(DTSweepConstraint e: tempedges){
				ArrayList<Integer> temp = null;
				if(edges.contains(e)){
					int edgeID = edges.indexOf(e);
					//System.out.println(edgeID);
					temp = edgesToTriangles.get(edgeID);
					temp.add(i);
					edgesToTriangles.set(edgeID,temp);
				}
				else if(temp == null){
					temp = new ArrayList<Integer>();
					temp.add(i);
					edges.add(e);
					edgesToTriangles.add(temp);
				}
			}
		}
	}
	
	private void getTriangleNeighbours(){
		// gather each triangle's neighbours
		for(int i=0;i<triangleListPoly2Tri.size();i++){
			DelaunayTriangle t = triangleListPoly2Tri.get(i);
			ArrayList<DTSweepConstraint> tempedges = getTriangleEdges(t);

			for(DTSweepConstraint e: tempedges){
				ArrayList<Integer> temp = null;
				if(edges.contains(e)){
					int edgeID = edges.indexOf(e);
					temp = edgesToTriangles.get(edgeID);
					for(int j=0;j<temp.size();j++){
						//System.out.println(temp.get(i));
						DelaunayTriangle neighbour = triangleListPoly2Tri.get(temp.get(j));
						if(!t.equals(neighbour)){
							t.neighbors[j] = neighbour; 
						}
						else{
							//System.out.println("test");
						}
					}
				}
				else{
					System.out.println("Edge missing in triangles sharing edges");
				}
				

			}
		}
	}
	
//	private ArrayList<TriangulationPoint> generateSteinerPoints(){
//		ArrayList<TriangulationPoint> SteinerPoints = new ArrayList <TriangulationPoint>();
//		int sizeX = storey.getSizeX() - 2;
//		int sizeY = storey.getSizeY() - 2;
//		double SteinerDistance = 0.5;
//		for(double i = 0.25; i<sizeX; i += SteinerDistance){
//			for(double j = 0.25; j<sizeY; j += SteinerDistance){
//				System.out.println("test");
//				TriangulationPoint temp = new TPoint(i*gridSize,j*gridSize); 
//				SteinerPoints.add(temp);
//			}
//		}
//		
//		return SteinerPoints;
//	}
	
	private ArrayList<DTSweepConstraint> getTriangleEdges(DelaunayTriangle t){
		ArrayList<DTSweepConstraint> res = new ArrayList<DTSweepConstraint>();
		DTSweepConstraint edge1 = new DTSweepConstraint(t.points[0], t.points[1]);
		DTSweepConstraint edge2 = new DTSweepConstraint(t.points[1], t.points[2]);
		DTSweepConstraint edge3 = new DTSweepConstraint(t.points[2], t.points[0]);
		
		res.add(edge1);
		res.add(edge2);
		res.add(edge3);
		
		return res;
	}
	
	private PolygonClipper getWallTop(float sx, float sy, float ex, float ey){
		PolygonClipper wallTop = new PolygonClipper();
		
		float dx = ex - sx; //delta x
		float dy = ey - sy; //delta y
		float linelength = (float) Math.sqrt(dx * dx + dy * dy);
		dx = dx / linelength;
		dy = dy / linelength;
		
		//(dx, dy) is a unit vector pointing in the direction of the line
		//A perpendicular vector is given by (-dy, dx)
		float thickness = 0.04f; //Some number
		float perx = 0.5f * thickness * (-dy); //perpendicular vector with length thickness * 0.5
		float pery = 0.5f * thickness * dx;
		float parx = 0.5f * thickness * (-dy); //Parallel with length thickness * 0.5
		float pary = 0.5f * thickness * dx;
		wallTop.add((int)(gridSize * sx - perx - parx), (int)(gridSize * sy + pery + pary));
		wallTop.add((int)(gridSize * ex - perx - parx), (int)(gridSize * ey + pery + pary));
		wallTop.add((int)(gridSize * ex + perx + parx), (int)(gridSize * ey - pery - pary));
		wallTop.add((int)(gridSize * sx + perx + parx), (int)(gridSize * sy - pery - pary));
		
//		if(Math.abs(ex-sx)==0){
//		float c=1.0f;
//			if(sy>ey){
//				c=1.0f;
//			}
//			else{
//				c=-1.0f;
//			}
//			wallTop.add((int)((sx-0.02f)), (int)(gridSize * (sy+0.019f*c)));
//			wallTop.add((int)(gridSize * (ex-0.02f)), (int)(gridSize * (ey-0.019f*c)));
//			wallTop.add((int)(gridSize * (ex+0.02f)), (int)(gridSize * (ey-0.019f*c)));
//			wallTop.add((int)(gridSize * (sx+0.02f)), (int)(gridSize * (sy+0.019f*c)));
//		}
//		else if(Math.abs(ey-sy)/Math.abs(ex-sx)<1){
//			float c = 1.0f;
//			if(sx>ex){
//				c=1.0f;
//			}
//			else{
//				c=-1.0f;
//			}
//			wallTop.add((int)(gridSize * (sx+0.019f*c)), (int)(gridSize * (sy-0.02f)));
//			wallTop.add((int)(gridSize * (ex-0.019f*c)), (int)(gridSize * (ey-0.02f)));
//			wallTop.add((int)(gridSize * (ex-0.019f*c)), (int)(gridSize * (ey+0.02f)));
//			wallTop.add((int)(gridSize * (sx+0.019f*c)), (int)(gridSize * (sy+0.02f)));
//		}
//		else if(Math.abs(ey-sy)/Math.abs(ex-sx)>=1){
//			float c=1.0f;
//			if(sy>ey){
//				c=1.0f;
//			}
//			else{
//				c=-1.0f;
//			}
//			wallTop.add((int)(gridSize * (sx-0.02f)), (int)(gridSize * (sy+0.019f*c)));
//			wallTop.add((int)(gridSize * (ex-0.02f)), (int)(gridSize * (ey-0.019f*c)));
//			wallTop.add((int)(gridSize * (ex+0.02f)), (int)(gridSize * (ey-0.019f*c)));
//			wallTop.add((int)(gridSize * (sx+0.02f)), (int)(gridSize * (sy+0.019f*c)));
//		}
		return wallTop;
	}
	
	private boolean PolyInPoly(List<TriangulationPoint> walkable,List<TriangulationPoint> blocked){
		for(int i=0;i<blocked.size();i++){
			if(!PointInPoly(walkable,blocked.get(i).getXf(),blocked.get(i).getYf())){
				return false;
			}
		}		
		return true;
	}
	
	private boolean PointInPoly(List<TriangulationPoint> walkable, float testx, float testy)
	{	
		int nvert = walkable.size();
		float[] vertx = new float[walkable.size()];
		float[] verty = new float[walkable.size()];
		
		float xMin = 0,xMax = 0,yMin = 0,yMax = 0;
		
		for(int i=0; i<walkable.size();i++){
			vertx[i] = walkable.get(i).getXf();
			verty[i] = walkable.get(i).getYf();
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
}
