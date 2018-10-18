package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

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

		//Initialize planets
		planets = new Planet[10];
		
		//Initialize texture
		
		//Initialize opponents
		opponents = new Opponent[10];
		
		//Initialize player
		player = new Tie(new Point3D(0,0,0), new Vector3D(1,0,0)); //Initialize in center of world
		player.setCamera(cam);
		
	}
	

	public void update(float dt) {
		
		Gdx.gl.glUniformMatrix4fv(shader.viewMatrixLoc, 1, false, cam.getViewMatrix());
	}
	
}
