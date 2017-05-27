attribute vec4 a_Position;
attribute vec4 a_Color;

uniform mat4 perspectiveMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

varying vec4 v_Color;

void main()
{
	v_Color = a_Color;
	gl_Position = perspectiveMatrix * viewMatrix * modelMatrix * a_Position;
}