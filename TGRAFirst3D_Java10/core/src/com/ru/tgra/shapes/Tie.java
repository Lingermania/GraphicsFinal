package com.ru.tgra.shapes;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Tie extends Player {
	
	private MeshModel model;
	private Shader shader;
	private Sound laser;
	private boolean canShoot;
	private Timer timer;
	protected ExcellerationEffect exc;
	
	public Tie(Point3D position, Vector3D direction, Shader shader, World world) {
		super(position, direction, world);
		
		model = G3DJModelLoader.loadG3DJFromFile("TIEfighter.g3dj");
		laser = Gdx.audio.newSound(Gdx.files.internal("sounds/Quadlaser turret fire.mp3"));
		this.shader = shader;
		this.canShoot = true;
		this.exc = new ExcellerationEffect(shader);
		
		timer = new Timer();
	}
	
	
	public void shoot() {
		float t = (new Date()).getTime();
		
		System.out.println(t);
		if (canShoot) {
			Random rand = new Random();
			laser.play(0.5f);
			
			for(int i = 0; i < 4; i++) {
				float a0 = (float)Math.pow(-1, rand.nextInt(2));
				float a1 = (float)Math.pow(-1, rand.nextInt(2));
				float a2 = (float)Math.pow(-1, rand.nextInt(2));
	
				Point3D pos = new Point3D(position.x + a0*rand.nextFloat()*0.8f, position.y+a1*rand.nextFloat()*0.8f, position.z+a2*rand.nextFloat()*0.8f);
				Point3D augmentedPlayerPosition = new Point3D(position.x, position.y, position.z);
				
				augmentedPlayerPosition.x -= direction.x*100;
				augmentedPlayerPosition.y -= direction.y*100;
				augmentedPlayerPosition.z -= direction.z*100;
				
				Vector3D direction = Vector3D.difference(pos,augmentedPlayerPosition);
				direction.normalize();
				lasers.add(new Laser(pos,
						  Vector3D.scale(direction, 80), 
						  new Point3D(angleX, angleY, angleZ), 
						  shader));
			}
			canShoot = false;
			
			timer.schedule(new TimerTask()
					{
						@Override
						public void run() {
							canShoot = true;
						}
					}
					, 750);
		}
		
		
	}
	
	public void drawLasers() {
		for(Laser l : lasers) {
			l.draw();
		}
	}
	
	public void draw() {
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.pushMatrix();
		

		//shader.setLightPosition(Lights.TIE_LIGHT,position.x-4, position.y+2, position.z-4, 1.0f);

		
		//shader.setSpotDirection(s2, -0.3f, c2, 0.0f);
		//shader.setSpotDirection(-cam.n.x, -cam.n.y, -cam.n.z, 0.0f);
		shader.setSpotExponent(0.0f);
		shader.setConstantAttenuation(1.0f);
		shader.setLinearAttenuation(0.00f);
		shader.setQuadraticAttenuation(0.00f);

		//shader.setLightColor(s2, 0.4f, c2, 1.0f);
		//shader.setLightColor(Lights.TIE_LIGHT,1.0f, 1.0f, 1.0f, 0.75f);
		//shader.setLightDirectional(Lights.TIE_LIGHT, 0);
		
		//shader.setGlobalAmbient(1f, 1f,1f, 1);

		//shader.setMaterialDiffuse(s, 0.4f, c, 1.0f);
		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		//shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0, 0, 0, 1);
		shader.setShininess(50.0f);
			
		if (this.alive)
		{
			ModelMatrix.main.addTranslation(position.x, position.y, position.z);
			
			ModelMatrix.main.addRotationY(angleY);
			ModelMatrix.main.addRotationX(angleX);
			ModelMatrix.main.addRotationZ(angleZ);
			ModelMatrix.main.addTranslation(-1.4616f, 0, 2.8852f);
	
			
			ModelMatrix.main.addScale(0.01f, 0.01f, 0.01f);
	
			
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
	
			model.draw(shader);
		}
		drawLasers();
		exc.draw(this);
		
		updateCamera();
		
		ModelMatrix.main.popMatrix();
	}
	
}
