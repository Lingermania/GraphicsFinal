package com.ru.tgra.shapes;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class World {
	
	private Point3D position; //Center of our world
	public ArrayList<Planet> planets;
	private Texture tex;
	public ArrayList<Opponent> opponents;
	public Tie player;
	private Sound sound, beamSound;
	public boolean intro = true, outro_win = false, outro_loose;
	public float intro_dt = 0.0f, outro_win_dt = 0.0f, outro_loose_dt;
	private Point3D outroCameraPoint;
	private Timer timer;
	
	//Explosion explosion;
	
	private Shader shader;
	
	
	private int size;
	
	public Camera cam;
	public Camera orthoCam;
	
	public World(Point3D position, Shader shader, int size) {
		this.shader = shader;
		this.size = size;
		this.position = position;
		
		BoxGraphic.create();
		SphereGraphic.create();
		SpriteGraphic.create();
		
		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		cam = new Camera();
		//cam.look(new Point3D(0, 0, 2), new Point3D(0,0,0), new Vector3D(0,1,0));
		
		orthoCam = new Camera();
		orthoCam.orthographicProjection(size/2, size/2, size/2, size/2, 0.2f, size/2);
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
		
		//this.explosion = new Explosion(player.position, shader);
		
		sound = Gdx.audio.newSound(Gdx.files.internal("sounds/theme.mp3"));
		sound.loop(1f);
		
		beamSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Laserfx.mp3"));
		beamSound.loop(0.3f);
		timer = new Timer();
		
		
	}
	
	public void endSound()
	{
		sound.dispose();
		beamSound.dispose();
	}
	
	private void initializeOpponents() {
		opponents.add(new Opponent(new Point3D(200,10,200), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(250,10,200), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(-200,25,200), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(-250,25,200), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(100,0,200), new Vector3D(0,0,-1), shader, player, this));
		//opponents.add(new Opponent(new Point3D(800,-10,80), new Vector3D(0,0,-1), shader, player, this));
		//opponents.add(new Opponent(new Point3D(80,10,800), new Vector3D(0,0,-1), shader, player, this));
		//opponents.add(new Opponent(new Point3D(1000,30,1000), new Vector3D(0,0,-1), shader, player, this));
		//opponents.add(new Opponent(new Point3D(10,22,1000), new Vector3D(0,0,-1), shader, player, this));
		/*opponents.add(new Opponent(new Point3D(300,-25,10), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(10,15,10), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(10,80,300), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(35,100,300), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(200,2,500), new Vector3D(0,0,-1), shader, player, this));
		
		opponents.add(new Opponent(new Point3D(1212,231,1231), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(300,-25,12), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(-1231,-15,-1111), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(10,-80,300), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(35,-123,-3200), new Vector3D(0,0,-1), shader, player, this));
		opponents.add(new Opponent(new Point3D(-311,2,5000), new Vector3D(0,0,-1), shader, player, this));*/
	}
	
	
	
	private void simulateOpponents(float dt) {
		for (Opponent o : opponents) {
			o.simulate(-dt);
		}
	}
	private void initializePlanets() {
		//Texture tex, Point3D position, float radius, int orbits
		
		Texture p =  new Texture(Gdx.files.internal("textures/planet_Quom1200.png"));
		planets.add(new Planet(p, new Point3D(90000, 90000, 50000), 40000, 0, shader, 3000));
		
		Texture p2 = new Texture(Gdx.files.internal("textures/deathstar.jpg"));
		planets.add(new Planet(p2, new Point3D(10000, 100, 10000), 4000, 0, shader, 500));
		
		//set laser beam on deathstar
		planets.get(1).drawLaser = true;
		planets.get(1).targetLaser = planets.get(0).position();
		
		
	}
	
	private void drawOpponents() {
		boolean alive = false;
		for(Opponent o : opponents) {
			if (o.alive) {
				o.draw();
				alive = true;
			}
			if(o.exploding) {
				o.explosion.simulate();
				o.explosion.draw();
			}
		}
		
	    
		if(!alive && !outro_win && outro_win_dt < 1f && !outro_loose && outro_loose_dt == 0) {
			outro_win = true;
			outroCameraPoint = player.cam.eye;
		}
		if(outro_win_dt > 1f) {
			planets.get(1).alive = false;
			planets.get(1).exploding = true;
			planets.get(0).drawLaser = false;
			beamSound.dispose();
		}
	}
	
	private void drawPlayer() {
		if (player.alive) {
			player.draw();
		}
		if(player.exploding) {
			player.explosion.simulate();
			player.explosion.draw();
		}
			
		
	}
	private void drawPlanets(float dt) {

		for(Planet p : planets) {
			if(p.drawLaser && p.alive) {
				
				p.drawLaser(planets.get(0).position(), planets.get(0).radius(), dt);
			}
			if (p.alive) {
				p.draw(player, dt);
			}
			if(p.exploding) {
				p.explosion.simulate(0.99f);
				p.explosion.draw();
			}
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
	
	public void test_exp() {
		//explosion.simulate();
		//explosion.draw();
	}
	
	
	public void camera_intro(float dt) {
		Point3D finalDest = Point3D.add(new Point3D(position.x,position.y + player.cameraUpAngle, position.z),Vector3D.scale(player.direction, 3f));
		ArrayList<Point3D> points = new ArrayList<Point3D>();
		points.add(new Point3D(-1000, -1000, -1000));
		points.add(new Point3D(-900, -700, -900));
		points.add(new Point3D(-800, -600, -800));
		points.add(new Point3D(-700, -500, -700));
		points.add(new Point3D(-600, -400, -600));
		points.add(new Point3D(-500, -300, -500));
		points.add(new Point3D(-400, -200, -400));
		points.add(new Point3D(-300, -100, -300));
		points.add(new Point3D(-200, -50, -200));
		points.add(new Point3D(-100, 4, -100));
		points.add(new Point3D(player.position.x, 4, player.position.z));
		points.add(finalDest);
		//points.add(new Point3D(player.position.x,player.position.y,player.position.z));
		
		Point3D position = player.bezier(intro_dt, points);
		
		player.cam.look(position, player.position, new Vector3D(0,1,0));
		intro_dt += dt/5.0f;
		
		if(intro_dt > 1) {
			intro = false;
		}
	}
	
	public void camera_outro_win(float dt) {
		if(outro_win_dt > 1) return;
		Point3D startingDest = outroCameraPoint;
		Point3D finalDest = new Point3D(-100, startingDest.y, -100);
		
		ArrayList<Point3D> points = new ArrayList<Point3D>();
		points.add(startingDest);
		
		float xdist = Math.abs(finalDest.x - startingDest.x);
		float zdist = Math.abs(finalDest.z - startingDest.z);
		float iter = 10;
		for(int i = 0; i < iter; i++) {
			float xit = 0;
			float zit = 0;
			
			if(startingDest.x < finalDest.x) {
				xit = xdist/iter;
			}
			else {
				xit = -xdist/iter;
			}
			if(startingDest.z < finalDest.z) {
				zit = zdist/iter;
			}
			else {
				zit = -zdist/iter;
			}
			
			points.add(new Point3D(startingDest.x + xit,startingDest.y, startingDest.z + zit ));
		}
	
		points.add(finalDest);

		//point on curve
		Point3D position = player.bezier(outro_win_dt, points);
		
		//death star point
		Point3D dsPoint = planets.get(1).position();
		dsPoint = new Point3D(dsPoint.x/100.f, dsPoint.y /100.0f, dsPoint.z /100.0f);
		
		Point3D lookat = new Point3D(player.position.x * (1- outro_win_dt) + dsPoint.x * outro_win_dt,
									 player.position.y * (1- outro_win_dt) + dsPoint.y * outro_win_dt,
									 player.position.z * (1- outro_win_dt) + dsPoint.z * outro_win_dt);
		
		player.cam.look(position, lookat, new Vector3D(0,1,0));
		//System.out.println(lookat.x + ", " + lookat.y + ", " + lookat.z);
		//System.out.println(player.position.x + ", " + player.position.y + ", " + player.position.z);
		outro_win_dt += dt/10.0f;
		
		if(outro_win_dt > 1) {
			//outro_win = false;
			
		}
	}
	
	public void camera_outro_loose(float dt) {
		if(outro_loose_dt > 1) return;
		Point3D startingDest = outroCameraPoint;
		Point3D finalDest = new Point3D(-100, startingDest.y, -100);
		
		ArrayList<Point3D> points = new ArrayList<Point3D>();
		points.add(startingDest);
		
		float xdist = Math.abs(finalDest.x - startingDest.x);
		float zdist = Math.abs(finalDest.z - startingDest.z);
		float iter = 10;
		for(int i = 0; i < iter; i++) {
			float xit = 0;
			float zit = 0;
			
			if(startingDest.x < finalDest.x) {
				xit = xdist/iter;
			}
			else {
				xit = -xdist/iter;
			}
			if(startingDest.z < finalDest.z) {
				zit = zdist/iter;
			}
			else {
				zit = -zdist/iter;
			}
			
			points.add(new Point3D(startingDest.x + xit,startingDest.y, startingDest.z + zit ));
		}
	
		points.add(finalDest);

		//point on curve
		Point3D position = player.bezier(outro_loose_dt, points);
		
		//death star point
		Point3D dsPoint = planets.get(0).position();
		dsPoint = new Point3D(dsPoint.x/100.f, dsPoint.y /100.0f, dsPoint.z /100.0f);
		
		Point3D lookat = new Point3D(player.position.x * (1- outro_loose_dt) + dsPoint.x * outro_loose_dt,
									 player.position.y * (1- outro_loose_dt) + dsPoint.y * outro_loose_dt,
									 player.position.z * (1- outro_loose_dt) + dsPoint.z * outro_loose_dt);
		
		player.cam.look(position, lookat, new Vector3D(0,1,0));
		//System.out.println(lookat.x + ", " + lookat.y + ", " + lookat.z);
		//System.out.println(player.position.x + ", " + player.position.y + ", " + player.position.z);
		outro_loose_dt += dt/10.0f;
		
		if(outro_loose_dt > 1) {
			//outro_loose = false;
		}
	}
	
	public void update(float dt) {
		
		
		this.position = new Point3D(player.position.x, player.position.y, player.position.z);
		
		if(!intro) {
			simulateOpponents(dt);
			player.simulateLasers(dt);
			player.rotateXYZ();
			player.neutralZ();
			player.neutralSpeed();
			
			player.updatePhysics(dt, true);
			if(!player.alive && outro_loose_dt < 1.0f) {
				outroCameraPoint = new Point3D(player.cam.eye.x, 
											   player.cam.eye.y,
											   player.cam.eye.z);
				timer.schedule(new TimerTask()
				{
					@Override
					public void run() {
						outro_loose = true;
					}
				}
				, 3500);
				
			}
			if(!player.alive && outro_loose_dt > 1.0f) {
				planets.get(0).alive = false;
				planets.get(0).exploding = true;
				planets.get(1).drawLaser = false;
				beamSound.dispose();
			}
			
		}
		else {
			camera_intro(dt);
		}
		
		if(outro_win && !outro_loose) {
			camera_outro_win(dt);
		}
		if(outro_loose) {
			camera_outro_loose(dt);
		}
		
		
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		
		shader.setViewMatrix(cam.getViewMatrix());
		shader.setEyePosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);

		

		
		lightsOff();
		//set projection for background
		shader.setBackground(true);
		cam.perspectiveProjection(90f, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 2000f, size);
		shader.setProjectionMatrix(cam.getProjectionMatrix());
		shader.setGlobalAmbient(1f, 1f,1f, 1f);
		//draw background
		drawPlanets(dt);
		drawSkyBox();
		
		
		
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		
		//set projection for non-background
		shader.setBackground(false);
		shader.setGlobalAmbient(0f, 0f,0f, 1);
		setLights();
		cam.perspectiveProjection(90f, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 0.2f, size/2);
		shader.setProjectionMatrix(cam.getProjectionMatrix());
		
		tieCollisions();
		
		drawPlayer();
		//ModelMatrix.main.popMatrix();
		drawOpponents();

		
		//Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

		//Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		//drawPyramids();
		
		//explosion.draw();
		
		//Draw map
	}
	
	private void lightsOff()
	{
		//Set up lights
		shader.setLightColor(Lights.LEFT_LIGHT, 0f,0f, 0f,player.brightness);

		shader.setLightColor(Lights.RIGHT_LIGHT, 0f,0f, 0f,player.brightness);
		
		shader.setLightColor(Lights.TIE_LIGHT, 0f,0f, 0f,player.brightness);

		
	}
	private void setLights()
	{
		//Set up lights
		shader.setLightColor(Lights.LEFT_LIGHT, 1f,1f, 1f,player.brightness);
		shader.setLightPosition(Lights.LEFT_LIGHT, this.size, 0, this.size, 1f);
		shader.setLightDirectional(Lights.LEFT_LIGHT, 0);
		
		shader.setLightColor(Lights.RIGHT_LIGHT, 1f,1f, 1f,player.brightness);
		shader.setLightPosition(Lights.RIGHT_LIGHT, this.size, 0, this.size, 1f);
		shader.setLightDirectional(Lights.RIGHT_LIGHT, 0);
	
		shader.setLightColor(Lights.TIE_LIGHT, 1f,1f, 1f,player.brightness);
		shader.setLightPosition(Lights.TIE_LIGHT, cam.eye.x, cam.eye.y, cam.eye.z, 1f);
		shader.setLightDirectional(Lights.TIE_LIGHT, 0);
		
		/*shader.setLightColor(Lights.BEHIND_LIGHT, 1f,1f, 1f,player.brightness);
		shader.setLightPosition(Lights.BEHIND_LIGHT, 0, 0, 0, 1f);
		shader.setLightDirectional(Lights.BEHIND_LIGHT, 0);
		
		/*shader.setLightColor(Lights.RIGHT_LOW_LIGHT, 1f,1f, 1f,1f);
		shader.setLightPosition(Lights.RIGHT_LOW_LIGHT, 0, -this.size, this.size, 1f);
		shader.setLightDirectional(Lights.RIGHT_LOW_LIGHT, 0);
		
		*/
	}
	
	private void tieCollisions()
	{
		for(Opponent o : opponents) {
			boolean collide;
			if (o.alive) {
				collide= player.playerCollision(o);
				if (collide)
				{
					System.out.println("collide");
					player.alive=false;
					player.setExplosion();
					player.exploding=true;
					o.alive=false;
					o.setExplosion();
				}
			}
			
		}
	}
	
	public void setPlayerLightIntensity(boolean up,float dt) {
		player.brightness(up, dt);
		shader.setLightColor(Lights.TIE_LIGHT, 1, 1, 1, player.brightness);
		shader.setLightColor(Lights.LEFT_LIGHT, 1, 1, 1, player.brightness);
		shader.setLightColor(Lights.RIGHT_LIGHT, 1, 1, 1, player.brightness);
		
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
