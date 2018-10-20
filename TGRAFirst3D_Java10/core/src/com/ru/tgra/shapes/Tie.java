package com.ru.tgra.shapes;

import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

public class Tie extends Player {
	
	private MeshModel model;
	private Shader shader;
	
	public Tie(Point3D position, Vector3D direction, Shader shader) {
		super(position, direction);
		
		model = G3DJModelLoader.loadG3DJFromFile("TIEfighter.g3dj");
		this.shader = shader;
		
	}
	
	private void updateCamera() {
		//System.out.println(cam.eye.x + "," + cam.eye.y + ", " + cam.eye.z);
		//cam.look(new Point3D(0,2,0), player.position, new Vector3D(0,1,0));
		cam.look(Point3D.add(new Point3D(position.x,position.y + 0.4f, position.z),direction), position, new Vector3D(0,1,0));
	}
	
	public void draw() {
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.pushMatrix();
		

		shader.setLightPosition(position.x, position.y + 1, position.z, 1.0f);


		//shader.setSpotDirection(s2, -0.3f, c2, 0.0f);
		//shader.setSpotDirection(-cam.n.x, -cam.n.y, -cam.n.z, 0.0f);
		shader.setSpotExponent(0.0f);
		shader.setConstantAttenuation(1.0f);
		shader.setLinearAttenuation(0.00f);
		shader.setQuadraticAttenuation(0.00f);

		//shader.setLightColor(s2, 0.4f, c2, 1.0f);
		shader.setLightColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		shader.setGlobalAmbient(0.3f, 0.3f, 0.3f, 1);

		//shader.setMaterialDiffuse(s, 0.4f, c, 1.0f);
		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		//shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0, 0, 0, 1);
		shader.setShininess(50.0f);
		
	
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		ModelMatrix.main.addRotationY(angleY);
		ModelMatrix.main.addTranslation(-1.4616f, 0, 2.8852f);

		
		ModelMatrix.main.addScale(0.01f, 0.01f, 0.01f);

		
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		model.draw(shader);
		
		
		updateCamera();
		
		ModelMatrix.main.popMatrix();
	}
	
}
