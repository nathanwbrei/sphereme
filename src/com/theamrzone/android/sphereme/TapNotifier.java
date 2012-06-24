package com.theamrzone.android.sphereme;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;

public class TapNotifier implements SensorEventListener, View.OnTouchListener {

	private long startHoldTime;
	private long holdTime;
	private boolean isDown;
	private float x, y;
	private float maxAccelX, maxAccelY, maxAccelZ, maxAccel;
	private TapListener listener;
	
	public TapNotifier(TapListener listener) {
		startHoldTime = 0; 
		holdTime = 0;
		isDown = false;
		maxAccelX = 0; 
		maxAccelY = 0;
		maxAccelZ = 0;
		this.listener = listener;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		

		x = event.getX();
		y = event.getY();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			startDown();
			break;
		case MotionEvent.ACTION_UP:
			endDown();
			break;
		}
		return true; 
	}
	
	private void startDown() {
		startHoldTime = System.currentTimeMillis();
		isDown = true;
		maxAccelX = 0;
		maxAccelY = 0;
		maxAccelZ = 0;
		maxAccel = 0;
	}
	
	private void endDown() {
		isDown = false;
		holdTime = System.currentTimeMillis() - startHoldTime;
		detectAction();
	}
	
	private void detectAction() {
		if (isDown) { return; } // too premature to determine what's goine on
		
		if (maxAccel > 8) {
			fireTapAndMove();
		} else if (holdTime < 250 && maxAccel < 4) {
			fireTap();
		} else if (holdTime >= 250 && maxAccel >= 4) {
			fireTapAndMove();
		} else if (holdTime < 150) {
			fireTap();
		}
	}
	
	private void fireTapAndMove() {
		listener.onTapAndMove(x, y);
	}
	
	private void fireTap() {
		listener.onTap(x, y);
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	public void onSensorChanged(SensorEvent event) {
		if (!isDown) { return; } // not interested in data if there is no tap
		
		float accelX = event.values[SensorManager.DATA_X];
		float accelY = event.values[SensorManager.DATA_Y];
		float accelZ = event.values[SensorManager.DATA_Z];
		float totalAccel = (float) Math.sqrt(accelX*accelX + accelY*accelY + accelZ*accelZ);
			
		maxAccelX = (Math.abs(accelX) > Math.abs(maxAccelX)) ? accelX : maxAccelX;
		maxAccelY = (Math.abs(accelY) > Math.abs(maxAccelY)) ? accelY : maxAccelY;
		maxAccelZ = (Math.abs(accelZ) > Math.abs(maxAccelZ)) ? accelZ : maxAccelZ;
		maxAccel  = (Math.abs(totalAccel) > Math.abs(maxAccel)) ? totalAccel : maxAccel;
	}
}