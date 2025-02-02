package com.ru.tgra.shapes;

import java.util.ArrayList;

/*
 * Player is a particle with a directional vector
 * */
public class Player {
	
	
	protected Vector3D direction, originalDirection;
	protected Vector3D normalizedDirection;
	protected Point3D  position;
	public Camera cam;
	
	public float cameraUpAngle;
	protected float angleY, angleX, angleZ;
	public float radius;
	
	protected PlayerPhysics phys;
	protected ArrayList<Laser> lasers;
	protected World world;
	
	protected boolean rotateSeq;
	protected float   rotateYSeq;
	public Explosion explosion;
	
	
	public boolean alive;
	public boolean exploding;
	
	public float brightness;
	public boolean up;

	
	
	public Player(Point3D position, Vector3D direction, World world) {
		this.direction = direction;
		this.originalDirection = direction;
		this.position  = position;
		this.angleY    = 0.0f;
		this.angleZ    = 0.0f;
		this.angleX    = 0.0f;
		this.world     = world;
		this.radius    = 1;
		this.rotateSeq = false;
		this.rotateYSeq = 0;
		
		this.cameraUpAngle = 1.5f;

		
		phys = new PlayerPhysics();
		lasers = new ArrayList<Laser>();
		
		alive=true;
		exploding=false;
		
		brightness=1;
		up=true;
	}
	
	
	private boolean sphereCollision(Point3D position, float radius, Point3D playerPosition) {
		float length = Vector3D.difference(position, playerPosition).length();
		
		return length <= radius + this.radius -1;
	}
	
	private Planet planetCollision(Point3D position) {
		//Planets are static
		
		boolean val = false;
		
		for(Planet p : world.planets) {
			//Point3D planetCenter = new Point3D(p.position().x, p.position().y + p.radius(), p.position().z);
			val |= sphereCollision(p.position(), p.radius(), position);
			if (val == true) {
				return p;
			}
		}
		
		return null;
	}
	
	
	public boolean playerCollision(Player other) {
		float len = Vector3D.difference(other.position, position).length();
		
		if (len <= other.radius + this.radius)
		{
			return true;
		}

		return false;
	}
	
	private boolean collision() {
		boolean val = false;
		
		//val |= planetCollision();
		//val |= playerCollision();
		
		
		return false;
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
		
		if(world.outro_win_dt > 0f || world.outro_loose_dt > 0f) return;
		cam.look(Point3D.add(new Point3D(position.x,position.y + cameraUpAngle, position.z),Vector3D.scale(direction, 3f)), position, new Vector3D(0,1,0));
	}
	
	
	public void updatePhysics(float dt, boolean movePhysics) {
		this.angleZ = phys.avgZ()*90;
		this.angleY -= phys.avgZ()*45*dt;
		
		float physSpeed = phys.avgSpeed()*2f;
		//System.out.println(physSpeed);
		
		
		if(movePhysics) {
			position.x += direction.x*physSpeed;
			position.y += direction.y*physSpeed;
			position.z += direction.z*physSpeed;
		}
		
	}
	
	public void simulateLasers(float dt) {
		
		for (Laser l : lasers) {
			l.simulate(dt, world);
		}
	}
	
	public void move(float dt) {
		//Check for collision

		float physSpeed = phys.avgSpeed()*0.6f;
		
		
		position.x += direction.x*dt;//*physSpeed;
		position.y += direction.y*dt;//*physSpeed;
		position.z += direction.z*dt;//*physSpeed;
		
		updatePhysics(dt, true);
		
		
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
		updatePhysics(dt, false);

	}
	
	public void explode() {
		exploding = true;
		
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
	
	public void brightness(boolean up, float dt)
	{
		if (up==this.up)
		{
			if (this.brightness - (1/3)*dt>1/3)
			{
				this.brightness=this.brightness - (1/3)*dt;
			}
		}
		else
		{
			if (this.brightness + (1/3)*dt>1)
			{
				this.up= !this.up;
				this.brightness=1;
			}
			else
			{
				this.brightness=this.brightness + (1/3)*dt;
			}
		}
	}
	
	public void rotateUp(float angle, float dt) {
		//cameraUpAngle += angle*dt;
	}
	
	

	
}
