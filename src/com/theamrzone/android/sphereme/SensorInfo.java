package com.theamrzone.android.sphereme;

import android.app.Activity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.content.Context;
import android.content.ContextWrapper;

//SensorInfo is an object which stores all of the raw sensor information
//as well as other relevant calculated information in publicly accessible variables.
// Note to Nate: find good functions, perhaps, here: http://developer.android.com/reference/android/hardware/SensorManager.html
//    to use the functions on that page you may need to use the SensorManager created here, named mSensorManager
public class SensorInfo extends ContextWrapper implements SensorEventListener {

	//// List of RAW sensor data:
	// Acceleration Sensor -- Use the "Azimuth" button on the "Accel test" app to get a feel for these three values
	public float acc_azimuth;
	public float acc_pitch;
	public float acc_roll;
	// Gyroscope - All values are in radians/second and measure the rate of rotation around the device's local X, Y and Z axis
	public float gyro_x;
	public float gyro_y;
	public float gyro_z;
	// Magnetism - All values are in micro-Tesla (uT) and measure the ambient magnetic field in the X, Y and Z axis
	public float mag_x;
	public float mag_y;
	public float mag_z;
	
	
	// List of calculated sensor data:
	public int VisualColumn;  // This is the estimated "bucket" or "column" in visible space.  It divides the vertical ring, most cleanly
								// when z in our radial plane (phi) is 0.  Based upon theta, r does not affect it.
							  // Possible values range from 1 (Front) to numVC.  In the case of in the case of 30 buckets, 360 / 30 = 12.
								// 6 degrees to the right of center and 6 degrees to the left lies "1", the front.
							  //From 6 degrees to the right to 18 degrees to the right is "2", 18 to 30 degrees "3". From left 18 degrees
								// to left 6 degrees is bucket "30".
	private int numVC = 30; // Change this to increase or decrease number of VCs (columns, buckets).  More means can fit more icons in
										// physical space but more jumpiness / calibration / noise filtering required.
	  
	public SensorInfo(Context base) {
		super(base);
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	    mField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    
	}
	
	public SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mField;
    private float[] mGravity;
    private float[] mMagnetic;
    private float[] mGyros;
    
    /* protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    } */
    
    private void updateDirection() {
        float[] R = new float[9];
        float[] I = new float[9];
        //Load rotation matrix into R
        SensorManager.getRotationMatrix(R, I, mGravity, mMagnetic);
        //Return the orientation values
        float[] values = new float[3];
        SensorManager.getOrientation(R, values);
        acc_azimuth = values[0];
        acc_pitch = values[1];
        acc_roll = values[2];
        //Convert to degrees
        for (int i=0; i < values.length; i++) {
            Double degrees = (values[i] * 180) / Math.PI;
            values[i] = degrees.floatValue();
        }
        //Determine the VisualColumn in the visual plane to which it belongs
        VisualColumn = (getVCFromDegrees(values[0]) );
        //Display the raw values
//        valueView.setText(String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f",
//                values[0], values[1], values[2]));
    }

    private double[] drev(double low, double up) {
    	// if either exceed 180, switch them
    	double[] range = new double[2];
    	if (low > 180) {low = low - 360;}
    	if (up > 180) {up = up - 360;}
    	// if both are negative, need to switch them 
    	if (up < 0 && low < 0) { range = new double[] {up, low}; }
    	else { range = new double[] {low, up};}
    	return range;
    }
    
    // getVCFromDegrees takes in the degrees measurement of theta and returns an integer representing the number bucket. 0 means an error.
    private int getVCFromDegrees(float degrees) {
    	double bsize = 360 / (double) numVC;
    	for (int i = 1; i <= numVC; i++){
    		double lower = (-bsize / 2) + (i - 1)*bsize;
    		double upper = lower + bsize;
    		double[] check = drev(lower, upper);
    		lower = check[0];
    		upper = check[1];
    		if(degrees >= lower && degrees < upper) {return i; }
    	}
    	
        /*
         * Note: code inspired by the below.
         * See "South" for insight into why the backmost column will not trigger
         * with above code and would require a special case
        if(degrees >= -22.5 && degrees < 22.5) { return "N"; }
        if(degrees >= 22.5 && degrees < 67.5) { return "NE"; }
        if(degrees >= 67.5 && degrees < 112.5) { return "E"; }
        if(degrees >= 112.5 && degrees < 157.5) { return "SE"; }
        if(degrees >= 157.5 || degrees < -157.5) { return "S"; }
        if(degrees >= -157.5 && degrees < -112.5) { return "SW"; }
        if(degrees >= -112.5 && degrees < -67.5) { return "W"; }
        if(degrees >= -67.5 && degrees < -22.5) { return "NW"; } */
        
        return 0;
    }
    
    /* protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    } */

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
    	// update the vars etc
        switch(event.sensor.getType()) {
        case Sensor.TYPE_ACCELEROMETER:
            mGravity = event.values.clone();
            break;
        case Sensor.TYPE_MAGNETIC_FIELD:
            mMagnetic = event.values.clone();
            mag_x = event.values[0];
        	mag_y = event.values[1];
        	mag_z = event.values[2];
            break;
        case Sensor.TYPE_GYROSCOPE:
        	mGyros = event.values.clone();
        	gyro_x = event.values[0];
        	gyro_y = event.values[1];
        	gyro_z = event.values[2];
        default:
            return;
        }
        
        if(mGravity != null && mMagnetic != null) {
            updateDirection();
        }
    }
}
