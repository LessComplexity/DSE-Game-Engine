package com.darksenic.dse2.game;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.darksenic.dse2.R;
import com.darksenic.dse2.data.DSEDrawable;
import com.darksenic.dse2.data.Screen;
import com.darksenic.dse2.util.Listener;

public class MainScreen extends Screen {
	public MainScreen(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	DSEDrawable d = new DSEDrawable(-0.5f, -0.5f, 1f, 1f, R.drawable.ic_launcher);
	boolean hasArrived = false;
	float tox, toy;
	int numOfTimesMoved = 0;
	
	@Override
	public void onChange(){
		super.onChange();
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		addDrawable(d);
		addListener(new Listener() {
			
			@Override
			public void onTouchListener(float x, float y) {
				Log.v("Touch", "Has touched Object");
					hasArrived = false;
					tox = x;
					toy = y;
					numOfTimesMoved++;
			}
			
			@Override
			public void onDragListener(float x, float y) {
				tox = x;
				toy = y;
			}
		});
	}
	
	public void onRun(){
		super.onRun();
		d.moveInstantly(tox, toy);
	}
}
