package com.ru.tgra.shapes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class World {
	
	private Point3D position; //Center of our world
	public ArrayList<Planet> planets;
	private Texture tex;
	public ArrayList<Opponent> opponents;
	public Tie player;
	
	
	private Shader shader;
	
	
	private int size;
	
	public Camera cam;
	
	public World(Point3D position, Shader shader, int size) {
		this.shader = shader;
		this.size = size;
		this.position = position;
		
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
		tex = new Texture(Gdx.files.internal("textures/Space.png"));
		
		//Initialize player
		player = new Tie(new Point3D(0,0,0), new Vector3D(0,0,-1), shader, this); //Initialize in center of world
		player.setCamera(cam);
		
		//Initialize opponents
		opponents = new ArrayList<Opponent>();
		initializeOpponents();
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
	}
	
	private void initializeOpponents() {
		//opponents.add(new Opponent(new Point3D(10,10,10), new Vector3D(0,0,-1), shader, player, this));
		//opponents.add(new Opponent(new Point3D(300,-25,10), new Vector3D(0,0,-1), shader, player, this));
		//opponents.add(new Opponent(new Point3D(10,15,10), new Vector3D(0,0,-1), shader, player, this));
		//opponents.add(new Opponent(new Point3D(10,10,300), new Vector3D(0,0,-1), shader, player, this));
	}
	
	
	
	private void simulateOpponents(float dt) {
		for (Opponent o : opponents) {
			o.simulate(-dt);
		}
	}
	private void initializePlanets() {
		//Texture tex, Point3D position, float radius, int orbits
		
		Texture p =  new Texture(Gdx.files.internal("textures/planet_Quom1200.png"));
		//planets.add(new Planet(p, new Point3D(400, 0, 400), 400, 0, shader));
		
		Texture p2 = new Texture(Gdx.files.internal("textures/deathstar.jpg"));
		planets.add(new Planet(p2, new Point3D(400, 0, 400), 400, 0, shader));
		
	}
	
	private void drawOpponents() {
		for(Opponent o : opponents) {
			o.draw();
		}
	}
	private void drawPlanets(float dt) {
		Point3D translate = null;
		for (Planet p : planets) {
			translate = p.getTranslation(player, dt);
			if (translate.x == 0 && translate.y == 0 && translate.z == 0) break;
		}
		if (translate.x != 0 || translate.y != 0 || translate.z != 0) {
			for(Planet p : planets) {
				//p.translate(translate, dt);
			}
		}
		for(Planet p : planets) {
			p.draw();
		}
	}

	private void drawSkyBox() {
		
		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		ModelMatrix.main.loadIdentityMatrix();
		
		ModelMatrix.main.pushMatrix();
		
		ModelMatrix.main.addTranslation(position.x,position.y, position.z);
		
		
		ModelMatrix.main.addScale(size, size, size);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		
		
		
		BoxGraphic.drawSolidCube(shader, tex);
		ModelMatrix.main.popMatrix();
	}
	
	public void update(float dt) {
		
		this.position = new Point3D(player.position.x, player.position.y, player.position.z);
		simulateOpponents(dt);
		player.simulateLasers(dt);
		player.rotateXYZ();
		player.neutralZ();
		player.neutralSpeed();
		
		player.updatePhysics(dt, true);
		
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.perspectiveProjection(90f, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 0.2f, size);
		shader.setViewMatrix(cam.getViewMatrix());
		shader.setProjectionMatrix(cam.getProjectionMatrix());
		shader.setEyePosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);


		//
		
		
		
		player.draw();
		//ModelMatrix.main.popMatrix();
		drawOpponents();
		drawPlanets(dt);
		
		//Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		drawSkyBox();
		//Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		//drawPyramids();
		
	
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
