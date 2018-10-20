package com.ru.tgra.shapes;

import java.util.ArrayList;

/*
 * Player is a particle with a directional vector
 * */
public class Player {
	
	
	protected Vector3D direction;
	protected Vector3D normalizedDirection;
	protected Point3D  position;
	protected Camera cam;
	protected float angleY;
	
	public Player(Point3D position, Vector3D direction) {
		this.direction = direction;
		this.position  = position;
		this.angleY    = 0.0f;
	}
	
	public Point3D position() {
		return this.position;
	}
	
	public Vector3D direction() {
		return this.direction;
	}
	
	public void setCamera(Camera cam) {
		this.cam = cam;
	}
	
	
	public void updateCam() {
		
	}
	
	public void move(float dt) {
		//Check for collision
	
		position.x += direction.x*dt;
		position.y += direction.y*dt;
		position.z += direction.z*dt;
		
		
	}
	
	public void slide(float dt) {
		//Check for collision
		
		
		
	}
	
	public void rotateY(float angle, float dt) {
		
		float radians = (angle * (float)Math.PI / 180.0f) * dt;
		float c       = (float)Math.cos(radians);
		float s       = -(float)Math.sin(radians);
		
		this.angleY += angle*dt;
		
		this.direction = new Vector3D(c*direction.x - s*direction.z,
				 					  direction.y,
				 					  s*direction.x + c*direction.z);
		
		
		//System.out.println(this.direction.x + ", " + this.direction.y + ", " +  this.direction.z);
		//this.cam.yaw(angle*dt);
		this.normalizedDirection = new Vector3D(this.direction.x, this.direction.y, this.direction.z);
		this.normalizedDirection.normalize();
		
		
	}
	
	public void rotateUp(float angle, float dt) {
		
	}
	
	

	
}
