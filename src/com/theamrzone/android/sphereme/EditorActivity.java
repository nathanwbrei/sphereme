package com.theamrzone.android.sphereme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class EditorActivity extends Activity  {
    public final static String EXTRA_MESSAGE = "com.theamrzone.android.sphereme.MESSAGE";
    
	private EditText editBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor);
		
		editBox = (EditText) findViewById(R.id.edit_box);
	}
	
	public void sendMessage() {
//		Intent intent = new Intent(this, DisplayMessageActivity.class);
//	    String message = editBox.getText().toString();
//	    intent.putExtra(EXTRA_MESSAGE, message);
//	    startActivity(intent);
		
		// TODO: save message
		
		// TODO: go back to world view
	}
}