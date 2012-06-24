package com.theamrzone.android.sphereme;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SpheremeActivity extends TapAndSensingActivity {
	
	private TextView hello;
	
	private TextBasedWorldView worldView;
	private NoteDatabaseHelper dbHelper;
	
	private String newNoteContent;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// DB
        dbHelper = NoteDatabaseHelper.getInstance(this);
        dbHelper.flush();
        
    	// view
        worldView = new TextBasedWorldView(this);
        generateNoteViews();
        setContentView(worldView);
	    
	    // doing the tapping and the sensing thing
        super.onCreate(savedInstanceState, worldView);
        
        // saving a new message thingy
        saveNewMessageFromEditor();
    }
    
    private void generateNoteViews() {
    	List<AbstractNote> notes = dbHelper.getNotes();
    	
    	for (int i=0;i< notes.size();i++){
    		AbstractNote note = notes.get(i);
    		note.setRTZ(0, note.getT(), 3); 
    		NoteView noteView = new NoteView(this, note);
    		
    		worldView.addNoteView(noteView);
    	}
    }

	private void saveNewMessageFromEditor() {
		// Get the message from the Editor intent
        Intent intent = getIntent();
        newNoteContent = intent.getStringExtra(EditorActivity.NEW_NOTE);
	}
	
	private void saveNewNote(String newContent, double theta) {
		Log.d("SpheremeActivty", "Creatng new content: " + newContent);
		Note note = new Note(0, theta, 0, 0, 0, 0, 
				Note.STRING, 
				Note.stringToBitmap(newContent), // generate bitmap from string
				Note.stringToByte(newContent));  // generate byte content from string 
		note.save(dbHelper); 
		
		NoteView noteView = new NoteView(this, note);
		worldView.addNoteView(noteView);
	}
	
	@Override
	public void onSensorChanged(SensorInfo info) {
		
		worldView.theta = info.VisualColumn;
		
		if (newNoteContent != null) {
			saveNewNote(newNoteContent, info.VisualColumn);
			newNoteContent = null;
		}
		worldView.requestLayout();
	}
	
	@Override
	public void onTap(float x, float y) {
		// TODO: Figure out what was tapped
		// TODO: Go to editor mode
	}
}