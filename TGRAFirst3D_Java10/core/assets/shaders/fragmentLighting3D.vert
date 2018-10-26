#version 120

#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_uv;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePosition;

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


void main()
{
	
	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	vec4 normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	normal = u_modelMatrix * normal;
	
	//global coordinates




	//preparation for lighting
	
	v_normal = normal;
	
	for(int i = 0; i < u_light_number; i++){
	
		if (u_light[i].u_directional == 0){
			v_s[i] = normalize(u_light[i].u_light_position - position); //direction to the light
		}
		else{
			v_s[i] = u_light[i].u_light_position ;
		}
		vec4 v = normalize(u_eyePosition - position); //direction to the camera
		
		v_h[i] = v_s[i] + v;
	}




	position = u_viewMatrix * position;
	//eye coordinates

	v_uv = a_uv;
	gl_Position = u_projectionMatrix * position;
	//clip coordinates
}