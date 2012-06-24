package com.theamrzone.android.sphereme;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class NoteView extends TextView {

	private AbstractNote note;
	
	public NoteView(Context context, AbstractNote note) {
		super(context);
		this.note = note;
		
		setText(AbstractNote.binaryToString(note.getContent()));
		setBackgroundResource(R.drawable.note);
		setTextColor(context.getResources().getColor(R.color.virtual_blue));
		setTypeface(Typeface.MONOSPACE);
	}
	
	public AbstractNote getNote() {
		return note;
	}
}