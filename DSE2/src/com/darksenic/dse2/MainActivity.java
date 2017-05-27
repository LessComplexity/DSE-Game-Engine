package com.darksenic.dse2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
	private GLSurfaceView glView;
	private boolean rendererSet = false;
	DSRenderer renderer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		renderer = new DSRenderer(this);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		glView = new GLSurfaceView(this);
		
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
				&& (Build.FINGERPRINT.startsWith("generic")
				|| Build.FINGERPRINT.startsWith("unknown")
				|| Build.MODEL.contains("google_sdk")
				|| Build.MODEL.contains("Emulator")
				|| Build.MODEL.contains("Android SDK built for x86")));
		
		if(supportsEs2){
			glView.setEGLContextClientVersion(2);
			glView.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
			glView.setRenderer(renderer);
			rendererSet = true;
		}
		
		else {
			Toast.makeText(this, "You can't run this app on your device > OpenGL ES version not supported.", Toast.LENGTH_LONG).show();
			return;
		}
		
		glView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event != null){
					final float x = event.getX();
					final float y = event.getY();
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						glView.queueEvent(new Runnable() {
							
							@Override
							public void run() {
								renderer.handleTouchPress(x, y);
							}
						});
					}
					else if (event.getAction() == MotionEvent.ACTION_MOVE){
						glView.queueEvent(new Runnable() {
							
							@Override
							public void run() {
								renderer.handleTouchDrag(x, y);
							}
						});
					}
					return true;
				}
				else {
					return false;
				}
			}
		});
		
		setContentView(glView);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		if(rendererSet) glView.onPause();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(rendererSet) glView.onResume();
	}
}
