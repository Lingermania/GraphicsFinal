package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;
import java.util.Random;

import com.badlogic.gdx.utils.BufferUtils;
/*
 * TODO
 * -Define and implement objective of game
 * -Slide on walls
 * -Camera can look into walls
 * -Make a clear light model
 * -Add moving obstacles
 * -fix jumping into walls
 */

public class First3DGame extends ApplicationAdapter implements InputProcessor {
	
	private World world;
	private Shader shader;
	

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);

		shader = new Shader(1);
		
		//Set shininess
		shader.setShininess(30);
		
		
		BoxGraphic.create(shader.positionLoc, shader.normalLoc);
		SphereGraphic.create(shader.positionLoc, shader.normalLoc);
		SincGraphic.create(shader.positionLoc);
		CoordFrameGraphic.create(shader.positionLoc);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.setShaderMatrix(shader.modelMatrixLoc);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		
		
		world = new World(new Point3D(0,0,0), shader, 10000);
	}

	private void input()
	{
		float dt = Gdx.graphics.getDeltaTime();

		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {

		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {

		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {

		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {

		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {

		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.N)) {


		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {

		}
	}
	
	private void update()
	{
		
		float dt = Gdx.graphics.getDeltaTime();

		world.update(dt);
	}
	
	private void buildMaze(boolean showPlayer) {
		
	}
	
	private void display()
	{

		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void render () {
		
		
		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}

	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


}