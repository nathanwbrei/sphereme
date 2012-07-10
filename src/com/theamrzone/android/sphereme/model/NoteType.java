package com.theamrzone.android.sphereme.model;

public enum NoteType {
	STRING, IMAGE;
	
	public static NoteType stringToType(String type) {
		if (IMAGE.toString().equals(type)) { 
			return IMAGE;
		} else {
			return STRING;
		}
	}
}
