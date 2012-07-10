package com.theamrzone.android.sphereme.model;

public enum NoteType {
	STRING, IMAGE, FAKE;
	
	public static NoteType stringToType(String type) {
		if (IMAGE.toString().equals(type)) { 
			return IMAGE;
		} else if (STRING.toString().equals(type)){
			return STRING;
		} else {
			return FAKE;
		}
	}
}
