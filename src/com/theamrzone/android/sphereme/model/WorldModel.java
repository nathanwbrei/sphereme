package com.theamrzone.android.sphereme.model;

import java.util.ArrayList;
import java.util.List;

public class WorldModel {

	public enum State {
		STATIONARY, MOVING
	}
	
	private State state;
	private int displayVisualColumn;
	private int actualVisualColumn;
	private List<WorldModelListener> listeners;
	
	public WorldModel() {
		state = State.STATIONARY;
		displayVisualColumn = 0;
		actualVisualColumn = 0;
		listeners = new ArrayList<WorldModelListener>();
	}
	
	// -- setters
	public void setState(State newState) {
		state = newState;
		fireListeners();
	}
	
	public void setVisualColumn(int newVC) {
		actualVisualColumn = newVC;
		
		if (state == State.MOVING || displayVisualColumn == 0) {
			displayVisualColumn = newVC;
		}
		
		fireListeners();
	}
	
	// -- getters
	public State getState() {
		return state;
	}
	
	public int getDisplayVisualColumn() {
		return displayVisualColumn;
	}
	
	public int getActualVisualColumn() {
		return actualVisualColumn;
	}
	
	// -- listeners
	public void addListener(WorldModelListener listener) {
		listeners.add(listener);
	}
	
	private void fireListeners() {
		for (WorldModelListener listener : listeners) {
			listener.onModelChanged(this);
		}
	}
}