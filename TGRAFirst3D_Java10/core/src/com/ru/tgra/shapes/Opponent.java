package com.ru.tgra.shapes;

import com.badlogic.gdx.graphics.Texture;

public class Opponent extends Player{

	private Texture tex;
	
	public Opponent(Point3D position, Vector3D direction, Texture tex) {
		
		super(position, direction);
		this.tex = tex;
	}
	
	
	public void draw() {
		
	}
}
