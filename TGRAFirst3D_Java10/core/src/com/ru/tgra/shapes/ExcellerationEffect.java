package com.ru.tgra.shapes;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class ExcellerationEffect {
	
	
	Particle particles[];
	private boolean intialized = false;
	public Shader shader;
	
	public ExcellerationEffect(Shader shader) {
		this.particles = new Particle[50];
		this.shader    = shader;
	}
	
	private void initializeParticles(Player player) {
		
		for(int i = 0; i < particles.length; i++) {

			particles[i] = randomParticle(player);
					
		}
	}
	private int sign(float n) {
		return (int)Math.signum(n);
	}
	private Particle randomParticle(Player player) {
		Random rand = new Random();
		float a0 = (float)Math.pow(-1, rand.nextInt(2));
		float a1 = (float)Math.pow(-1, rand.nextInt(2));
		float a2 = (float)Math.pow(-1, rand.nextInt(2));
		
		
		Vector3D direction = new Vector3D(-player.direction.x, -player.direction.y, -player.direction.z);
		
		//Spawn particles in front of player
		Point3D pos = new Point3D(player.position.x - player.direction.x*3,
								  player.position.y - player.direction.y*3,
								  player.position.z - player.direction.z*3);
		
		
		//Give a random normal direction
		Vector3D perp1 = new Vector3D(player.direction.y, -player.direction.z, 0);
		Vector3D perp2 = new Vector3D(-player.direction.z, 0, player.direction.x);
		//Scale for randomness
		perp1.scale(rand.nextFloat());
		perp2.scale(rand.nextFloat());
		
		//random normal
		Vector3D norm = new Vector3D(perp1.x + perp2.x,
										perp1.y + perp2.y,
										perp1.z + perp2.z);		
		
		//Give physics speed to particle
		float avgSpeed = player.phys.avgSpeed();
		norm.normalize();
		norm.scale(avgSpeed);

		
		Point3D color = new Point3D(0f, rand.nextFloat(), 1f);
		
		
		
		return new Particle(pos,
				norm,
				color,
				rand.nextFloat(),
				avgSpeed*2);
	}
	
	private void simulate(Player player) {
	
		for(int i = 0; i < particles.length; i++) {
			particles[i].simulate(0.999f);
			
			if (particles[i].life < 0.1f) {
				//particles[i] = this.randomParticle(player);
			}
		}
		
		
	}
	
	public void draw(Player player) {
		if(!this.intialized) {
			this.initializeParticles(player);
			this.intialized = false;
		}
		
		simulate(player);
		Random rand = new Random();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		
		for(Particle p : particles) {
			
			if (p.life > 0.1) {
				ModelMatrix.main.loadIdentityMatrix();
				
				ModelMatrix.main.pushMatrix();
				
				ModelMatrix.main.addTranslation(p.position.x, p.position.y, p.position.z);
				ModelMatrix.main.addRotationZ(rand.nextFloat()*360f);
				ModelMatrix.main.addRotationY(rand.nextFloat()*360f);
				ModelMatrix.main.addRotationX(rand.nextFloat()*360f);
				ModelMatrix.main.addScale(rand.nextFloat()*0.04f, rand.nextFloat()*0.04f, rand.nextFloat()*0.04f);
				
				shader.setMaterialDiffuse(p.color.x, p.color.y, p.color.z, p.life*0.7f);
				
				//shader.setMaterialDiffuse(1.0f, rand.nextFloat(),0f, p.life*0.9f);
				shader.setMaterialSpecular(1.0f, rand.nextFloat(),0f, 1f);
				
				
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				SpriteGraphic.drawSprite(shader, null, null);
				//BoxGraphic.drawSolidCube(shader, null);
				
				ModelMatrix.main.popMatrix();
			}
			
		}
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
}
