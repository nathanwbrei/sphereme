package com.theamrzone.android.sphereme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.theamrzone.android.sphereme.R;
import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.model.Note;
import com.theamrzone.android.sphereme.model.NoteDatabaseHelper;
import com.theamrzone.android.sphereme.model.WorldModel;
import com.theamrzone.android.sphereme.model.WorldModel.State;
import com.theamrzone.android.sphereme.sensing.SensorInfo;
import com.theamrzone.android.sphereme.sensing.TapAndSensingActivity;
import com.theamrzone.android.sphereme.view.ImageNoteView;
import com.theamrzone.android.sphereme.view.TextNoteView;
import com.theamrzone.android.sphereme.view.WorldView;
import com.theamrzone.android.sphereme.view.WorldViewOneNote;

public class Main extends TapAndSensingActivity {

    public final static String NOTE_ID = "com.theamrzone.android.sphereme.ID";
    public final static String NOTE_TEXT = "com.theamrzone.android.sphereme.TEXT";
    public final static String NOTE_IMAGE = "com.theamrzone.android.sphereme.IMAGE";
    public final static int NUM_VISUAL_COLUMNS = 4;
	
    private TextView textView;
	private WorldModel model;
	private WorldView worldView;
	private NoteDatabaseHelper dbHelper;
	
	private Note noteToSave;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Create the text view
	    textView = new TextView(this);
	    textView.setText("THIS IS SOME TEXT");
	    
	    // World Model
	    model = new WorldModel();
	    worldView = new WorldViewOneNote(this, model);
	    
	    // Create view and tapping
	    setContentView(worldView);
	    super.onCreate(savedInstanceState, worldView);
		
	    // Database
	    dbHelper = NoteDatabaseHelper.getInstance(this);
	    noteToSave = null;
	    getNoteToSave();
	    
	    initializeNotes();
	}
	
	private void displayNote(AbstractNote note) {
		// if it's a text-based note
		if (Note.STRING.equals(note.getType())) {
			TextNoteView noteView = new TextNoteView(this, note);
			worldView.addNoteView(noteView);
						
		// if it's an image based note
		} else {
			ImageNoteView noteView = new ImageNoteView(this, note);
			worldView.addNoteView(noteView);
		}
	}
	
	private void initializeNotes() {
		for (AbstractNote note : dbHelper.getNotes()) {
			displayNote(note);
		}
	}
	
	/**
	 * It is unknown as to whether the model's VC, so we wait till we know for sure to save, 
	 * but we can build the rest of it first.
	 */
	private void getNoteToSave() {
    	Intent intent = getIntent();
    	int noteId = intent.getIntExtra(Main.NOTE_ID, -1);
    	String textContent = intent.getStringExtra(Main.NOTE_TEXT);
    	byte[] imageContent = intent.getByteArrayExtra(Main.NOTE_IMAGE);
    	
    	// TODO: if noteID == -1, then it's a note to update, not create
    	if (noteId == -1 && textContent != null && textContent.length() > 0) {
    		noteToSave = new Note(0, 0, 0, 0, 0, 0, 
    				Note.STRING, 
    				Note.stringToBitmap(textContent), // generate bitmap from string
    				Note.stringToByte(textContent));  // generate byte content from string
    	} else if (noteId == -1 && imageContent != null && imageContent.length > 0) {
    		noteToSave = new Note(0, 0, 0, 0, 0, 0,
    				Note.IMAGE,
    				Note.binaryToBitmap(imageContent),
    				imageContent);
    	}
	}
	
	/**
	 * Only call this once it's known what the actual model's VC is.
	 */
	private void saveNote() {
		if (noteToSave != null) {
			if (Note.STRING.equals(noteToSave.getType())) {
				textView.setText("Note content to save: " + 
								  Note.binaryToString(noteToSave.getContent()));				
			}
			
			noteToSave.setRTZ(0, model.getActualVisualColumn(), 0);
			noteToSave.save(dbHelper);
			displayNote(noteToSave);
			noteToSave = null;
		}
	}

	// -- MENU STUFF
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_new_thought, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.options_new_text:
			intent = new Intent(this, TextEditor.class);
			break;
		case R.id.options_new_image:
			intent = new Intent(this, ImageEditor.class);
			break;
		}
		
		if (intent != null) {
			startActivity(intent);
		}
		
		return false;
	}
	
	// -- DESTRUCTION
	@Override
	public void onPause() {
		Log.d("Main", "paused!");
		super.onPause();
	}
	@Override
	public void onDestroy() {
		dbHelper.close();
		super.onDestroy();
	}
	
	// -- SENSOR LISTENER 
	@Override
	public void onSensorChanged(SensorInfo info) {
		boolean isDown = tapNotifier.isDown();
		
		// Here is the actual tap sensing state change
		if (isDown && model.getState() == State.STATIONARY) {
			model.setState(State.MOVING);
		} else if (!isDown && model.getState() == State.MOVING) {
			model.setState(State.STATIONARY);
		}
		
		// here is the visual column state change
		model.setVisualColumn(info.VisualColumnNice);
		
		// we now know for certain that the model's vc is up to date, 
		// so we can go ahead and add a note
		saveNote();
	}
}