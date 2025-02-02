package com.ru.tgra.shapes;

public class Point3D {

	public float x;
	public float y;
	public float z;

	public Point3D()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Point3D(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void add(Vector3D v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	static Point3D add(Point3D p, Vector3D v) {
		return new Point3D(p.x + v.x, p.y + v.y, p.z + v.z);
	}
}
