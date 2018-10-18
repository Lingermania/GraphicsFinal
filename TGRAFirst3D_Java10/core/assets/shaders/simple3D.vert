#ifdef GL_ES
precision mediump float;
#endif


attribute vec3 a_position;
attribute vec3 a_normal;

uniform vec4 u_material_diffuse;
uniform vec4 u_material_specular;
uniform vec4 u_material_ambient;
uniform float u_shininess;

uniform vec4 u_global_ambient;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_light_position;
uniform vec4 u_light_color;

uniform vec4 u_eye_position;

varying vec4 v_color;

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

	v_color = u_global_ambient * u_material_ambient;
	for(int i = 0; i < u_light_number; i++){
		vec4 v = u_eye_position - position;
		vec4 s;
		
		if (u_light[i].u_directional == 0){
			s = u_light[i].u_light_position - position;
		}
		else{
			s = u_light[i].u_light_position ;
		}
		vec4 h = s + v;
		float lambert = max(0.0, dot(normal, s) / (length(normal)*length(s)));
		float phong   = max(0.0, dot(normal, h) / (length(normal)*length(h)));
		
		v_color += lambert * u_light[i].u_light_color * u_material_diffuse + pow(phong, u_shininess) * u_light[i].u_light_color * u_material_specular;
		
	}
	
	position = u_viewMatrix * position;


	gl_Position = u_projectionMatrix * position;
}