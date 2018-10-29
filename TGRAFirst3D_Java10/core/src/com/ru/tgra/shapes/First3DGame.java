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
		
		

		shader = new Shader(3);

		world = new World(new Point3D(0,0,0), shader, 1000000);
		
	}

	private void input()
	{
	}
	
	private void update()
	{
		//System.out.println(world.player.position.x + ", " + world.player.position.y + ", " + world.player.position.z);
		//System.out.println(world.cam.eye.x + ", " + world.cam.eye.y + ", " + world.cam.eye.z);
		float deltaTime = Gdx.graphics.getDeltaTime();

		angle += 180.0f * deltaTime;

		if(Gdx.input.isKeyPressed(Input.Keys.A) && world.player.alive== true) {
			//world.cam.slide(-3.0f * deltaTime, 0, 0);
			if(!world.intro) {
				world.player.rotateY(90, deltaTime);
				world.player.left();
			}
			
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D) && world.player.alive== true) {
			if(!world.intro) {
				world.cam.slide(3.0f * deltaTime, 0, 0);
				world.player.rotateY(-90, deltaTime);
				world.player.right();
			}
			
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W) && world.player.alive== true) {
			//world.cam.slide(0, 0, -3.0f * deltaTime);
			if(!world.intro) {
				world.player.move(-deltaTime);
				world.player.neutralZ();
				world.player.forward();
			}
			
			//cam.walkForward(3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S) && world.player.alive== true) {
			//world.cam.slide(0, 0, 3.0f * deltaTime);
			
			if(!world.intro) {
				world.player.move(deltaTime);
				world.player.neutralZ();
				world.player.backward();
			}
			
			//cam.walkForward(-3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R) && world.player.alive== true) {
			//world.test_exp();
			//world.cam.slide(0, 3.0f * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F) && world.player.alive== true) {
			
			//world.cam.slide(0, -3.0f * deltaTime, 0);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && world.player.alive== true) {
			//world.player.rotateZ(-45f, deltaTime);
			//world.cam.yaw(-90.0f * deltaTime);
			//cam.rotateY(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && world.player.alive== true) {
			//world.player.rotateZ(45f, deltaTime);
			//world.cam.yaw(90.0f * deltaTime);
			
			//cam.rotateY(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP) && world.player.alive== true) {
			if(!world.intro) {
				world.player.rotateX(-90.0f, deltaTime);
				world.setPlayerLightIntensity(true,deltaTime);
			}
			
			//world.player.rotateUp(4.0f, deltaTime);
			//world.cam.walkForward(3.0f * deltaTime);
			//world.cam.pitch(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && world.player.alive== true) {
			//world.player.rotateUp(-4.0f, deltaTime);
			
			if(!world.intro) {
				world.player.rotateX(90.0f, deltaTime);
				world.setPlayerLightIntensity(false,deltaTime);
			}
			
			//world.cam.walkForward(-3.0f * deltaTime);
			//world.cam.pitch(90.0f * deltaTime);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && world.player.alive== true) {
			//Shoot laser
			if(!world.intro) {
				world.player.shoot();
			}
			
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q) && world.player.alive== true) {
			
			if(!world.intro) {
				world.cam.roll(-90.0f * deltaTime);
			}
			
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E) && world.player.alive== true) {
			if(!world.intro) {
				world.cam.roll(90.0f * deltaTime);
			}
			
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