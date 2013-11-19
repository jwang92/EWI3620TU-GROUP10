
public class Point3D {
	protected float x;
	protected float y;
	protected float z;
	
	public Point3D(){
		x=0.0f;
		y=0.0f;
		z=0.0f;
	}
	
	public Point3D(float x,float y,float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getZ(){
		return z;
	}

}
