package com.ru.tgra.shapes;

public class Opponent extends Player{

	private Texture tex;
	
	public Opponent(Point3D position, Vector3D direction, Texture tex) {
		
		super(position, direction);
		this.tex = tex;
	}
	
	public Point3D position() {
		return super.position;
	}
	
	public Vector3D direction() {
		return super.direction;
	}
	
	public void draw() {
		
	}
}
