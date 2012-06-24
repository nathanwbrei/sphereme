package com.theamrzone.android.sphereme;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class TextBasedWorldView extends RelativeLayout {

	public String[] notes = {"This is bucket 1!", "And this is bucket 2", "Now bucket 3"};
	public int[] rs = {1,1,1};  // does nothing for now
	public int[] ts = {1,2,3};	// discrete buckets 1..30
	public int[] zs = {5,5,7};	// discrete buckets 0..10
	
	// Dummy variable to get values to update whenever you touch the screen
	// Needs to be replaced with live data, and a call to invalidate()
	public float theta = 0;
		
	// Bounding box for screen. See onSizeChanged()
	public int maxX = 0;
	public int maxY = 0;
		
	public float maxZ = 10;  // bucket our Z as well
	
	private ArrayList<NoteView> noteViews;
	
	public TextBasedWorldView(Context context){
		super(context);
		
		noteViews = new ArrayList<NoteView>();
		generateNoteViews();
	}
	
	private void generateNoteViews() { 
		for (int i=0;i<3;i++){
			NoteView note = new NoteView(getContext());
			note.setText(notes[i]);
				
			RelativeLayout.LayoutParams params;
			params = new RelativeLayout.LayoutParams(150, 150);
			params.leftMargin = 100+(i+1)*30; //maxX/5;
			params.topMargin = 100+(i+1)*30; // zs[i]*maxY/5;
			
			noteViews.add(note);
			addView(note, params);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		noteViews.get(0).setText("Changed layout: " + System.currentTimeMillis() + " " + theta);
		
		// Now we draw all of the strings which are in our bucket
		for (int i=0;i<3;i++){
			NoteView note = noteViews.get(i);
			
			if (ts[i] == (int) theta){  //if our test value matches our discretized azimuth
				note.setVisibility(View.VISIBLE);
			} else {
				note.setVisibility(View.GONE);
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