package com.ru.tgra.shapes;

public class Laser {
	private Point3D position;
	private Vector3D direction;
	private Point3D angles;
	private Shader shader;
	
	public Laser(Point3D position, Vector3D direction, Point3D angles, Shader shader) {
		this.position  = position;
		this.direction = direction;
		this.shader = shader;
		this.angles = angles;
	}
	
	
	public void simulate(float dt) {
		this.position.x -= direction.x * dt;
		this.position.y -= direction.y * dt;
		this.position.z -= direction.z * dt;
	}
	
	public void draw() {
		shader.setMaterialDiffuse(1.0f,0f, 0f, 1.0f);
		shader.setMaterialSpecular(1.0f, 0f, 0f, 1.0f);
		ModelMatrix.main.loadIdentityMatrix();
		
		ModelMatrix.main.pushMatrix();
		
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		ModelMatrix.main.addRotationY(angles.y);
		ModelMatrix.main.addRotationX(angles.x);
		ModelMatrix.main.addRotationZ(angles.z);
		
		ModelMatrix.main.addScale(0.01f, 0.01f, 0.4f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		
		
		
		SphereGraphic.drawSolidSphere(shader, null, null);
		ModelMatrix.main.popMatrix();
	}
}
