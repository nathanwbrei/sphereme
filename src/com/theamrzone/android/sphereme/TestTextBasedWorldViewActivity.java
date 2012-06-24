package com.theamrzone.android.sphereme;

import android.os.Bundle;
import android.widget.RelativeLayout;


public class TestTextBasedWorldViewActivity extends TapAndSensingActivity {

	private TextBasedWorldView worldView;
	private NoteView note;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		note = new NoteView(this);
		note.setText("Hi?");
		
		worldView = new TextBasedWorldView(this, note);
		
		RelativeLayout.LayoutParams params;
		params = new RelativeLayout.LayoutParams(150, 150);
		params.leftMargin = 50; //maxX/5;
		params.topMargin = 60; // zs[i]*maxY/5;
		worldView.addView(note, params);
		
        super.onCreate(savedInstanceState, worldView);
        
		setContentView(worldView);
	}
	
	@Override
	public void onSensorChanged(SensorInfo info) {
		worldView.theta = info.VisualColumn;
		note.setText("VC: " + (int) worldView.theta);
		worldView.requestLayout();
	}
	
	@Override
	public void onTap(float x, float y) {
		worldView.removeView(note);
	}
}