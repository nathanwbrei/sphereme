package com.theamrzone.android.sphereme;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.theamrzone.android.sphereme.activity.Main;
import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.model.Note;
import com.theamrzone.android.sphereme.model.NoteDatabaseHelper;
import com.theamrzone.android.sphereme.model.NoteType;
import com.theamrzone.android.sphereme.sensing.SensorInfo;
import com.theamrzone.android.sphereme.sensing.TapAndSensingActivity;
import com.theamrzone.android.sphereme.view.INoteView;
import com.theamrzone.android.sphereme.view.TextNoteView;
import com.theamrzone.android.sphereme.view.WorldView200;

public class SpheremeActivity extends TapAndSensingActivity {
	
	private TextView hello;
	
	private WorldView200 worldView;
	private NoteDatabaseHelper dbHelper;
	
	private String newNoteContent;
	private byte[] newImageContent;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// DB
        dbHelper = new NoteDatabaseHelper(this);
        
    	// view
        // OworldView = new WorldView(this);
        generateNoteViews();
        setContentView(worldView);
	    
	    // doing the tapping and the sensing thing
        super.onCreate(savedInstanceState, worldView);
        Log.d("NOTE", "we are here");
        // saving a new message thingy
        saveNewMessageFromEditor();
        saveNewImageFromEditor();
    }
    
    private void generateNoteViews() {
    	List<AbstractNote> notes = dbHelper.getNotes();
    	
    	for (int i=0;i< notes.size();i++){
    		AbstractNote note = notes.get(i);
    		note.setRTZ(0, note.getT(), 3); 

    		try
    		{
    			INoteView noteView = NoteView.createNoteView(this, note);
//    			worldView.addNoteView(noteView);
    		}
    		catch (Exception e)
    		{
    			Log.e("Error with note "+note.getId(), e.toString());
    		}
    	}
    }

	private void saveNewMessageFromEditor() {
		// Get the message from the Editor intent
        Intent intent = getIntent();
        newNoteContent = intent.getStringExtra(Main.NOTE_TEXT);
	}
	
    private void saveNewImageFromEditor() {
    	Intent intent = getIntent();
    	newImageContent = intent.getByteArrayExtra(PenActivity.NEW_IMAGE);
    }
	
	private void saveNewNote(String newContent, double theta) throws Exception {
		Log.d("SpheremeActivty", "Creatng new content: " + newContent);
		Note note = new Note(0, theta, 0, 0, 0, 0, 
				NoteType.STRING, 
				Note.stringToBitmap(newContent), // generate bitmap from string
				Note.stringToByte(newContent));  // generate byte content from string 
		note.save(dbHelper); 
		
		TextNoteView noteView = new TextNoteView(this, note);
		worldView.addNoteView(noteView);
	}
	
	private void saveNewNote(byte[] newContent, double theta) throws Exception {
		Log.d("SpheremeActivity", "creating new bitmap content");
		Note note = new Note(0, theta, 0, 0, 0, 0,
				NoteType.IMAGE,
				Note.binaryToBitmap(newContent),
				newContent);
		note.save(dbHelper);
		
		TextNoteView noteView = new TextNoteView(this, note);
		worldView.addNoteView(noteView);
	}
	
	@Override
	public void onSensorChanged(SensorInfo info) {
		int vc = info.VisualColumn;
		vc = 1;
		Log.d("lol", "are we being called");
		if (tapNotifier.isDown()) {
			// worldView.theta = vc;
		}
		
		if (newNoteContent != null) {
			if (newNoteContent.length() > 0) {
				try {
					saveNewNote(newNoteContent, vc);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			newNoteContent = null;
		}
		
		if (newImageContent != null) {
			try {
				saveNewNote(newImageContent, vc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			newImageContent = null;
		}
		
		worldView.requestLayout();
	}
	
	@Override
	public void onTap(float x, float y) {
		// TODO: Figure out what was tapped
		// TODO: Go to editor mode
	}
	
	@Override
	public void onStop() {
		Log.d("ThoughtPod", "Stopped");
	}
	
	@Override
	public void onDestroy() {
		Log.d("ThoughtPod", "Destroyed.");
	}
}