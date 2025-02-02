#version 120

#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_alphaTexture;

uniform float u_usesDiffuseTexture;
uniform float u_usesAlphaTexture;

uniform vec4 u_globalAmbient;

uniform vec4 u_lightColor;

uniform vec4 u_spotDirection;
uniform float u_spotExponent;

uniform float u_constantAttenuation;
uniform float u_linearAttenuation;
uniform float u_quadraticAttenuation;

uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform float u_materialShininess;

uniform vec4 u_materialEmission;

varying vec2 v_uv;
varying vec4 v_normal;
varying vec4 v_s[15];
varying vec4 v_h[15];

struct LightSource
{
	vec4 u_light_position;
	vec4 u_light_color;
	int  u_directional; //0 is false everything else is true
};

uniform LightSource u_light[15]; //Defines maximum number of lights
uniform int u_light_number;

uniform int u_background;

void main()
{

	vec4 materialDiffuse;
	if(u_usesDiffuseTexture == 1.0)
	{
		materialDiffuse = texture2D(u_diffuseTexture, v_uv);  //also * u_materialDiffuse ??? up to you.
	}
	else
	{
		materialDiffuse = u_materialDiffuse;
	}
	
	if(u_usesAlphaTexture == 1.0)
	{
		materialDiffuse.a = texture2D(u_alphaTexture, v_uv).r;  //also * u_materialDiffuse ??? up to you.
	}


	vec4 materialSpecular = u_materialSpecular;

	//Lighting
	vec4 lightCalcColor = u_globalAmbient * materialDiffuse;
	
	for(int i = 0; i < u_light_number; i++){
		float length_s = length(v_s[i]);
		
		float lambert = max(0.0, dot(v_normal, v_s[i]) / (length(v_normal) * length_s));
		float phong = max(0.0, dot(v_normal, v_h[i]) / (length(v_normal) * length(v_h[i])));
	
		vec4 diffuseColor = lambert * u_light[i].u_light_color * materialDiffuse;
	
		vec4 specularColor = pow(phong, u_materialShininess) * u_light[i].u_light_color * materialSpecular;
	
		float attenuation = 1.0;
		if(u_spotExponent != 0.0)
		{
			float spotAttenuation = max(0.0, dot(-v_s[i], u_spotDirection) / (length_s * length(u_spotDirection)));
			spotAttenuation = pow(spotAttenuation, u_spotExponent);
			attenuation *= spotAttenuation;
		}
		attenuation *= 1.0 / (u_constantAttenuation + length_s * u_linearAttenuation + pow(length_s, 2.0) * u_quadraticAttenuation);
			
		if (u_background == 1)
		{	
			lightCalcColor = lightCalcColor + attenuation * (diffuseColor);// + specularColor);
		}
		else
		{
			lightCalcColor = lightCalcColor + attenuation * (diffuseColor + specularColor);
		}
	}
	// end for each light
	
	

	//gl_FragColor = u_globalAmbient + u_materialEmission + light1CalcColor;
	gl_FragColor =  u_materialEmission + lightCalcColor;
	gl_FragColor.a = materialDiffuse.a;
}