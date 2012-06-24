package com.theamrzone.android.sphereme;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.RelativeLayout;

public class TextBasedWorldView extends RelativeLayout {

	
	// Dummy variable to get values to update whenever you touch the screen
	// Needs to be replaced with live data, and a call to invalidate()
	public float theta = 0;
		
	// Bounding box for screen. See onSizeChanged()
	public int maxX = 0;
	public int maxY = 0;
		
	public float maxZ = 10;  // bucket our Z as well
	
	private ArrayList<NoteView> noteViews;
	private GradientDrawable gradient;

	
	public TextBasedWorldView(Context context){
		super(context);
		
		noteViews = new ArrayList<NoteView>();
	}
	
	public void addNoteView(NoteView nv) {
		noteViews.add(nv);

		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(150, 150);
		params.leftMargin = 50 * (int) nv.getNote().getT(); // 100+(i+1)*30; //maxX/5;
		params.topMargin = 10 * (int) nv.getNote().getZ(); // 100+(i+1)*30; // zs[i]*maxY/5;
		
		addView(nv, params);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
		// Responsive gradient background. Is the gradient object even mutable?
		// HSV wants: h=[0,360); s=[0,1]; v=[0,1]
		float[] hsv = {theta*12, 0.7f, 0.5f};
		
		int[] gradcolors = {Color.HSVToColor(hsv), Color.BLACK};
        gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradcolors);
        gradient.setBounds(0,0,maxX, maxY);
        this.setBackgroundDrawable(gradient);
		
		// Now we draw all of the strings which are in our bucket
		for (int i=0; i<noteViews.size(); i++){
			NoteView noteView = noteViews.get(i);
			
			if (noteView.getNote().getT() == (int) theta){  //if our test value matches our discretized azimuth
				noteView.setVisibility(View.VISIBLE);
			} else {
				noteView.setVisibility(View.GONE);
			}
		}
		// Create an infinite draw loop by requesting another onDraw()
		super.onLayout(changed, l, t, r, b);
		invalidate();
	}
	
	// Keep track of the bounding box for the entire screen
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		maxX = w;
		maxY = h;
	}
}