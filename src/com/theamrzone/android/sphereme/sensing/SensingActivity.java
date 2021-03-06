package com.theamrzone.android.sphereme.sensing;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;

public abstract class SensingActivity extends Activity implements SensorListener {

	protected SensorInfo sensorInfo;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensorInfo = new SensorInfo(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorInfo.mSensorManager.unregisterListener(sensorInfo);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sensorInfo.mSensorManager.registerListener(sensorInfo, 
				sensorInfo.mAccelerometer, SensorManager.SENSOR_DELAY_UI);
		sensorInfo.mSensorManager.registerListener(sensorInfo, 
				sensorInfo.mField, SensorManager.SENSOR_DELAY_UI);
		sensorInfo.mSensorManager.registerListener(sensorInfo, 
				sensorInfo.mGyroscope, SensorManager.SENSOR_DELAY_UI);
	}

	
	@Override
	public void onSensorChanged(SensorInfo info) {}
}