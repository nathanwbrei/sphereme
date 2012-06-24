package com.theamrzone.android.sphereme;

import android.os.Bundle;


public class TestTextBasedWorldViewActivity extends TapAndSensingActivity {

	private TextBasedWorldView worldView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		worldView = new TextBasedWorldView(this);
        super.onCreate(savedInstanceState, worldView);
		setContentView(worldView);
	}
	
	@Override
	public void onSensorChanged(SensorInfo info) {
		worldView.theta = info.VisualColumn;
		worldView.requestLayout();
	}
	
	@Override
	public void onTap(float x, float y) {
	}
}