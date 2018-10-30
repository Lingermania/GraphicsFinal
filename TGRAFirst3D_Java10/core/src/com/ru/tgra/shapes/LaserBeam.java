package com.ru.tgra.shapes;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class LaserBeam {
	public Point3D position, target;
	public Shader shader;
	public Particle[] particles;
	public Texture tex;
	public float velocity, size, targetRadius;
	private int maxSimulations, simulations;
	
	public LaserBeam(Point3D position, Point3D target, Shader shader, float velocity, float size, float targetRadius) {
		this.position = position;
		this.target = target;
		this.targetRadius = targetRadius;
		this.shader = shader;
		this.maxSimulations = 1000;
		this.simulations = 0;
		this.velocity = velocity;
		this.size = size;
		
		this.tex = new Texture(Gdx.files.internal("textures/particle.png"));
		
		particles = new Particle[5000];
		initializeParticles();
	}
	
	private Particle randomParticle() {
		Random rand = new Random();
		
		Vector3D direction = Vector3D.difference(this.target,this.position);
		
		direction.normalize();
		
		direction.scale(velocity * rand.nextFloat()*0.1f);
		
		Point3D color = new Point3D(0f, 1f, 0.3f - rand.nextFloat()/4.0f);
		
		float a0 = (float)Math.pow(-1, rand.nextInt(2));
		float a1 = (float)Math.pow(-1, rand.nextInt(2));
		float a2 = (float)Math.pow(-1, rand.nextInt(2));
		
		Point3D partPos = new Point3D(position.x + a0*rand.nextFloat()*size/2.0f,
									  position.y+ a1*rand.nextFloat()*size/2.0f,
									  position.z+ a2*rand.nextFloat()*size/2.0f);
		
		return new Particle(partPos,
				direction,
				color,
				size);
	}
	
	private void initializeParticles() {
		
		for(int i = 0; i < particles.length; i++) {

			//particles[i] = randomParticle();
					
		}
	}
	
	public void hitTarget(Particle p) {
		float len = Vector3D.difference(p.position, target).length();
		Random rand = new Random();
		
		if (len < targetRadius) {
			//give random direction
			float a0 = (float)Math.pow(-1, rand.nextInt(2));
			float a1 = (float)Math.pow(-1, rand.nextInt(2));
			float a2 = (float)Math.pow(-1, rand.nextInt(2));
			
			p.direction.x = a0 * rand.nextFloat();
			p.direction.y = a1 * rand.nextFloat();
			p.direction.z = a2 * rand.nextFloat();
			
			p.direction.normalize();
			p.direction.scale(size*2f);
			
			p.size = size*(1 + rand.nextFloat());
			p.color.x = 0.5f + rand.nextFloat()/2.0f;
			p.color.y = 0.5f - rand.nextFloat()/2.0f;
			p.color.z = 0.1f;
			
		}
	}
	public void simulate() {
		//Add 200 per simulation
		int batchSize = 200;
		for(int i = 0; i < particles.length; i++) {
			
			
			if(particles[i] == null && batchSize > 0) {
				batchSize--;
				particles[i] = randomParticle();
			}
			else if (particles[i] == null){
				continue;
			}
			
			particles[i].simulate(0.97f);
			if(particles[i].life < 0.5) {
				particles[i] = randomParticle();
			}
			//changes particle direction if it hits 
			hitTarget(particles[i]);
		}
		this.simulations++;
	}
	
	public void simulate(float exp) {
		if (this.simulations < this.maxSimulations) {
			for(int i = 0; i < particles.length; i++) {
				particles[i].simulate(exp);
			}
			this.simulations++;
		}
	}
	
	public void draw() {

		//System.out.println("Drawing explosions");
		Random rand = new Random();
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		
		for(Particle p : particles) {
			if (p == null) continue;
			if (p.life > 0.1) {
				ModelMatrix.main.loadIdentityMatrix();
				
				ModelMatrix.main.pushMatrix();
				
				
				
				
				ModelMatrix.main.addTranslation(p.position.x, p.position.y, p.position.z);
				ModelMatrix.main.addRotationZ(rand.nextFloat()*360f);
				ModelMatrix.main.addRotationY(rand.nextFloat()*360f);
				ModelMatrix.main.addRotationX(rand.nextFloat()*360f);
				
				ModelMatrix.main.addScale(p.size * rand.nextFloat()*0.8f, p.size* rand.nextFloat()*0.8f, p.size* rand.nextFloat()*0.8f);
				
				shader.setMaterialDiffuse(p.color.x, p.color.y, p.color.z, p.life*0.7f);
				
				//shader.setMaterialDiffuse(1.0f, rand.nextFloat(),0f, p.life*0.9f);
				shader.setMaterialSpecular(1.0f, rand.nextFloat(),0f, 1f);
				
				
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				SpriteGraphic.drawSprite(shader, tex, null);
				//BoxGraphic.drawSolidCube(shader, null);
				
				ModelMatrix.main.popMatrix();
			}
			
		}
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
}
