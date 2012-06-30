package com.theamrzone.android.sphereme;

import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.view.TextNoteView;

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
		TextNoteView nv = (TextNoteView) v;
		
		int id = nv.getNote().getId();
		String content = AbstractNote.binaryToString(nv.getNote().getContent());
		
		Intent intent = new Intent(context, UpdateActivity.class);
		intent.putExtra(UpdateActivity.NOTE_ID, id);
		intent.putExtra(UpdateActivity.NOTE_CONTENT, content);
		
		context.startActivity(intent);
	}
}
