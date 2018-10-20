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
	
	
	public void draw() {
		ModelMatrix.main.loadIdentityMatrix();

		
		float angle = 90f;
		//ModelMatrix.main.addRotationZ(angle);
		float s = (float)Math.sin((angle / 2.0) * Math.PI / 180.0);
		float c = (float)Math.cos((angle / 2.0) * Math.PI / 180.0);

		shader.setLightPosition(0.0f + c * 3.0f, 5.0f, 0.0f + s * 3.0f, 1.0f);
		//shader.setLightPosition(3.0f, 4.0f, 0.0f, 1.0f);
		//shader.setLightPosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);


		float s2 = Math.abs((float)Math.sin((angle / 1.312) * Math.PI / 180.0));
		float c2 = Math.abs((float)Math.cos((angle / 1.312) * Math.PI / 180.0));

		shader.setSpotDirection(s2, -0.3f, c2, 0.0f);
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

		//ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(position.x - 1.4616f, position.y, position.z + 2.8852f + 1f);
		ModelMatrix.main.addScale(0.01f, 0.01f, 0.01f);
		
		
		//ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		
		
		
		model.draw(shader);
		
		//ModelMatrix.main.popMatrix();
	}
}
