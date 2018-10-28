package com.ru.tgra.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

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

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;
	private int uvLoc;
	
	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int projectionMatrixLoc;

	private boolean usesDiffuseTexture = false;
	private boolean usesAlphaTexture   = false;
	
	private int usesDiffuseTexLoc;
	private int diffuseTextureLoc;

	private int eyePosLoc;

	private int globalAmbLoc;

	private int spotDirLoc;
	private int spotExpLoc;
	private int constantAttLoc;
	private int linearAttLoc;
	private int quadraticAttLoc;
	
	private int lightColorLoc;
	private int matDifLoc;
	private int matSpecLoc;
	private int matShineLoc;
	private int matEmissionLoc;
	
	private int usesAlphaTexLoc;
	private int alphaTextureLoc;
	
	private int lightNumber;
	public int u_lightNumberLoc;
	
	private LightSource[] lights;
	
	private int backgroundLoc;

	public Shader(int lightNumber)
	{
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/fragmentLighting3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/fragmentLighting3D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);

		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);

		System.out.println("Vertex shader compile messages:");
		System.out.println(Gdx.gl.glGetShaderInfoLog(vertexShaderID));
		System.out.println("Fragment shader compile messages:");
		System.out.println(Gdx.gl.glGetShaderInfoLog(fragmentShaderID));

		renderingProgramID = Gdx.gl.glCreateProgram();

		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);
	
		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);

		uvLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_uv");
		Gdx.gl.glEnableVertexAttribArray(uvLoc);

		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		usesDiffuseTexLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_usesDiffuseTexture");
		diffuseTextureLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_diffuseTexture");
		
		usesAlphaTexLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_usesAlphaTexture");
		alphaTextureLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_alphaTexture");

		eyePosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");

		globalAmbLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbient");

		//lightPosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPosition");

		spotDirLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_spotDirection");
		spotExpLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_spotExponent");
		constantAttLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_constantAttenuation");
		linearAttLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_linearAttenuation");
		quadraticAttLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_quadraticAttenuation");
	
		lightColorLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightColor");
		matDifLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
		matSpecLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
		matShineLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShininess");
		
		matEmissionLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialEmission");

		backgroundLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_background");
		
		this.lightNumber = lightNumber;
		
		initilize_lights();
		
		Gdx.gl.glUseProgram(renderingProgramID);
		
	}
	
	private void initilize_lights()
	{
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

	public void setDiffuseTexture(Texture tex)
	{
		if(tex == null)
		{
			Gdx.gl.glUniform1f(usesDiffuseTexLoc, 0.0f);
			usesDiffuseTexture = false;
		}
		else
		{
			tex.bind(0);
			Gdx.gl.glUniform1i(diffuseTextureLoc, 0);
			Gdx.gl.glUniform1f(usesDiffuseTexLoc, 1.0f);
			usesDiffuseTexture = true;

			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		}
	}
	
	public void setAlphaTexture(Texture tex)
	{
		if(tex == null)
		{
			Gdx.gl.glUniform1f(usesAlphaTexLoc, 0.0f);
			usesAlphaTexture = false;
		}
		else
		{
			tex.bind(0);
			Gdx.gl.glUniform1i(alphaTextureLoc, 0);
			Gdx.gl.glUniform1f(usesAlphaTexLoc, 1.0f);
			usesAlphaTexture = true;

		}
	}
	public boolean usesTextures()
	{
		return (usesDiffuseTexture || usesAlphaTexture /* || usesSpecularTexture ... etc.*/);
	}

	public void setBackground(boolean isBackground)
	{
		if (isBackground)
		{
			Gdx.gl.glUniform1i(backgroundLoc,1);
		}
		else
		{
			Gdx.gl.glUniform1i(backgroundLoc,0);
		}
	}
	public void setEyePosition(float x, float y, float z, float w)
	{
		Gdx.gl.glUniform4f(eyePosLoc, x, y, z, w);
	}
	public void setGlobalAmbient(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(globalAmbLoc, r, g, b, a);
	}
	private boolean validLightNumber(int lightNumber) {
		if (0 <= lightNumber && lightNumber < this.lightNumber) return true;
		return false;
	}
	
	public void setLightDirectional(int lightNumber, int directional) {
		if(validLightNumber(lightNumber)) {
			Gdx.gl.glUniform1i(lights[lightNumber].u_directionalLoc, directional);
		}
	}
	
	public void setLightPosition(int lightNumber, float x, float y, float z, float w)
	{
		if(validLightNumber(lightNumber)) {
			Gdx.gl.glUniform4f(lights[lightNumber].u_lightPositionLoc, x, y, z, w);
		}
	}

	public void setLightColor(int lightNumber,float r, float g, float b, float a)
	{
		if(validLightNumber(lightNumber)) {
			Gdx.gl.glUniform4f(lights[lightNumber].u_lightColorLoc, r, g, b, a);
		}
	}
	
	public void setSpotDirection(float x, float y, float z, float w)
	{
		Gdx.gl.glUniform4f(spotDirLoc, x, y, z, w);
	}
	public void setSpotExponent(float exp)
	{
		Gdx.gl.glUniform1f(spotExpLoc, exp);
	}
	public void setConstantAttenuation(float att)
	{
		Gdx.gl.glUniform1f(constantAttLoc, att);
	}
	public void setLinearAttenuation(float att)
	{
		Gdx.gl.glUniform1f(linearAttLoc, att);
	}
	public void setQuadraticAttenuation(float att)
	{
		Gdx.gl.glUniform1f(quadraticAttLoc, att);
	}


	public void setMaterialDiffuse(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(matDifLoc, r, g, b, a);
	}
	public void setMaterialSpecular(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(matSpecLoc, r, g, b, a);
	}
	public void setShininess(float shine)
	{
		Gdx.gl.glUniform1f(matShineLoc, shine);
	}
	public void setMaterialEmission(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(matEmissionLoc, r, g, b, a);
	}


	public int getVertexPointer()
	{
		return positionLoc;
	}
	public int getNormalPointer()
	{
		return normalLoc;
	}
	public int getUVPointer()
	{
		return uvLoc;
	}

	public void setModelMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
	}
	public void setViewMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
	}
	public void setProjectionMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
	}
}
