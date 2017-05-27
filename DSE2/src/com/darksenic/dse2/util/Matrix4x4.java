package com.darksenic.dse2.util;

public class Matrix4x4 {
	float[][] matrix = new float[4][4];
	
	public Matrix4x4(float m[][]){
		matrix = m;
	}
	
	public void Multiply(float[][] m1){
		float[][] m2 = new float[4][4];
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				float value = 0f;
				for(int t = 0; t < 4; t++){
					value += matrix[i][t] * m1[t][j];
				}
				m2[i][j] = value;
			}
		}
		setMatrix(m2);
	}
	
	public void setMatrix(float[][] m){
		matrix = m;
	}
	
	public void Multiply(Matrix4x4 m){
		float[][] m1 = m.toFloatMat();
		float[][] m2 = new float[4][4];
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				float value = 0f;
				for(int t = 0; t < 4; t++){
					value += matrix[i][t] * m1[t][j];
				}
				m2[i][j] = value;
			}
		}
		setMatrix(m2);
	}
	
	public float[] MultiplyVec(float[] vec){
		float[] newV = new float[4];
		for(int i = 0; i < 4; i++){
			float value = 0f;
			for(int j = 0; j < 4; j++){
				value += matrix[i][j] * vec[j]; 
			}
			newV[i] = value;
		}
		
		return newV;
	}
	
	public void MultiplyScal(float scalar){
		for(int i = 0; i < 4; i++){	
			for(int j = 0; j < 4; j++){
				matrix[i][j] *= scalar;
			}
		}
	}
	
	public void Add(float[][] m1){
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				matrix[i][j] += m1[i][j];
			}
		}
	}
	
	public void Add(Matrix4x4 m){
		float[][] m1 = m.toFloatMat();
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				matrix[i][j] += m1[i][j];
			}
		}
	}
	
	public void translate(float x, float y, float z){
		matrix[0][3] += x;
		matrix[1][3] += y;
		matrix[2][3] += z;
	}
	
	public void scale(float x, float y, float z){
		matrix[0][0] = x;
		matrix[1][1] = y;
		matrix[2][2] = z;
	}
	
	public void rotate(float angle, float x, float y, float z){
		float cos = (float) (Math.cos(Math.toRadians(angle)));
		float sin = (float) (Math.sin(Math.toRadians(angle)));
		Matrix4x4 ret = IdentityM();
		ret.setValue(cos + (x * x * (1 - cos)), 0, 0);
		ret.setValue((x * y * (1 - cos)) - (z * sin), 0, 1);
		ret.setValue((x * z * (1 - cos)) + (y * sin), 0, 2);
		
		ret.setValue((x * y * (1 - cos)) + (z * sin), 1, 0);
		ret.setValue(cos + (y * y * (1 - cos)), 1, 1);
		ret.setValue((y * z * (1 - cos)) - (x * sin), 1, 2);
		
		ret.setValue((x * z * (1 - cos)) - (y * sin), 2, 0);
		ret.setValue((y * z * (1 - cos)) + (x * sin), 2, 1);
		ret.setValue(cos + (z * z * (1 - cos)), 2, 2);
		
		Multiply(ret);
	}
	
	public void rotateZ(float angle){
		float cos = (float) (Math.cos(Math.toRadians(angle)));
		float sin = (float) (Math.sin(Math.toRadians(angle)));
		Matrix4x4 ret = IdentityM();
		ret.setValue(cos, 0, 0);
		ret.setValue(-sin, 0, 1);
		
		ret.setValue(sin, 1, 0);
		ret.setValue(cos, 1, 1);
		
		Multiply(ret);
	}
	
	public static Matrix4x4 IdentityM(){
		return new Matrix4x4(new float[][]{
				{1f, 0f, 0f, 0f},
				{0f, 1f, 0f, 0f},
				{0f, 0f, 1f, 0f},
				{0f, 0f, 0f, 1f}
		});
	}
	
	public static Matrix4x4 ZeroM(){
		return new Matrix4x4(new float[][]{
				{0f, 0f, 0f, 0f},
				{0f, 0f, 0f, 0f},
				{0f, 0f, 0f, 0f},
				{0f, 0f, 0f, 0f}
		});
	}
	
	public static Matrix4x4 RTS(float[] rotate, float[] translate, float[] scale){
		Matrix4x4 ret = IdentityM();
		ret.translate(translate[0], translate[1], translate[2]);
		ret.scale(scale[0], scale[1], scale[2]);
		ret.rotate(rotate[0], rotate[1], rotate[2], rotate[3]);
		return ret;
	}
	
	public float getValue(int i, int j){
		return matrix[i][j];
	}
	
	public void setValue(float v, int i, int j){
		matrix[i][j] = v;
	}
	
	public void transpose(){
		float[][] ret = new float[4][4];
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				ret[i][j] = matrix[j][i];
			}
		}
		matrix = ret;
	}
	
	public Matrix4x4 returnTranspose(){
		float[][] ret = new float[4][4];
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				ret[i][j] = matrix[j][i];
			}
		}
		return new Matrix4x4(ret);
	}
	
	public float[] toFloatArray(){
		float[] ret = new float[16];
		int t = 0;
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				ret[t] = matrix[i][j];
				t++;
			}
		}
		
		return ret;
	}
	
	public float[][] toFloatMat(){
		return matrix;
	}
	
	public static Matrix4x4 DeviceToNormalizedMatrix(float width, float height){
		return new Matrix4x4(new float[][]{
				{2f/width, 0f, 0f, -1f},
				{0f, -2f/width, 0f, height/width},
				{0f, 0f, 0f, 0f},
				{0f, 0f, 0f, 1f}
		});
	}
}
