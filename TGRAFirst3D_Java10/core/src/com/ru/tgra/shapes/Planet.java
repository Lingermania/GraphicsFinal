package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Planet {
	
	private Texture tex;
	private Point3D position;
	private float radius;
	private Orbit[] orbit;
	private int orbits;
	private Shader shader;
	public Explosion explosion;
	public boolean drawLaser = false;
	public Point3D targetLaser;
	public boolean alive = true;
	public boolean exploding = false;
	public LaserBeam beam;
	
	
	public Planet(Texture tex, Point3D position, float radius, int orbits, Shader shader, int explosionSize) {
		this.tex = tex;
		this.position = position;
		this.radius = radius;
		this.orbits = orbits;
		this.shader = shader;
		explosion = new Explosion(position, shader, explosionSize, explosionSize*2);
	}
	
	public float radius() {
		return this.radius;
	}
	
	public Point3D position() {
		return this.position;
	}
	
	
	public void translate(Point3D p, float dt) {
		System.out.println("Translating Planet!!");
		this.position.x += p.x*dt;
		this.position.y += p.y*dt;
		this.position.z += p.z*dt;
	}
	
	
	public Point3D getTranslation(Player player, float dt) {
		//If planet is close to player return the translation
		//s.t. all planets will be translated by that point
		
		float min = 50f;
		Point3D res = new Point3D(0,0,0);
		
		if(Vector3D.difference(player.position, this.position).length() <= this.radius + min) {
			//translate in player direction
			float physSpeed = player.phys.avgSpeed()*10f;
			res = new Point3D(-player.direction.x*(1 + physSpeed*(1.0f/dt)), -player.direction.y*(1 + physSpeed*(1.0f/dt)), -player.direction.z*(1 + physSpeed*(1.0f/dt)));
		}
		
		return res;
		
	}
	
	public void drawLaser(Point3D target, float targetRadius, float dt) {
		if(drawLaser) {
			if(beam == null) {
				beam = new LaserBeam(this.position, target, shader, 100000, 500, targetRadius);
			}
	
			beam.simulate();
			beam.draw();
		}
	}
	
	public void draw(Player p, float dt) {
		
		
		if (this.alive) {
			shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setShininess(500);
			ModelMatrix.main.loadIdentityMatrix();
			
			ModelMatrix.main.pushMatrix();
			
			float len = Vector3D.difference(p.position, position).length();
			//System.out.println(len - radius);
			if (len - radius*2 < 30) {
				position.x += p.position.x*dt;
				position.y += p.position.y*dt;
				position.z += p.position.z*dt;
				//ModelMatrix.main.addTranslation(position.x + p.position.x, position.y + p.position.y, position.z + p.position.z);
			}
			else {
				
			}
			
			ModelMatrix.main.addTranslation(position.x, position.y , position.z);
			
			ModelMatrix.main.addScale(radius, radius*2, radius);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			
			
			
			SphereGraphic.drawSolidSphere(shader, tex, null);
			ModelMatrix.main.popMatrix();
		}
		
		
	}
	
}
