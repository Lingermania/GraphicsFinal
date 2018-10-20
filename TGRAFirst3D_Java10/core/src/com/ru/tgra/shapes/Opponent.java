package com.ru.tgra.shapes;

import com.badlogic.gdx.graphics.Texture;

public class Opponent extends Player{

	private Shader shader;
	
	public Opponent(Point3D position, Vector3D direction, Shader shader) {
		
		super(position, direction);
		this.shader = shader;
	}
	
	
	public void draw() {
		
	}
}
