package com.darksenic.dse2.data;

import com.darksenic.dse2.util.Matrix4x4;

public class Camera {
	public static String viewMatrix = "viewMatrix";
	public static Matrix4x4 vMatrix = Matrix4x4.IdentityM();
	public static int vMatrixLocation;
	
	public static void move(float x, float y){
		vMatrix.translate(-x, -y, 0);
	}
}
