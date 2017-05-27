package com.darksenic.dse2.data;
import android.opengl.GLES20;

import com.darksenic.dse2.util.ColorShader;
import com.darksenic.dse2.util.Listener;
import com.darksenic.dse2.util.Matrix4x4;
import com.darksenic.dse2.util.TextureHelper;
import com.darksenic.dse2.util.TextureShader;

public class DSEDrawable {
	private VertexArray vertexData;
	
	public Matrix4x4 mMatrix = Matrix4x4.IdentityM();
	public int mMatrixLocation;
	
	float x, y, width, height;
	float speed = 0.005f;
	float angle = 0;
	
	Listener l;
	
	private boolean isTextured;
	private int textureId;
	public boolean isListenerSet = false;
	
	public DSEDrawable(float x, float y, float width, float height, float r, float g, float b){
		float midx = x + (width/2);
		float midy = y + (height/2);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		float[] rectangle = new float[] {
				midx, midy, 0f, 1f, r, g, b,
				x, y, 0f, 1f, r, g, b,
				x + width, y, 0f, 1f, r, g, b,
				x + width, y + height, 0f, 1f, r, g, b,
				x, y + height, 0f, 1f, r, g, b,
				x, y, 0f, 1f, r, g, b
		};
		
		vertexData = new VertexArray(rectangle);
		isTextured = false;
	}
	
	public DSEDrawable(float x, float y, float width, float height, int resourceId){
		float midx = x + (width/2);
		float midy = y + (height/2);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		float[] rectangle = new float[] {
				midx, midy, 0f, 1f, 0.5f, 0.5f,
				x, y, 0f, 1f, 0f, 1f,
				x + width, y, 0f, 1f, 1f, 1f,
				x + width, y + height, 0f, 1f, 1f, 0f,
				x, y + height, 0f, 1f, 0f, 0f,
				x, y, 0f, 1f, 0f, 1f
		};
		
		vertexData = new VertexArray(rectangle);
		isTextured = true;
		textureId = TextureHelper.loadTexture(resourceId);
	}
	
	public void draw(){
		if(isTextured){
			TextureShader.useProgram();
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0); // Set the active texture unit to texture unit 0.
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			GLES20.glUniform1i(TextureShader.uTextureUnitLocation, 0); /* Tell the texture uniform sampler to use this texture in the shader by
																		* telling it to read from texture unit 0. */
		}
		else {
			ColorShader.useProgram();
		}
		GLES20.glUniformMatrix4fv(mMatrixLocation, 1, false, mMatrix.returnTranspose().toFloatArray(), 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
	}
	
	public void init(){
		if(isTextured){
			mMatrixLocation = GLES20.glGetUniformLocation(TextureShader.programID(), World.modelMatrix);
			vertexData.setVertexAttribPointer(0, TextureShader.aPositionLocation, World.POSITION_COMPONENT_COUNT, World.STRIDE_TEXTURE);
			vertexData.setVertexAttribPointer(World.POSITION_COMPONENT_COUNT, TextureShader.aTextureLocation, World.TEXTURE_COORD_COUNT, World.STRIDE_TEXTURE);
		}
		else {
			mMatrixLocation = GLES20.glGetUniformLocation(ColorShader.programID(), World.modelMatrix);
			vertexData.setVertexAttribPointer(0, ColorShader.aPositionLocation, World.POSITION_COMPONENT_COUNT, World.STRIDE_COLOR);
			vertexData.setVertexAttribPointer(World.POSITION_COMPONENT_COUNT, ColorShader.aColorLocation, World.COLOR_COMPONENT_COUNT, World.STRIDE_COLOR);
		}
	}
	
	public void addListener(Listener listener){
		l = listener;
		isListenerSet = true;
	}
	
	public void move(float x, float y){
		mMatrix.translate(x, y, 0);
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public boolean moveToPoint(float x, float y){
		float[] vertices = getVertices();
		float movex = 0, movey = 0;
		boolean hasArrived = true;

        // Get the middle of the rectangle:
        float[] vecMid = {(vertices[0] + vertices[4]) / 2, (vertices[1] + vertices[5]) / 2, 0f, 1f};
        
        if(vecMid[0] != x || vecMid[1] != y){
        	hasArrived = false;
        	float disx = Math.abs(vecMid[0] - x);
        	float disy = Math.abs(vecMid[1] - y);
        	
        	if(disx < speed){
        		if(vecMid[0] < x){
        			movex = disx;
        		} else {
        			movex = -disx;
        		}
        	}
        	else {
        		if(vecMid[0] < x){
        			movex = speed;
        		} else {
        			movex = -speed;
        		}
        	}
        	
        	if(disy < speed){
        		if(vecMid[1] < y){
        			movey = disy;
        		} else {
        			movey = -disy;
        		}
        	}
        	else {
        		if(vecMid[1] < y){
        			movey = speed;
        		} else {
        			movey = -speed;
        		}
        	}
        }
        
        if(!hasArrived) move(movex, movey);
        return hasArrived;
	}
	
	public void moveInstantly(float x, float y){
		float[] vertices = getVertices();
		float movex = 0, movey = 0;
		boolean hasArrived = true;

        // Get the middle of the rectangle:
        float[] vecMid = {(vertices[0] + vertices[4]) / 2, (vertices[1] + vertices[5]) / 2, 0f, 1f};
        
        if(vecMid[0] != x && vecMid[1] != y){
        	hasArrived = false;
        	float disx = Math.abs(vecMid[0] - x);
        	float disy = Math.abs(vecMid[1] - y);
        	
        	if(vecMid[0] < x){
        		movex = disx;
        	} else {
        		movex = -disx;
       		}
       	
       		if(vecMid[1] < y){
       			movey = disy;
       		} else {
       			movey = -disy;
       		}
        }
        
        if(!hasArrived) move(movex, movey);
	}
	
	public float[] getVertices(){
		// Vertices in object space:
        float[] vector1 = {x, y, 0f, 1f};
        float[] vector2 = {x + width, y, 0f, 1f};
        float[] vector3 = {x + width, y + height, 0f, 1f};
        float[] vector4 = {x, y, 0f, 1f};
        
        // Calculate the vertices in world space:
        float[] vector1r = mMatrix.MultiplyVec(vector1);
        float[] vector2r = mMatrix.MultiplyVec(vector2);
        float[] vector3r = mMatrix.MultiplyVec(vector3);
        float[] vector4r = mMatrix.MultiplyVec(vector4);
        
        return new float[] {
        		vector1r[0], vector1r[1], 
        		vector2r[0], vector2r[1], 
        		vector3r[0], vector3r[1], 
        		vector4r[0], vector4r[1]
        };
	}
	
	public void rotate(float angle){
		float[] vertices = getVertices();
		
		while(angle > 360) angle = angle - 360;
		
		this.angle = angle;
		
        // Get the middle of the rectangle:
        float[] vecMid = {(vertices[0] + vertices[4]) / 2, (vertices[1] + vertices[5]) / 2, 0f, 1f};
        
        moveInstantly(0, 0);
        mMatrix.rotate(angle, 0, 0, 1);
        moveInstantly(vecMid[0], vecMid[1]);
	}
	
	public void setTexture(int resourceId){
		if(isTextured){
			textureId = TextureHelper.loadTexture(resourceId);
		}
	}
	
	public Listener getListener(){
		return l;
	}
}
