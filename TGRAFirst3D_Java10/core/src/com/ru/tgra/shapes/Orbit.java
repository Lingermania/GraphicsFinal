package com.ru.tgra.shapes;

import com.badlogic.gdx.graphics.Texture;

public class Orbit extends Planet {
	
	private float speed;
	private Planet planet; //Planet being orbited
	
	public Orbit(Texture tex, Point3D position, float radius, float speed, Planet planet, Shader shader) {
		//Takes in texture, position, radius, speed and the planet it is orbiting
		
		super(tex, position, radius, 0, shader);
		
		this.speed = speed;
		this.planet = planet;
	}
	
	
	public void draw() {
		//Draw orbit
	}
}
