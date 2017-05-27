attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;

uniform mat4 perspectiveMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

varying vec2 v_TextureCoordinates;

void main()
{
	v_TextureCoordinates = a_TextureCoordinates;
	gl_Position = perspectiveMatrix * viewMatrix * modelMatrix * a_Position;
}