package com.theamrzone.android.sphereme;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SpheremeActivity extends TapAndSensingActivity {
	
	private TextView hello;
	
	private TextBasedWorldView worldView;
	private NoteDatabaseHelper dbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// DB
        dbHelper = NoteDatabaseHelper.getInstance(this);
        
        // saving a new message thingy
        saveNewMessageFromEditor();
        
    	// view
        worldView = new TextBasedWorldView(this);
        generateNoteViews();
        setContentView(worldView);
	    
	    // doing the tapping and the sensing thing
        super.onCreate(savedInstanceState, worldView);
    }
    
    private void generateNoteViews() {
    	List<AbstractNote> notes = dbHelper.getNotes();
    	
    	double[] rs = {1,1,1};  // does nothing for now
    	double[] ts = {1,2,3};	// discrete buckets 1..30
    	double[] zs = {5,5,7};	// discrete buckets 0..10
    	
    	for (int i=0;i< notes.size();i++){
    		AbstractNote note = notes.get(i);
    		note.setRTZ(rs[i], ts[i], zs[i]); 
    		NoteView noteView = new NoteView(this, note);
    		
    		worldView.addNoteView(noteView);
    	}
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
		worldView.theta = info.VisualColumn;
		worldView.requestLayout();
	}
	
	@Override
	public void onTap(float x, float y) {
		// TODO: Figure out what was tapped
		// TODO: Go to editor mode
	}
}