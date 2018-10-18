package com.ru.tgra.shapes;

import java.util.ArrayList;

/*
 * Player is a particle with a directional vector
 * */
public class Player {
	
	
	Vector3D direction;
	Vector3D normalizedDirection;
	Point3D  position;
	Camera cam, orthoCam;
	float radius, cameraUpAngle;
	
	public Player(Vector3D direction, Point3D position, Camera cam, Camera orthoCam, float radius) {
		this.direction = direction;
		this.position  = position;
		this.cam = cam;
		this.orthoCam = orthoCam;
		this.normalizedDirection = new Vector3D(direction.x, direction.y, direction.z);
		this.normalizedDirection.normalize();
		this.radius = radius;
		this.cameraUpAngle = 1.5f;
		
		camLook(Point3D.add(new Point3D(this.position.x, cameraUpAngle, this.position.z), this.normalizedDirection), this.position, new Vector3D(0,1,0));
		//cam.look(Point3D.add(new Point3D(this.position.x, cameraUpAngle, this.position.z), this.normalizedDirection), this.position, new Vector3D(0,1,0));
		orthoCam.look(new Point3D(5,25,5), this.position, new Vector3D(0,1,0));
	}
	
	public boolean canMove(float x, float z) {
		

		return true;
	}
	
	
	public void updateCam() {
		camLook(Point3D.add(new Point3D(this.position.x, cameraUpAngle, this.position.z), this.normalizedDirection), this.position, new Vector3D(0,1,0));
	}
	
	public void move(float dt) {
		//Check for collision
		//canMove(position.x - direction.x*dt, position.z - direction.z*dt)
		position.x -= direction.x*dt;
		//position.z -= direction.z*dt;
		cam.walkForwardX(dt);
		orthoCam.walkForwardX(dt);
		
		position.z -= direction.z*dt;
		cam.walkForwardZ(dt);
		orthoCam.walkForwardZ(dt);
		camLook(Point3D.add(new Point3D(this.position.x, cameraUpAngle, this.position.z), this.normalizedDirection), this.position, new Vector3D(0,1,0));
		//cam.look(Point3D.add(new Point3D(this.position.x, cameraUpAngle, this.position.z), this.normalizedDirection), this.position, new Vector3D(0,1,0));
	}
	
	public void slide(float dt) {
		//Check for collision
		
		
		
	}
	
	public void rotateY(float angle, float dt) {
		
		float radians = (angle * (float)Math.PI / 180.0f) * dt;
		float c       = (float)Math.cos(radians);
		float s       = -(float)Math.sin(radians);
		
		this.direction = new Vector3D(c*direction.x - s*direction.z,
				 					  direction.y,
				 					  s*direction.x + c*direction.z);
		this.cam.yaw(angle*dt);
		this.normalizedDirection = new Vector3D(this.direction.x, this.direction.y, this.direction.z);
		this.normalizedDirection.normalize();
		
		camLook(Point3D.add(new Point3D(this.position.x, cameraUpAngle, this.position.z), this.normalizedDirection), this.position, new Vector3D(0,1,0));
		//cam.look(Point3D.add(new Point3D(this.position.x, cameraUpAngle, this.position.z), this.normalizedDirection), this.position, new Vector3D(0,1,0));
	}
	
	private void camLook(Point3D eye, Point3D center, Vector3D up) {
		//Makes the camera smoothly go slide up walls instead of going into them
		
	}
	
	public void rotateUp(float angle, float dt) {
		if (this.cameraUpAngle + angle*dt >= 0.2 && this.cameraUpAngle + angle*dt <= 4.0f) {
			this.cameraUpAngle += angle*dt;
		}
		camLook(Point3D.add(new Point3D(this.position.x, cameraUpAngle, this.position.z), this.normalizedDirection), this.position, new Vector3D(0,1,0));
		
	}
	
	public void reset(Point3D position)
	{
		
	
	}
	
}
