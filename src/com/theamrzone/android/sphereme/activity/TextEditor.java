package com.theamrzone.android.sphereme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.theamrzone.android.sphereme.R;

public class TextEditor extends Activity  {
    
	private EditText editBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor);
		
		editBox = (EditText) findViewById(R.id.edit_box);
	}
	
	public void exitAndCreateNewNote(View view) {
		Intent intent = new Intent(this, Main.class);
	    String content = editBox.getText().toString();
	    intent.putExtra(Main.NOTE_TEXT, content);
	    startActivity(intent);
	}

	// -- MENU STUFF
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.return_to_world, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.options_return_to_world:
			intent = new Intent(this, Main.class);
			break;
		}
		
		if (intent != null) {
			startActivity(intent);
		}
		
		return false;
	}
}