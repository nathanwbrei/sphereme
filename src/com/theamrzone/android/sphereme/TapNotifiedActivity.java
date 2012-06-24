package com.theamrzone.android.sphereme;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

/**
 * Extend this if you want some tap action! Fires (one) listener on "tap" and on "tap and hold" 
 * @author Katherine
 */
abstract class TapNotifiedActivity extends Activity implements TapListener {

	private SensorManager manager;
	private Sensor sensor;
	private TapNotifier tapNotifier;
	
	/**
	 * Use this instead of onCreate(Bundle) of Activity 
	 * @param savedInstanceState
	 * @param view
	 */
	public void onCreate(Bundle savedInstanceState, View view) {
		super.onCreate(savedInstanceState);
		tapNotifier = new TapNotifier(this); 
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		view.setOnTouchListener(tapNotifier);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		manager.unregisterListener(tapNotifier);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		manager.registerListener(tapNotifier, sensor, 
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onTap(float x, float y) {}

	@Override
	public void onTapHold(float x, float y) {}
	
	@Override
	public void onTapHoldMoving(float x, float y) {}

	@Override
	public void onTapHoldRelease(float x, float y) {}
}