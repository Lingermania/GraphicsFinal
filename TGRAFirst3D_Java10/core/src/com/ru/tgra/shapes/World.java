package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class World {
	
	private Point3D position; //Center of our world
	private Planet[] planets;
	private Texture tex;
	private Opponent[] opponents;
	private Tie player;
	private Shader shader;
	
	private int size;
	
	private Camera cam;
	
	public World(Point3D position, Shader shader, int size) {
		this.shader = shader;
		this.size = size;
		
		
		//Initialize camera
		cam = new Camera(shader.viewMatrixLoc, shader.projectionMatrixLoc);
		cam.perspectiveProjection(90.0f, 1f, 0.1f, 100.0f);
		
		
		
	}
	

	public void update(float dt) {
		
		Gdx.gl.glUniformMatrix4fv(shader.viewMatrixLoc, 1, false, cam.getViewMatrix());
	}
	
}
