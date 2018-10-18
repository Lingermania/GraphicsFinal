package com.ru.tgra.shapes;

public class Orbit extends Planet {
	
	private float speed;
	private Planet planet; //Planet being orbited
	
	public Orbit(Texture tex, Point3D position, float radius, float speed, Planet planet) {
		//Takes in texture, position, radius, speed and the planet it is orbiting
		
		super(tex, position, radius, 0);
		
		this.speed = speed;
		this.planet = planet;
	}
	
	
	public void draw() {
		//Draw orbit
	}
}
