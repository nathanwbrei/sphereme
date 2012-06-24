package com.theamrzone.android.sphereme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditorActivity extends Activity  {
    public final static String NEW_NOTE = "com.theamrzone.android.sphereme.MESSAGE";
    
	private EditText editBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor);
		
		editBox = (EditText) findViewById(R.id.edit_box);
	}
	
	public void exitAndCreateNewNote(View view) {
		Intent intent = new Intent(this, SpheremeActivity.class);
	    String content = editBox.getText().toString();
	    intent.putExtra(NEW_NOTE, content);
	    startActivity(intent);
	}
}