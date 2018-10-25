package com.ru.tgra.shapes;

import java.util.Random;

public class Particle{
	public Point3D position;
	public Point3D color;
	public Vector3D direction;
	public float size;
	public float life;
	
	public Particle(Point3D position, Vector3D direction, Point3D color, float size) {
		this.position = position;
		this.direction = direction;
		this.color = color;
		this.size = size;
		this.life = 1.0f;
	}
	
	public Particle(Point3D position, Vector3D direction, Point3D color, float size, float life) {
		this.position = position;
		this.direction = direction;
		this.color = color;
		this.size = size;
		this.life = life;
	}
	
	public void simulate(float lifeConst) {
		
		this.position.x += direction.x;
		this.position.y += direction.y;
		this.position.z += direction.z;
		this.life*= lifeConst;
	}
}