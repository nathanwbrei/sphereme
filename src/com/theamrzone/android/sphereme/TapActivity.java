package com.theamrzone.android.sphereme;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TapActivity extends Activity implements SensorEventListener, View.OnTouchListener {

	TextView textEvent, textTime, textX, textY, 
			 textMaxX, textMaxY, textMaxZ, 
			 textMaxAccel, textAction;

	private SensorManager manager;
	private Sensor sensor;	
	long startHoldTime;
	long holdTime;
	boolean isDown;
	float maxAccelX, maxAccelY, maxAccelZ, maxAccel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tap);
		LinearLayout layout = (LinearLayout) findViewById(R.id.tap);
		
		textEvent = (TextView) findViewById(R.id.event);
		textTime = (TextView) findViewById(R.id.time);
		textX = (TextView) findViewById(R.id.x);
		textY = (TextView) findViewById(R.id.y);
		textMaxX = (TextView) findViewById(R.id.max_x);
		textMaxY = (TextView) findViewById(R.id.max_y);
		textMaxZ = (TextView) findViewById(R.id.max_z);
		textMaxAccel = (TextView) findViewById(R.id.max_accel);
		textAction = (TextView) findViewById(R.id.action);
		
		startHoldTime = 0; holdTime = 0;
		isDown = false;
		maxAccelX = 0; 
		maxAccelY = 0;
		maxAccelZ = 0;
		
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		
		layout.setOnTouchListener(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		manager.unregisterListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		manager.registerListener(this, sensor, 
				SensorManager.SENSOR_DELAY_UI);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		textX.setText("x: " + String.valueOf(event.getX()));
		textY.setText("y: " + String.valueOf(event.getY()));
		
		int action = event.getAction();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			startDown();
			textEvent.setText("ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			textEvent.setText("ACTION_MOVE");
			break;
		case MotionEvent.ACTION_UP:
			endDown();
			textEvent.setText("ACTION_UP");
			break;
		case MotionEvent.ACTION_CANCEL:
			textEvent.setText("ACTION_CANCEL");
			break;
		default:
			textEvent.setText("Unknown!");
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
		textTime.setText("Time down: " + holdTime);
		updateAction();
	}
	
	private void updateAction() {
		if (isDown) { return; } // too premature to determine what's goine on
		
		if (maxAccel > 8) {
			textAction.setText("Action: Move");
		} else if (holdTime < 250 && maxAccel < 4) {
			textAction.setText("Action: Tap");
		} else if (holdTime >= 250 && maxAccel >= 4) {
			textAction.setText("Action: Move");
		} else if (holdTime < 150) {
			textAction.setText("Action: Tap");
		} else {
			textAction.setText("Action: Unknown");
		}
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	public void onSensorChanged(SensorEvent event) {
		if (isDown) {
			float accelX = event.values[SensorManager.DATA_X];
			float accelY = event.values[SensorManager.DATA_Y];
			float accelZ = event.values[SensorManager.DATA_Z];
			float totalAccel = (float) Math.sqrt(accelX*accelX + accelY*accelY + accelZ*accelZ);
			
			maxAccelX = (Math.abs(accelX) > Math.abs(maxAccelX)) ? accelX : maxAccelX;
			maxAccelY = (Math.abs(accelY) > Math.abs(maxAccelY)) ? accelY : maxAccelY;
			maxAccelZ = (Math.abs(accelZ) > Math.abs(maxAccelZ)) ? accelZ : maxAccelZ;
			maxAccel  = (Math.abs(totalAccel) > Math.abs(maxAccel)) ? totalAccel : maxAccel;
			
			textMaxX.setText("max x accel: " + maxAccelX);
			textMaxY.setText("max y accel: " + maxAccelY);
			textMaxZ.setText("max z accel: " + maxAccelZ);
			textMaxAccel.setText("max accel: " + maxAccel);
		}
	}
}