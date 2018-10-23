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
	
	public Planet(Texture tex, Point3D position, float radius, int orbits, Shader shader) {
		this.tex = tex;
		this.position = position;
		this.radius = radius;
		this.orbits = orbits;
		this.shader = shader;
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
	
	public void draw() {
		
		
		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		ModelMatrix.main.loadIdentityMatrix();
		
		ModelMatrix.main.pushMatrix();
		
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		ModelMatrix.main.addScale(radius, radius, radius);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		
		
		
		SphereGraphic.drawSolidSphere(shader, tex, null);
		ModelMatrix.main.popMatrix();
	}
	
}
