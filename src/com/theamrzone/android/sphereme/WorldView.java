package com.theamrzone.android.sphereme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;


public class WorldView extends View{
	
	// Test data. Replace this with real data.
	// Each theta bucket is given one note, so this should be really darn easy
	// We don't have bounding boxes, so we insert \n's into our strings every some-number of chars

	public String[] notes = {"This is bucket 1!", "And this is bucket 2", "Now bucket 3"};
	public int[] rs = {1,1,1};  // does nothing for now
	public int[] ts = {1,2,3};	// discrete buckets 1..30
	public int[] zs = {5,5,7};	// discrete buckets 0..10
		
	// Dummy variable to get values to update whenever you touch the screen
	// Needs to be replaced with live data, and a call to invalidate()
	public float t = 0;
	
	// Bounding box for screen. See onSizeChanged()
	public int maxX = 0;
	public int maxY = 0;
	
	public float maxZ = 10;  // bucket our Z as well

	private Paint paint;
	private GradientDrawable gradient;
	
	public WorldView(Context context){
		super(context);
		paint = new Paint();
        paint.setColor(0xffffffff);
        paint.setTypeface(Typeface.MONOSPACE);
		paint.setTextSize(22);
	}
	

	// This is where the magic happens. Make this its own thread, if it becomes too slow.
	protected void onDraw (Canvas canvas){
		
		// Dummy r,t,z data. Poll something to get the attitude 
		float r=1f;
		t+=.01; //(float) dummy;		// in [1..30]
		float z=1.5f;
		
		// Responsive gradient background. Is the gradient object even mutable?
		// HSV wants: h=[0,360); s=[0,1]; v=[0,1]
		float[] hsv = {t, 0.7f, (float)(z/maxZ)};
		
		int[] gradcolors = {Color.HSVToColor(hsv), Color.BLACK};
        gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradcolors);
        gradient.setBounds(0,0,maxX, maxY);        		
		gradient.draw(canvas);

		// Now we draw all of the strings which are in our bucket
		for (int i=0;i<3;i++){
			if (ts[i] == (int) t){  //if our test value matches our discretized azimuth
				// print to screen
				
				canvas.drawText(notes[i], maxX/5, zs[i]*maxX/10, paint);
			}
		}
		// Create an infinite draw loop by requesting another onDraw()
		invalidate();
	}	
	
	// Keep track of the bounding box for the entire screen
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		maxX = w;
		maxY = h;
	}
}
