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
	
	public void draw() {
		
		
		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		ModelMatrix.main.loadIdentityMatrix();
		
		ModelMatrix.main.pushMatrix();
		
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		ModelMatrix.main.addScale(radius, radius*2, radius);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		
		
		
		SphereGraphic.drawSolidSphere(shader, null, null);
		ModelMatrix.main.popMatrix();
	}
	
}
