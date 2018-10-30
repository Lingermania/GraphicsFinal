package com.ru.tgra.shapes;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;


public class Explosion {
	
	public Point3D position;
	public Shader shader;
	public Particle[] particles;
	public Texture tex;
	public float velocity, size;
	private int maxSimulations, simulations;
	
	public Explosion(Point3D position, Shader shader) {
		this.position = position;
		this.shader = shader;
		this.maxSimulations = 1000;
		this.simulations = 0;
		this.velocity = 1;
		this.size = 1;
		this.tex = new Texture(Gdx.files.internal("textures/particle.png"));
		
		particles = new Particle[5000];
		initializeParticles();
	}
	
	public Explosion(Point3D position, Shader shader, float velocity, float size) {
		this.position = position;
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
		
		Vector3D direction = new Vector3D((float)Math.pow(-1, rand.nextInt(2))*rand.nextFloat(),
				(float)Math.pow(-1, rand.nextInt(2))*rand.nextFloat(),
				(float)Math.pow(-1, rand.nextInt(2))*rand.nextFloat());
		
		direction.normalize();
		
		direction.scale(velocity * rand.nextFloat()*0.1f);
		
		Point3D color = new Point3D(1.0f, rand.nextFloat(), 0f);
		
		return new Particle(new Point3D(position.x, position.y, position.z),
				direction,
				color,
				rand.nextFloat());
	}
	private void initializeParticles() {
		
		for(int i = 0; i < particles.length; i++) {

			particles[i] = randomParticle();
					
		}
	}
	
	public void simulate() {
		if (this.simulations < this.maxSimulations) {
			for(int i = 0; i < particles.length; i++) {
				particles[i].simulate(0.95f);
			}
			this.simulations++;
		}
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

		if (this.simulations < this.maxSimulations) {
			//System.out.println("Drawing explosions");
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
					ModelMatrix.main.addScale(size * rand.nextFloat()*0.8f, size* rand.nextFloat()*0.8f, size* rand.nextFloat()*0.8f);
					
					shader.setMaterialDiffuse(p.color.x, p.color.y, p.color.z, p.life*0.7f);
					
					//shader.setMaterialDiffuse(1.0f, rand.nextFloat(),0f, p.life*0.9f);
					shader.setMaterialSpecular(1.0f, rand.nextFloat(),0f, 1f);
					
					
					shader.setModelMatrix(ModelMatrix.main.getMatrix());
					SpriteGraphic.drawSprite(shader, tex, null);
					//BoxGraphic.drawSolidCube(shader, null);
					
					ModelMatrix.main.popMatrix();
				}
				
			}
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}
	
	
}
