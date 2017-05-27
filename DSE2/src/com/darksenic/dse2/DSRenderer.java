package com.darksenic.dse2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.darksenic.dse2.data.Camera;
import com.darksenic.dse2.data.Screen;
import com.darksenic.dse2.data.World;
import com.darksenic.dse2.game.MainScreen;
import com.darksenic.dse2.util.ColorShader;
import com.darksenic.dse2.util.Matrix4x4;
import com.darksenic.dse2.util.TextureHelper;
import com.darksenic.dse2.util.TextureShader;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class DSRenderer implements Renderer {
	private Context context;
	static Screen screen;
	
	public DSRenderer(Context context){
		this.context = context;
		TextureHelper.setContext(context);
		screen = new MainScreen(context);
	}
	
	public static void setSCreen(Screen s){
		screen = s;
		screen.onCreate();
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(1, 0, 0, 0);
		
		ColorShader.loadProgram(context);
		TextureShader.loadProgram(context);
		screen.onCreate();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		World.width = width;
		World.height = height;
		screen.onChange();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		screen.onRun();
	}
	
	public void handleTouchPress(float x, float y){
		float[] vec = new float[] {x, y, 0f, 1f};
		vec = Matrix4x4.DeviceToNormalizedMatrix(World.width, World.height).MultiplyVec(vec);
		//vec = World.pMatrix.MultiplyVec(Camera.vMatrix.MultiplyVec(vec));
		screen.objectsTouchListener(vec[0], vec[1]);
	}
	
	public void handleTouchDrag(float x, float y){
		float[] vec = new float[] {x, y, 0f, 1f};
		vec = Matrix4x4.DeviceToNormalizedMatrix(World.width, World.height).MultiplyVec(vec);
		//vec = World.pMatrix.MultiplyVec(Camera.vMatrix.MultiplyVec(vec));
		screen.objectsDragListener(vec[0], vec[1]);
	}
}
