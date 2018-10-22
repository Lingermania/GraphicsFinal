package com.ru.tgra.shapes;

import java.util.ArrayList;

/*
 * Player is a particle with a directional vector
 * */
public class Player {
	
	
	protected Vector3D direction, originalDirection;
	protected Vector3D normalizedDirection;
	protected Point3D  position;
	protected Camera cam;
	protected float cameraUpAngle;
	protected float angleY, angleX, angleZ;
	protected PlayerPhysics phys;
	protected ArrayList<Laser> lasers;
	
	public Player(Point3D position, Vector3D direction) {
		this.direction = direction;
		this.originalDirection = direction;
		this.position  = position;
		this.angleY    = 0.0f;
		this.angleZ    = 0.0f;
		this.angleX    = 0.0f;
		
		this.cameraUpAngle = 0.4f;
		
		phys = new PlayerPhysics();
		lasers = new ArrayList<Laser>();
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
	
	
	protected void updateCamera() {
		//System.out.println(cam.eye.x + "," + cam.eye.y + ", " + cam.eye.z);
		//cam.look(new Point3D(0,2,0), player.position, new Vector3D(0,1,0));
		
		cam.look(Point3D.add(new Point3D(position.x,position.y + cameraUpAngle, position.z),Vector3D.scale(direction, 2f)), position, new Vector3D(0,1,0));
	}
	
	
	public void updatePhysics(float dt) {
		this.angleZ = phys.avgZ()*90;
		this.angleY -= phys.avgZ()*45*dt;
		
		float physSpeed = phys.avgSpeed()*0.6f;
		//System.out.println(physSpeed);
		
		position.x += direction.x*physSpeed;
		position.y += direction.y*physSpeed;
		position.z += direction.z*physSpeed;
	}
	
	public void simulateLasers(float dt) {
		for (Laser l : lasers) {
			l.simulate(dt);
		}
	}
	
	public void move(float dt) {
		//Check for collision
	
		position.x += direction.x*dt;
		position.y += direction.y*dt;
		position.z += direction.z*dt;
		
		updatePhysics(dt);
		
	}
	
	public void neutralSpeed() {
		phys.neutralSpeed();
	}
	
	public void neutralZ() {
		phys.neutralZ();
	}
	
	public void forward() {
		phys.forward();
	}
	
	public void backward() {
		phys.backwards();
	}
	
	public void left() {
		phys.left();
	}
	public void right() {
		phys.right();
	}
	
	public void rotateY(float angle, float dt) {
		
		this.angleY += angle*dt;
		
		//Some minor physics
		this.angleZ = phys.avgZ()*20;
		updatePhysics(dt);

	}
	

	public void rotateXYZ() {
		//Set direction to rotation about angleX, angleY and angleZ
		float radians = (angleZ * (float)Math.PI / 180.0f);
		float c       = (float)Math.cos(radians);
		float s       = -(float)Math.sin(radians);
		
		Vector3D dir = new Vector3D(c*originalDirection.x + s*originalDirection.y,
								  -s*originalDirection.x + c*originalDirection.y,
								  originalDirection.z);
		
		
		
		
		radians = (angleX * (float)Math.PI / 180.0f);
		c       = (float)Math.cos(radians);
		s       = -(float)Math.sin(radians);
		dir = new Vector3D(dir.x,
									  c*dir.y + s*dir.z,
				 					  -s*dir.y + c*dir.z);
		
		radians = (angleY * (float)Math.PI / 180.0f);
		c       = (float)Math.cos(radians);
		s       = -(float)Math.sin(radians);
		dir = new Vector3D(c*dir.x - s*dir.z,
						  dir.y,
						  s*dir.x + c*dir.z);
		
		this.direction = dir;
		
		//System.out.println(this.direction.x + ", " + this.direction.y + ", " +  this.direction.z);
		//this.cam.yaw(angle*dt);
		
		this.normalizedDirection = new Vector3D(this.direction.x, this.direction.y, this.direction.z);
		this.normalizedDirection.normalize();
		
		
	}
	public void rotateX(float angle, float dt) {
		

		//System.out.println(this.angleX);
		if (this.angleX + angle*dt < 89 && this.angleX + angle*dt > -89) {
			this.angleX += angle*dt;
		}
		
		
	}
	
	public void rotateUp(float angle, float dt) {
		//cameraUpAngle += angle*dt;
	}
	
	

	
}
