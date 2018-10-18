package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

class LightSource{
	public int u_lightColorLoc;
	public int u_lightPositionLoc;
	public int u_directionalLoc;
	public LightSource(int u_lightColorLoc, int u_lightPositionLoc, int u_directionalLoc) {
		this.u_lightColorLoc = u_lightColorLoc;
		this.u_lightPositionLoc = u_lightPositionLoc;
		this.u_directionalLoc = u_directionalLoc;
	}
}

public class Shader {
	public int renderingProgramID;
	public int vertexShaderID;
	public int fragmentShaderID;

	public int positionLoc;
	public int normalLoc;

	public int modelMatrixLoc;
	public int viewMatrixLoc;
	public int projectionMatrixLoc;

	public int u_lightColorLoc;
	public int u_lightPositionLoc;
	
	public int u_materialDiffuseLoc;
	public int u_materialSpecularLoc;
	public int u_materialAmbientLoc;
	public int u_shininessLoc;
	
	public int u_globalAmbientLoc;
	
	public int u_eyePositionLoc;
	
	public int u_lightNumberLoc;
	
	private int lightNumber;
	
	private LightSource[] lights;
	
	public Shader(int lightNumber) {
		this.lightNumber = lightNumber;
		
		intialize_shader();
		
	}
	
	
	private void intialize_shader() {
		System.out.println("Initializing shader...");
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/simple3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/simple3D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);
		
		System.out.println("Compiling shaders");
		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);
		
		
		System.out.println(Gdx.gl.glGetProgramInfoLog(vertexShaderID));
		System.out.println(Gdx.gl.glGetProgramInfoLog(fragmentShaderID));
		renderingProgramID = Gdx.gl.glCreateProgram();
		
	
		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);
	
		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);
		
		
		//Set matrice handles
		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");
		

        //Set material handles
		u_materialDiffuseLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_material_diffuse");
		u_materialSpecularLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_material_specular");
		u_materialAmbientLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_material_ambient");
		u_shininessLoc						= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_shininess");
		
		//Set light handles
		u_lightColorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light_color");
		u_lightPositionLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light_position");


		//Set light handles for multiple lights
		this.lights = new LightSource[this.lightNumber];
		u_lightNumberLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light_number");
		for(int i = 0; i < this.lightNumber; i++) {
			int colorLoc       = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light[" + i + "].u_light_color");
			int positionLoc    = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light[" + i + "].u_light_position");
			int directionalLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_light[" + i + "].u_directional");
			lights[i] = new LightSource(colorLoc, positionLoc, directionalLoc);
			//System.out.println(colorLoc + "," + positionLoc);
		}
		Gdx.gl.glUseProgram(renderingProgramID);
		
		System.out.println(this.lightNumber);
		Gdx.gl.glUniform1i(u_lightNumberLoc, this.lightNumber);
	}
	private boolean validLightNumber(int lightNumber) {
		if (0 <= lightNumber && lightNumber < this.lightNumber) return true;
		return false;
	}
	public int getLightColorLoc(int lightNumber) {
		if (validLightNumber(lightNumber)) {
			return lights[lightNumber].u_lightColorLoc;
		}
		return -1;
	}
	
	public int getLightPositionLoc(int lightNumber) {
		if (validLightNumber(lightNumber)) {
			return lights[lightNumber].u_lightPositionLoc;
		}
		return -1;
	}
	
	public void setGlobalAmbient(float r, float g, float b) {
		Gdx.gl.glUniform4f(u_globalAmbientLoc, r, g,b, 1.0f);
	}
	public void setMaterialDiffuse(float r, float g, float b) {
		Gdx.gl.glUniform4f(u_materialDiffuseLoc, r, g,b, 1.0f);
	}
	
	public void setMaterialSpecular(float r, float g, float b) {
		Gdx.gl.glUniform4f(u_materialSpecularLoc, r, g,b, 1.0f);
	}
	
	public void setMaterialAmbient(float r, float g, float b) {
		Gdx.gl.glUniform4f(u_materialAmbientLoc, r, g,b, 1.0f);
	}
	
	public void setShininess(int val) {
		Gdx.gl.glUniform1f(u_shininessLoc, val);
	}
	
	public void setEyePosition(Point3D eye) {
		Gdx.gl.glUniform4f(u_eyePositionLoc, eye.x, eye.y, eye.z, 1);
	}
	
	public void setLightDirectional(int lightNumber, int directional) {
		if(validLightNumber(lightNumber)) {
			Gdx.gl.glUniform1i(lights[lightNumber].u_directionalLoc, directional);
		}
	}
	
	public void setLightColor(int lightNumber, float r, float g, float b) {
		if(validLightNumber(lightNumber)) {
			//System.out.println("Setting light color: " + lights[lightNumber].u_lightColorLoc);
			Gdx.gl.glUniform4f(lights[lightNumber].u_lightColorLoc, r ,g, b, 1);
		}
	}
	
	public void setLightPosition(int lightNumber, float x, float y, float z) {
		if(validLightNumber(lightNumber)) {
			//System.out.println("Setting light position: " + lights[lightNumber].u_lightPositionLoc);
			Gdx.gl.glUniform4f(lights[lightNumber].u_lightPositionLoc, x ,y, z, 1);
		}
	}
}
