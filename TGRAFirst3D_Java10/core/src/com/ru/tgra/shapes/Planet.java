package com.ru.tgra.shapes;

import com.badlogic.gdx.graphics.Texture;

public class Planet {
	
	private Texture tex;
	private Point3D position;
	private float radius;
	private Orbit[] orbit;
	private int orbits;
	
	public Planet(Texture tex, Point3D position, float radius, int orbits) {
		this.tex = tex;
		this.position = position;
		this.radius = radius;
		this.orbit = orbit;
	}
	
	public float radius() {
		return this.radius;
	}
	
	public Point3D position() {
		return this.position;
	}
	
	public void draw() {
		
	}
	
}
