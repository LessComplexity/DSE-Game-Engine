package com.darksenic.dse2.util;

import com.darksenic.dse2.R;
import com.darksenic.dse2.data.Camera;
import com.darksenic.dse2.data.World;

import android.content.Context;
import android.opengl.GLES20;

public class ColorShader {
	private static int program;
	public static String A_POSITION = "a_Position";
	public static int aPositionLocation;
	public static String A_COLOR = "a_Color";
	public static int aColorLocation;
	
	public static void useProgram(){
		GLES20.glUseProgram(program);
	}
	
	public static int programID(){
		return program;
	}
	
	public static void loadProgram(Context context){
		String vertexCode = ResourceLoader.readTextFromFile(context, R.raw.vertex);
		String fragmentCode = ResourceLoader.readTextFromFile(context, R.raw.fragment);
		
		int vertexShader = ShaderHelper.compileVertexShader(vertexCode);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentCode);
		
		program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
		
		aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
		aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
		
		World.pMatrixLocation = GLES20.glGetUniformLocation(program, World.perspectiveMatrix);
		Camera.vMatrixLocation = GLES20.glGetUniformLocation(program, Camera.viewMatrix);
	}
}
