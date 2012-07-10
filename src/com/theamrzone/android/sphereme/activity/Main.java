package com.theamrzone.android.sphereme.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.theamrzone.android.sphereme.R;
import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.model.FakeNote;
import com.theamrzone.android.sphereme.model.Note;
import com.theamrzone.android.sphereme.model.NoteDatabaseHelper;
import com.theamrzone.android.sphereme.model.NoteType;
import com.theamrzone.android.sphereme.model.WorldModel;
import com.theamrzone.android.sphereme.model.WorldModel.State;
import com.theamrzone.android.sphereme.sensing.SensorInfo;
import com.theamrzone.android.sphereme.sensing.TapAndSensingActivity;
import com.theamrzone.android.sphereme.view.FakeImageNoteView;
import com.theamrzone.android.sphereme.view.INoteView;
import com.theamrzone.android.sphereme.view.ImageNoteView;
import com.theamrzone.android.sphereme.view.TextNoteView;
import com.theamrzone.android.sphereme.view.WorldView;
import com.theamrzone.android.sphereme.view.WorldViewOneNote;

public class Main extends TapAndSensingActivity {

	public final static String NOTE_VISUAL_COLUMN = "com.theamrzone.android.sphereme.VC";
    public final static String NOTE_ID = "com.theamrzone.android.sphereme.ID";
    public final static String NOTE_TEXT = "com.theamrzone.android.sphereme.TEXT";
    public final static String NOTE_IMAGE = "com.theamrzone.android.sphereme.IMAGE";
    public final static String NOTE_IMAGE_FILE = "com.theamrzone.android.sphereme.IMAGE_FILE";
    
    public final static int NUM_VISUAL_COLUMNS = 8;
    public final static int BAD_BIAS = -100;
    public static int visualColumnBias = BAD_BIAS;
    
    private TextView textView;
	private WorldModel model;
	private WorldView worldView;
	private NoteDatabaseHelper dbHelper;
	
	private Note noteToSave;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Trace logging
		Log.d("Main", "onCreate was called.");
		
	    // World Model
		model = new WorldModel();
	    worldView = new WorldViewOneNote(this, model);
	    
	    // Create view and tapping
	    setContentView(worldView);
	    super.onCreate(savedInstanceState, worldView);
		
	    // Database
	    dbHelper = new NoteDatabaseHelper(this);
	    noteToSave = null;
	    getNoteToSave();
	    
	    initializeNotes();
	}
	
	@Override
	public void onResume() {
		Log.d("Main", "onResume");
		super.onResume();
	}
	
	private void displayNote(AbstractNote note) {
		// if it's a text-based note
		if (note.getType() == NoteType.STRING) {
			TextNoteView noteView = new TextNoteView(this, note);
			worldView.addNoteView(noteView);
						
		// if it's an image based note
		} else if (note.getType() == NoteType.IMAGE) {
			ImageNoteView noteView = new ImageNoteView(this, note);
			worldView.addNoteView(noteView);
			
		// it's a fake note. Lol.
		} else if (note.getType() == NoteType.FAKE) {
			FakeImageNoteView noteView = new FakeImageNoteView(this, note);
			worldView.addNoteView(noteView);
		}
	}
	
	private void initializeNotes() {
		for (AbstractNote note : dbHelper.getNotes()) {
			displayNote(note);
		}
		
		// add a How-To note if there are no notes.
		if (dbHelper.getNotes().isEmpty()) {
			makeHowToNote();
		}
	}
	
	private void makeHowToNote() {
		// make note from bitmap
		Bitmap bitmap  = BitmapFactory.decodeResource(getResources(), R.drawable.how_to);
		AbstractNote note = new FakeNote(0, Main.NUM_VISUAL_COLUMNS, 0, 0, 0, 0, bitmap, bitmap);
		
		// save and display
		displayNote(note);
		Log.d("Main", "Display how to note");
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
    	int visualColumn = intent.getIntExtra(NOTE_VISUAL_COLUMN, 0);
    	
    	// TODO: if noteID == -1, then it's a note to update, not create
    	if (noteId == -1 && textContent != null && textContent.length() > 0) {
    		noteToSave = new Note(0, visualColumn, 0, 0, 0, 0, 
    				NoteType.STRING, 
    				Note.stringToBitmap(textContent), // generate bitmap from string
    				Note.stringToByte(textContent));  // generate byte content from string
    	} else if (noteId == -1 && imageContent != null && imageContent.length > 0) {
    		noteToSave = new Note(0, visualColumn, 0, 0, 0, 0,
    				NoteType.IMAGE,
    				Note.binaryToBitmap(imageContent),
    				imageContent);
    	}
	}
	
	/**
	 * Only call this once it's known what the actual model's VC is.
	 */
	private void saveNote() {
		if (noteToSave != null) {
			if (noteToSave.getType() == NoteType.STRING) {
				textView.setText("Note content to save: " + 
								  noteToSave.getStringContent());				
			}
			
			if (noteToSave.getT() == 0) {
				noteToSave.setRTZ(0, model.getActualVisualColumn(), 0);
			} else {
				model.setDisplayVisualColumn((int) noteToSave.getT());
			}

			noteToSave.save(dbHelper);
			displayNote(noteToSave);
			noteToSave = null;
		}
	}

	// -- MENU STUFF
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.options_new_text:
			intent = new Intent(this, TextEditor.class);
			intent.putExtra(NOTE_VISUAL_COLUMN, model.getDisplayVisualColumn());
			break;
		case R.id.options_new_image:
			intent = new Intent(this, ImageEditor.class);
			intent.putExtra(NOTE_VISUAL_COLUMN, model.getDisplayVisualColumn());
			break;
		case R.id.options_how_to:
			intent = new Intent(this, HowTo.class);
			intent.putExtra(NOTE_VISUAL_COLUMN, model.getDisplayVisualColumn());
			break;
		case R.id.options_about:
			intent = new Intent(this, About.class);
			intent.putExtra(NOTE_VISUAL_COLUMN, model.getDisplayVisualColumn());
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
		Log.d("Main", "paused.");
		super.onPause();
	}
	
	@Override
	public void onStop() {
		Log.d("Main", "Stopped.");
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		Log.d("Main", "destroyed!");
		dbHelper.close();
		dbHelper.flush();
		super.onDestroy();
	}
	
	// -- SENSOR LISTENER 
	@Override
	public void onSensorChanged(SensorInfo info) {
		boolean isDown = tapNotifier.isDown();
		
		// Here is the actual tap sensing state change
		if (tapNotifier.getCurrentHoldTime() > 100 && model.getState() == State.STATIONARY) {
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
	
	@Override
	public void onTap(float x, float y) {
		Intent intent = new Intent(this, ImageEditor.class);
		intent.putExtra(NOTE_VISUAL_COLUMN, model.getDisplayVisualColumn());
		
		INoteView noteView = worldView.getCurrentNoteView();
		Log.d("Main", "Current Note: " + (noteView != null));
		if (noteView != null && noteView.getNote().getType() == NoteType.IMAGE) {
			Log.d("Main", "Current Note Type: " + noteView.getNote().getType());
			intent.putExtra(NOTE_IMAGE_FILE, noteView.getNote().getStringContent());
		}
		
		startActivity(intent);
	}
}