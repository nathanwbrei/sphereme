package com.theamrzone.android.sphereme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UpdateActivity extends Activity  {
    public final static String NOTE_ID = "com.theamrzone.android.sphereme.NOTE_ID";
    public final static String NOTE_CONTENT = "com.theamrzone.android.sphereme.NOTE_CONTENT";
    
	private EditText updateBox;
	
	private int id;
	private String content;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updater);
		
		Intent intent = getIntent();
		id = intent.getIntExtra(NOTE_ID, -1);
		content = intent.getStringExtra(NOTE_CONTENT);
		
		updateBox = (EditText) findViewById(R.id.update_box);
		updateBox.setText(content);
	}
	
	public void exitAndUpdateNote(View view) {
		Intent intent = new Intent(this, SpheremeActivity.class);
	    content = updateBox.getText().toString();
	    intent.putExtra(NOTE_ID, id);
	    intent.putExtra(NOTE_CONTENT, content);
	    startActivity(intent);
	}
}