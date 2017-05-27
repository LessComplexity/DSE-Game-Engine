package com.darksenic.dse2.util;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
	private static final String TAG = "ShaderHelper";
	
	public static int compileVertexShader(String shaderCode){
		return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
	}
	
	public static int compileFragmentShader(String shaderCode){
		return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
	}

	private static int compileShader(int type, String shaderCode) {
		final int shaderObjectId = GLES20.glCreateShader(type);
		
		if(shaderObjectId == 0){
			if(LoggerConfig.ON) Log.w(TAG, "Couldn't create new shader");
			
			return 0;
		}
		
		GLES20.glShaderSource(shaderObjectId, shaderCode);
		GLES20.glCompileShader(shaderObjectId);
		
		final int[] compileStatus = new int[1];
		GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		
		if(LoggerConfig.ON) Log.v(TAG, "Result of compiling source: " + "\n" + shaderCode + "\n:" + GLES20.glGetShaderInfoLog(shaderObjectId));
		
		if(compileStatus[0] == 0){
			GLES20.glDeleteShader(shaderObjectId);
			if(LoggerConfig.ON) Log.w(TAG, "Compilation of shader failed!");
			
			return 0;
		}
		
		
		return shaderObjectId;
	}
	
	public static int linkProgram(int vertex, int fragment){
		final int programObjectId = GLES20.glCreateProgram();
		
		if(programObjectId == 0){
			if(LoggerConfig.ON) Log.w(TAG, "Couldn't create prgram");
			return 0;
		}
		
		GLES20.glAttachShader(programObjectId, vertex);
		GLES20.glAttachShader(programObjectId, fragment);
		
		GLES20.glLinkProgram(programObjectId);
		
		final int[] programLinkStatus = new int[1];
		GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, programLinkStatus, 0);
		
		if(LoggerConfig.ON) Log.v(TAG, "Result of linking program:\n" + GLES20.glGetProgramInfoLog(programObjectId));
		
		if(programLinkStatus[0] == 0){
			if(LoggerConfig.ON) Log.w(TAG, "Couldn't link program!");
			GLES20.glDeleteProgram(programObjectId);
		}
		
		return programObjectId;
	}
	
	public static boolean validateProgram(int programId){
		GLES20.glValidateProgram(programId);
		final int[] validateStatus = new int[1];
		GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
		if(LoggerConfig.ON) Log.v(TAG, "Result of validating program: " + validateStatus[0] + "\nLog: " + GLES20.glGetProgramInfoLog(programId));
		
		return validateStatus[0] != 0;
	}
}
