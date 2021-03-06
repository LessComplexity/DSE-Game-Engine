package com.darksenic.dse2.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureHelper {
	private static final String TAG = "TextureHelper";
	private static Context context;
	
	public static void setContext(Context c){
		context = c;
	}
	
	public static int loadTexture(int resourceId) {
		final int[] textureObjectIds = new int[1];
		GLES20.glGenTextures(1, textureObjectIds, 0);
		
		if(textureObjectIds[0] == 0){
			if(LoggerConfig.ON) Log.w(TAG, "Couldn't generate a new OpenGL texture object");
			return 0;
		}
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		
		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
		
		if(bitmap == null) {
			if(LoggerConfig.ON) Log.w(TAG, "Resource ID " + resourceId + " couldn't be decoded");
			GLES20.glDeleteTextures(1, textureObjectIds, 0);
			return 0;
		}
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
		
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		
		bitmap.recycle();
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		
		return textureObjectIds[0];
	}
}
