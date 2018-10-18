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
	
	private Camera cam;
	private Camera orthoCam;

	private Player player;

	private float degrees;
	private float angel;
	
	
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


		orthoCam = new Camera(shader.viewMatrixLoc, shader.projectionMatrixLoc);
		
		cam = new Camera(shader.viewMatrixLoc, shader.projectionMatrixLoc);
		
		cam.perspectiveProjection(90.0f, 1f, 0.1f, 100.0f);
		orthoCam.orthographicProjection(-10, 10, -10, 10, 1, 100);
		
		//playerStart=new Point3D(2,1,2);
		//player = new Player(new Vector3D(5,0,0),new Point3D(playerStart.x,playerStart.y,playerStart.z) , cam, orthoCam, maze, 0.1f);

		//Set up lights
		/*shader.setGlobalAmbient(1.0f, 1.0f, 1.0f);
		shader.setLightColor(Lights.TOP_LIGHT, 1f,1f, 1f);
		shader.setLightPosition(Lights.TOP_LIGHT, maze.getWidth()/2, 1000, maze.getHeight()/2);
		shader.setLightDirectional(Lights.TOP_LIGHT, 1);
				
		shader.setLightColor(Lights.MAIN_LIGHT, 1f, 1f, 1f);
		shader.setLightPosition(Lights.MAIN_LIGHT, cam.eye.x, cam.eye.y, cam.eye.z);
		shader.setLightDirectional(Lights.MAIN_LIGHT,0	);
		
		shader.setLightColor(Lights.GOAL_LIGHT1, 1f, 0f, 0f);
		shader.setLightPosition(Lights.GOAL_LIGHT1, maze.getGoalPosition().x + 2f, 3, maze.getGoalPosition().z + 2f);
		shader.setLightDirectional(Lights.GOAL_LIGHT1, 1);
		
		shader.setLightColor(Lights.GOAL_LIGHT2, 0f, 1f, 0f);
		shader.setLightPosition(Lights.GOAL_LIGHT2, maze.getGoalPosition().x - 2f, 1, maze.getGoalPosition().z + 2f);
		shader.setLightDirectional(Lights.GOAL_LIGHT2, 1);
		
		shader.setLightColor(Lights.GOAL_LIGHT3, 0f, 0f, 1f);
		shader.setLightPosition(Lights.GOAL_LIGHT3, maze.getGoalPosition().x - 2f, 1, maze.getGoalPosition().z - 2f);
		shader.setLightDirectional(Lights.GOAL_LIGHT3, 1);
		
		shader.setLightColor(Lights.GOAL_LIGHT4, 0f, 0.5f, 0.5f);
		shader.setLightPosition(Lights.GOAL_LIGHT4, maze.getGoalPosition().x + 2f, 1, maze.getGoalPosition().z - 2f);
		shader.setLightDirectional(Lights.GOAL_LIGHT4, 1);8*/
		
				

	}

	private void input()
	{
		float dt = Gdx.graphics.getDeltaTime();
		float moveSpeed = 1, lookSpeed = 75;

		
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			//cam.yaw(lookSpeed*dt);
			player.rotateY(90, dt);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			player.rotateY(-90, dt);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			//TODO add look up
			player.rotateUp(1, dt);
			//cam.pitch(-lookSpeed*dt);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			//TODO add look down
			player.rotateUp(-1, dt);
			//cam.pitch(lookSpeed*dt);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {

	
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {

			//cam.slide(moveSpeed*dt, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {

			//cam.walkForward(dt);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {

			//cam.walkForward(-dt);
			//cam.slide(0, 0, moveSpeed*dt);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.N)) {


		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {

		}
	}
	
	private void update()
	{
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		//player.updateCam();
		Gdx.gl.glUniformMatrix4fv(shader.viewMatrixLoc, 1, false, cam.getViewMatrix());

		
		//do all updates to the game
		

				
		
		/*shader.setLightPosition(Lights.GOAL_LIGHT1, maze.getGoalPosition().x + 2f, 0.5f, maze.getGoalPosition().z + 2f);

		shader.setLightPosition(Lights.GOAL_LIGHT2, maze.getGoalPosition().x - 2f, 0.5f, maze.getGoalPosition().z + 2f);

		shader.setLightPosition(Lights.GOAL_LIGHT3, maze.getGoalPosition().x - 2f, 0.5f, maze.getGoalPosition().z - 2f);

		shader.setLightPosition(Lights.GOAL_LIGHT4, maze.getGoalPosition().x + 2f, 0.5f, maze.getGoalPosition().z - 2f);*/

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