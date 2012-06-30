package com.theamrzone.android.sphereme.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.RelativeLayout;

import com.theamrzone.android.sphereme.activity.Main;
import com.theamrzone.android.sphereme.model.WorldModel;
import com.theamrzone.android.sphereme.model.WorldModelListener;

/**
 * Refers to the fact it will only keep one note per thing.
 */
public abstract class WorldView extends RelativeLayout implements WorldModelListener {

	// Bounding box for screen. See onSizeChanged()
	public int maxX = 0;
	public int maxY = 0;
	public float maxZ = 10;  // bucket our Z as well
	
	protected GradientDrawable gradient;
	
	protected WorldModel model;
	
	public WorldView(Context context, WorldModel worldModel){
		super(context);
		
		model = worldModel;
		model.addListener(this);
	}
	
	public abstract void addNoteView(TextNoteView nv);
	public abstract void addNoteView(ImageNoteView nv);
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		setBackgroundGradient();
		drawNotes();
        
		// Create an infinite draw loop by requesting another onDraw()
		super.onLayout(changed, l, t, r, b);
		invalidate();
	}
	
	protected abstract void drawNotes();
	
	protected void setBackgroundGradient() {
		// set to nothing while it's still thinking
		if (model.getDisplayVisualColumn() == 0) {
			setBackgroundColor(Color.BLACK);
			return;
		}
		
		// Responsive gradient background. Is the gradient object even mutable?
		// HSV wants: h=[0,360); s=[0,1]; v=[0,1]
		float hue = model.getDisplayVisualColumn() * 360.0f/Main.NUM_VISUAL_COLUMNS ;
		float[] hsv = {hue, 0.7f, 0.5f};
				
		int[] gradcolors = {Color.HSVToColor(hsv), Color.BLACK};
		gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradcolors);
		gradient.setBounds(0,0,maxX, maxY);
		this.setBackgroundDrawable(gradient);
	}
	
	// Keep track of the bounding box for the entire screen
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		maxX = w;
		maxY = h;
	}
	
	// -- World model listener
	@Override
	public void onModelChanged(WorldModel model) {
		requestLayout();
	}
}