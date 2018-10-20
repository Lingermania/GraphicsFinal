package com.ru.tgra.shapes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class World {
	
	private Point3D position; //Center of our world
	private ArrayList<Planet> planets;
	private Texture tex;
	private ArrayList<Opponent> opponents;
	public Tie player;
	private Shader shader;
	
	private int size;
	
	public Camera cam;
	
	public World(Point3D position, Shader shader, int size) {
		this.shader = shader;
		this.size = size;
		
		BoxGraphic.create();
		SphereGraphic.create();

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		cam = new Camera();
		cam.look(new Point3D(0, 0, 2), new Point3D(0,0,0), new Vector3D(0,1,0));
		//cam.perspectiveProjection(90.0f, 1f, 0.1f, 100.0f);

		//Initialize planets
		planets = new ArrayList<Planet>();
		initializePlanets();
		
		//Initialize texture
		
		//Initialize opponents
		opponents = new ArrayList<Opponent>();
		
		//Initialize player
		player = new Tie(new Point3D(0,0,0), new Vector3D(0,0,-1), shader); //Initialize in center of world
		player.setCamera(cam);
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
	}
	
	private void initializePlanets() {
		//Texture tex, Point3D position, float radius, int orbits
		
		planets.add(new Planet(null, new Point3D(0, 0, 10), 10, 0, shader));
		
	}
	
	private void drawPlanets() {
		for(Planet p : planets) {
			p.draw();
		}
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


		//drawPlanets();
		player.draw();
		//ModelMatrix.main.popMatrix();
		drawPyramids();
		
	
	}
	
	private void drawPyramids()
	{
		ModelMatrix.main.loadIdentityMatrix();
		int maxLevel = 9;

		for(int pyramidNr = 0; pyramidNr < 2; pyramidNr++)
		{
			ModelMatrix.main.pushMatrix();
			if(pyramidNr == 0)
			{
				shader.setMaterialDiffuse(0.8f, 0.8f, 0.2f, 1.0f);
				shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
				shader.setShininess(150.0f);
				shader.setMaterialEmission(0, 0, 0, 1);
				ModelMatrix.main.addTranslation(0.0f, 0.0f, -7.0f);
			}
			else
			{
				shader.setMaterialDiffuse(0.5f, 0.3f, 1.0f, 1.0f);
				shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
				shader.setShininess(150.0f);
				shader.setMaterialEmission(0, 0, 0, 1);
				ModelMatrix.main.addTranslation(0.0f, 0.0f, 7.0f);
			}
			ModelMatrix.main.pushMatrix();
			for(int level = 0; level < maxLevel; level++)
			{
	
				ModelMatrix.main.addTranslation(0.55f, 1.0f, -0.55f);
	
				ModelMatrix.main.pushMatrix();
				for(int i = 0; i < maxLevel-level; i++)
				{
					ModelMatrix.main.addTranslation(1.1f, 0, 0);
					ModelMatrix.main.pushMatrix();
					for(int j = 0; j < maxLevel-level; j++)
					{
						ModelMatrix.main.addTranslation(0, 0, -1.1f);
						ModelMatrix.main.pushMatrix();
						if(i % 2 == 0)
						{
							ModelMatrix.main.addScale(0.2f, 1, 1);
						}
						else
						{
							ModelMatrix.main.addScale(1, 1, 0.2f);
						}
						shader.setModelMatrix(ModelMatrix.main.getMatrix());
						
						SphereGraphic.drawSolidSphere(shader, null, null);
						//BoxGraphic.drawSolidCube(shader, null);
						//BoxGraphic.drawSolidCube(shader, tex);
						ModelMatrix.main.popMatrix();
					}
					ModelMatrix.main.popMatrix();
				}
				ModelMatrix.main.popMatrix();
			}
			ModelMatrix.main.popMatrix();
			ModelMatrix.main.popMatrix();
		}
	}
	
	
	
}
