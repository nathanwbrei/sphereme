package com.theamrzone.android.sphereme;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

/**
 * Extend this if you want some tap action! Fires (one) listener on "tap" and on "tap and hold" 
 * @author Katherine
 */
public abstract class TapAndSensingActivity extends SensingActivity implements TapListener {

	private Sensor linearAccelerationSensor;
	protected TapNotifier tapNotifier;
	
	/**
	 * Use this instead of onCreate(Bundle) of Activity 
	 * @param savedInstanceState
	 * @param view The view you want to listen for taps on
	 */
	public void onCreate(Bundle savedInstanceState, View view) {
		super.onCreate(savedInstanceState);
		tapNotifier = new TapNotifier(this);
		linearAccelerationSensor = sensorInfo.mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		view.setOnTouchListener(tapNotifier);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorInfo.mSensorManager.unregisterListener(tapNotifier);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sensorInfo.mSensorManager.registerListener(tapNotifier, linearAccelerationSensor, 
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