package com.theamrzone.android.sphereme;

import android.content.Intent;
import android.os.Bundle;

public class SpheremeActivity extends TapAndSensingActivity {
	
	private WorldView worldView;
	private NoteDatabaseHelper dbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// view
        worldView = new WorldView(this);
	    setContentView(worldView);
	    
	    // doing the tapping and the sensing thing
        super.onCreate(savedInstanceState, worldView);
        
        // DB
        dbHelper = NoteDatabaseHelper.getInstance(this);
        
        // saving a new message thingy
        saveNewMessageFromEditor();
        
        // trying to display all notes but it doesn't work yet.
        displayAllNotes();
    }
    
    private void displayAllNotes() {
    	dbHelper.getNotes();
    	// TODO
    }

	private void saveNewMessageFromEditor() {
		// Get the message from the Editor intent
        Intent intent = getIntent();
        String newContent = intent.getStringExtra(EditorActivity.NEW_NOTE);
        
        // save content if exists
        if (newContent != null) {
        	saveNewNote(newContent);
        }
        
        // TODO generate new image for the note probably
	}
	
	private void saveNewNote(String newContent) {
		Note note = new Note(0, 0, 0, 0, 0, 0, 
				Note.STRING, 
				Note.stringToBitmap(newContent), // generate bitmap from string
				Note.stringToByte(newContent));  // generate byte content from string 
		note.save(dbHelper);
	}
	
	@Override
	public void onSensorChanged(SensorInfo info) {
		worldView.t = info.VisualColumn;
		worldView.invalidate();
	}
	
	@Override
	public void onTap(float x, float y) {
		// TODO: Figure out what was tapped
		// TODO: Go to editor mode
	}
}