package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import java.nio.FloatBuffer;
import java.util.Random;

import com.badlogic.gdx.utils.BufferUtils;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
/*
 * TODO
 * -Define and implement objective of game
 * -Slide on walls
 * -world.camera can look into walls
 * -Make a clear light model
 * -Add moving obstacles
 * -fix jumping into walls
 */
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class First3DGame extends ApplicationAdapter implements InputProcessor {
	
	World world;
	Shader shader;

	private float angle;

	private Camera cam;
	private Camera topCam;
	
	private float fov = 90.0f;

	MeshModel model;

	private Texture tex;
	
	Random rand = new Random();

	@Override
	public void create () {

		

		shader = new Shader();

		world = new World(new Point3D(0,0,0), shader, 1000);
	}

	private void input()
	{
	}
	
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		angle += 180.0f * deltaTime;

		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			world.cam.slide(-3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			world.cam.slide(3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			world.cam.slide(0, 0, -3.0f * deltaTime);
			//cam.walkForward(3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			world.cam.slide(0, 0, 3.0f * deltaTime);
			//cam.walkForward(-3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			world.cam.slide(0, 3.0f * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			world.cam.slide(0, -3.0f * deltaTime, 0);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			world.cam.yaw(-90.0f * deltaTime);
			//cam.rotateY(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			world.cam.yaw(90.0f * deltaTime);
			//cam.rotateY(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			world.cam.pitch(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			world.cam.pitch(90.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			world.cam.roll(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			world.cam.roll(90.0f * deltaTime);
		}


		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
		{
			//Gdx.graphics.setDisplayMode(500, 500, false);
			Gdx.app.exit();
		}

		//do all updates to the game
	}
	
	private void display()
	{
		float dt = Gdx.graphics.getDeltaTime();
		world.update(dt);
	}

	@Override
	public void render () {
		
		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}

	private void drawPyramids()
	{
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

						BoxGraphic.drawSolidCube(shader, null);
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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}


}