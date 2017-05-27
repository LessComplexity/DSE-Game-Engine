package com.darksenic.dse2.util;

import android.content.Context;
import android.opengl.GLES20;

import com.darksenic.dse2.R;
import com.darksenic.dse2.data.Camera;
import com.darksenic.dse2.data.World;

public class TextureShader {
	private static int program;
	public static String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
	public static int aTextureLocation;
	public static String A_POSITION = "a_Position";
	public static int aPositionLocation;
	public static String U_TEXTURE_UNIT = "u_TextureUnit";
	public static int uTextureUnitLocation;
	
	public static void useProgram(){
		GLES20.glUseProgram(program);
	}
	
	public static int programID(){
		return program;
	}
	
	public static void loadProgram(Context context){
		String vertexCode = ResourceLoader.readTextFromFile(context, R.raw.vertex_texture);
		String fragmentCode = ResourceLoader.readTextFromFile(context, R.raw.fragment_texture);
		
		int vertexShader = ShaderHelper.compileVertexShader(vertexCode);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentCode);
		
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
		
		aTextureLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
		aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
		
		World.pMatrixLocation = GLES20.glGetUniformLocation(program, World.perspectiveMatrix);
		Camera.vMatrixLocation = GLES20.glGetUniformLocation(program, Camera.viewMatrix);
		uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);
	}
}
