package com.darksenic.dse2.data;

import java.util.LinkedList;

import android.content.Context;
import android.database.ContentObservable;
import android.opengl.GLES20;

import com.darksenic.dse2.DSRenderer;
import com.darksenic.dse2.util.Listener;

abstract public class Screen {
	Listener listener;
	LinkedList<DSEDrawable> objects = new LinkedList<DSEDrawable>();
	float width, height;
	boolean onCreated = false;
	public boolean isListenerSet = false;
	public Context context;
	
	public Screen(Context context){
		this.context = context;
	}
	
	public void onCreate() {
		onCreated = true;
	}
	
	public void onRun(){
		GLES20.glUniformMatrix4fv(World.pMatrixLocation, 1, false, World.pMatrix.toFloatArray(), 0);
		GLES20.glUniformMatrix4fv(Camera.vMatrixLocation, 1, false, Camera.vMatrix.toFloatArray(), 0);
		
		for(DSEDrawable o: objects){
			o.draw();
		}
	}
	
	public void onChange(){
		World.pMatrix.setValue((float) World.width / (float) World.height, 1, 1);
	}
	
	public void goToNextScreen(Screen screen){
		DSRenderer.setSCreen(screen);
	}
	
	public void addDrawable(DSEDrawable n){
		if(onCreated){
			n.init();
		}
		objects.add(n);
	}
	
	/**
	 * TODO: Need to modify for better performance
	 */
	public boolean checkCollision(DSEDrawable a, DSEDrawable b){
		float x1 = a.getVertices()[0], y1 = a.getVertices()[1], x2 = b.getVertices()[0], y2 = b.getVertices()[1];
		if((x1 < x2 + b.width && x1 > x2 && y1 < y2 + b.height && y1 > y2)
				|| (x1 + a.width < x2 + b.width && x1 + a.width > x2 && y1 < y2 + b.height && y1 > y2)
				|| (x1 < x2 + b.width && x1 > x2 && y1 + a.height < y2 + b.height && y1 + a.height > y2)
				|| (x1 + a.width < x2 + b.width && x1 + a.width > x2 && y1 + a.height < y2 + b.height && y1 + a.height > y2)){
			return true;
		}
		else if((x2 < x1 + a.width && x2 > x1 && y2 < y1 + a.height && y2 > y1)
				|| (x2 + b.width < x1 + a.width && x2 + b.width > x1 && y2 < y1 + a.height && y2 > y1)
				|| (x2 < x1 + a.width && x2 > x1 && y2 + b.height < y1 + a.height && y2 + b.height > y1)
				|| (x2 + b.width < x1 + a.width && x2 + b.width > x1 && y2 + b.height < y1 + a.height && y2 + b.height > y1)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * TODO: Need to modify for better performance
	 */
	public boolean checkPointCollision(DSEDrawable a, float x, float y){
		float x1 = a.getVertices()[0], y1 = a.getVertices()[1];
		if(x < x1 + a.width && x > x1 && y < y1 + a.height && y > y1){
			return true;
		}
		return false;
	}
	
	public void addListener(Listener l){
		listener = l;
		isListenerSet = true;
	}
	
	public Listener getListener(){
		return listener;
	}
	
	public void objectsTouchListener(float x, float y){
		if(isListenerSet) listener.onTouchListener(x, y);
		
		for(DSEDrawable o: objects){
			if(checkPointCollision(o, x, y)){
				if(o.isListenerSet) o.getListener().onTouchListener(x, y);
			}
		}
	}
	
	public void objectsDragListener(float x, float y){
		if(isListenerSet) listener.onDragListener(x, y);
		
		for(DSEDrawable o: objects){
			if(checkPointCollision(o, x, y)){
				if(o.isListenerSet) o.getListener().onDragListener(x, y);
			}
		}
	}
}
