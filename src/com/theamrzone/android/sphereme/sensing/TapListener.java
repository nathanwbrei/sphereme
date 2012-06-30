package com.theamrzone.android.sphereme.sensing;

/**
 * Listens for tap data.  The input to functions is where the user taps on the screen.
 * @author Katherine
 */
public interface TapListener {
	
	/**
	 * Called on short tap.
	 */
	public void onTap(float x, float y);
	
	/**
	 * Called when the tap has been held for 250.
	 */
	public void onTapHold(float x, float y);

	/**
	 * Called when the tap has been held for >= 250 and is moving
	 */
	public void onTapHoldMoving(float x, float y);
	
	/**
	 * Called on tap and moved.
	 */
	public void onTapHoldRelease(float x, float y);
}
