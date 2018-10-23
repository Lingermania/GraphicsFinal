package com.ru.tgra.shapes;

import java.util.Random;

class Particle{
	public Point3D position;
	public Vector3D direction;
	public float size;
	
	public Particle(Point3D position, Vector3D direction, float size) {
		this.position = position;
		this.direction = direction;
		this.size = size;
	}
	
	public void simulate() {
		this.position.x += direction.x;
		this.position.y += direction.y;
		this.position.z += direction.z;
	}
}
public class Explosion {
	
	public Point3D position;
	public Shader shader;
	public Particle[] particles;
	private int maxSimulations, simulations;
	
	public Explosion(Point3D position, Shader shader) {
		this.position = position;
		this.shader = shader;
		this.maxSimulations = 100;
		this.simulations = 0;
		
		particles = new Particle[10000];
		initializeParticles();
	}
	
	private void initializeParticles() {
		Random rand = new Random();
		
		for(int i = 0; i < particles.length; i++) {
			Vector3D direction = new Vector3D((float)Math.pow(-1, rand.nextInt(2))*rand.nextFloat(),
					(float)Math.pow(-1, rand.nextInt(2))*rand.nextFloat(),
					(float)Math.pow(-1, rand.nextInt(2))*rand.nextFloat());
			
			direction.normalize();
			direction.scale(rand.nextFloat());
			particles[i] = new Particle(new Point3D(position.x, position.y, position.z),
										direction,
										rand.nextFloat());
					
		}
	}
	
	public void simulate() {
		if (this.simulations < this.maxSimulations) {
			for(Particle p : particles) {
				p.simulate();
			}
			this.simulations++;
		}
	}
	
	public void draw() {

		if (this.simulations < this.maxSimulations) {
			System.out.println("Drawing explosions");
			Random rand = new Random();
			for(Particle p : particles) {
	
				ModelMatrix.main.loadIdentityMatrix();
				
				ModelMatrix.main.pushMatrix();
				
				
				
				ModelMatrix.main.addTranslation(p.position.x, p.position.y, p.position.z);
				ModelMatrix.main.addScale(rand.nextFloat()*0.3f, rand.nextFloat()*0.3f, rand.nextFloat()*0.3f);
				shader.setMaterialDiffuse(1.0f, rand.nextFloat(),0f, 1.0f);
				shader.setMaterialSpecular(1.0f, rand.nextFloat(),0f, 1.0f);
				
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube(shader, null);
				
				ModelMatrix.main.popMatrix();
			}
		}
	}
	
	
}
