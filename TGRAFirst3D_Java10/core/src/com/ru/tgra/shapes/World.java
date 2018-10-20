package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class World {
	
	private Point3D position; //Center of our world
	private Planet[] planets;
	private Texture tex;
	private Opponent[] opponents;
	private Tie player;
	private Shader shader;
	
	private int size;
	
	public Camera cam;
	
	public World(Point3D position, Shader shader, int size) {
		this.shader = shader;
		this.size = size;
		
		BoxGraphic.create();
		//SphereGraphic.create();

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		cam = new Camera();
		cam.look(new Point3D(0, 0, 2), new Point3D(0,0,0), new Vector3D(0,1,0));
		//cam.perspectiveProjection(90.0f, 1f, 0.1f, 100.0f);

		//Initialize planets
		planets = new Planet[10];
		
		//Initialize texture
		
		//Initialize opponents
		opponents = new Opponent[10];
		
		//Initialize player
		player = new Tie(new Point3D(0,0,0), new Vector3D(0,0,-1), shader); //Initialize in center of world
		player.setCamera(cam);
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
	}
	
	
	private void setCamera() {
		System.out.println(cam.eye.x + "," + cam.eye.y + ", " + cam.eye.z);
		//cam.look(new Point3D(0,2,0), player.position, new Vector3D(0,1,0));
		cam.look(Point3D.add(new Point3D(player.position.x, player.position.y + 0.4f, player.position.z), player.direction), player.position, new Vector3D(0,1,0));
	}
	
	public void update(float dt) {
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.perspectiveProjection(90f, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 0.2f, 100.0f);
		shader.setViewMatrix(cam.getViewMatrix());
		shader.setProjectionMatrix(cam.getProjectionMatrix());
		shader.setEyePosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);


		player.draw();
		
		setCamera();
		
		//ModelMatrix.main.popMatrix();

		//drawPyramids();
	
	}
	
	
	
}
