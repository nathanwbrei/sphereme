package com.theamrzone.android.sphereme;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class NoteViewOnClickListener implements OnClickListener {
	
	private Context context;
	
	public NoteViewOnClickListener(Context context) {
		this.context = context;
	}
	
	@Override
	public void onClick(View v) {
		NoteView nv = (NoteView) v;
		
		int id = nv.getNote().getId();
		String content = nv.getNote().getStringContent();
		
		Intent intent = new Intent(context, UpdateActivity.class);
		intent.putExtra(UpdateActivity.NOTE_ID, id);
		intent.putExtra(UpdateActivity.NOTE_CONTENT, content);
		
		context.startActivity(intent);
	}
}
