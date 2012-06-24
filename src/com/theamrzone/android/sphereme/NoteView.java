package com.theamrzone.android.sphereme;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class NoteView extends TextView {

	private Note note;
	
	public NoteView(Context context, Note note) {
		super(context);
		this.note = note;
		
		setText(AbstractNote.binaryToString(note.getContent()));
		setBackgroundResource(R.drawable.note);
		setTextColor(context.getResources().getColor(R.color.virtual_blue));
		setTypeface(Typeface.MONOSPACE);
	}
	
	public Note getNote() {
		return note;
	}
}