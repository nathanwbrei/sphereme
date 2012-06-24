package com.theamrzone.android.sphereme;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SpheremeActivity extends Activity {
	
	private TextView hello;
	private NoteDatabaseHelper dbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        dbHelper = NoteDatabaseHelper.getInstance(this);
        
        hello = (TextView) findViewById(R.id.hello);
        
        saveNewMessageFromEditor();
        displayAllNotes();
    }
    
    private void displayAllNotes() {
    	List<AbstractNote> notes = dbHelper.getNotes();
    	
    	String toPrint = "Num: " + notes.size();
    	
    	for (AbstractNote note : notes) {
    		toPrint += "ID: " + note.getId() + " " + note.getContent().toString() + "\n";
    	}
    	
    	displayText(toPrint);
    }

	private void saveNewMessageFromEditor() {
		// Get the message from the Editor intent
        Intent intent = getIntent();
        String newContent = intent.getStringExtra(EditorActivity.NEW_NOTE);
        
        // save content if exists
        if (newContent != null) {
        	saveNewNote(newContent);
        	displayText(newContent);
        }
	}
	
	private void displayText(String text) {
		hello.setText(text);
	}
	
	private void saveNewNote(String newContent) {
		Note note = new Note(0, 0, 0, 0, 0, 0, 
				Note.STRING, 
				Note.stringToBitmap(newContent), // generate bitmap from string
				Note.stringToByte(newContent));  // generate byte content from string 
		note.save(dbHelper);
	}
}