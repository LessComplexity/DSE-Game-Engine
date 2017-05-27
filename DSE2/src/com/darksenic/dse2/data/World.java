package com.darksenic.dse2.data;

import com.darksenic.dse2.util.Matrix4x4;

public class World {
	// Matrix data
	public static Matrix4x4 pMatrix = Matrix4x4.IdentityM();
	public static int pMatrixLocation;
	public static String perspectiveMatrix = "perspectiveMatrix";
	public static String modelMatrix = "modelMatrix";
	
	// Stride and sizes
	public static int width, height;
	public static final int BYTES_PER_FLOAT = 4;
	public static int POSITION_COMPONENT_COUNT = 4;
	public static int COLOR_COMPONENT_COUNT = 3;
	public static int TEXTURE_COORD_COUNT = 2;
	public static int STRIDE_COLOR = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
	public static int STRIDE_TEXTURE = (POSITION_COMPONENT_COUNT + TEXTURE_COORD_COUNT) * BYTES_PER_FLOAT;
}
