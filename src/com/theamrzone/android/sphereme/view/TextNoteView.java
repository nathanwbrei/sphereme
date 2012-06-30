package com.theamrzone.android.sphereme.view;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.model.Note;

public class TextNoteView extends TextView implements INoteView {

	private AbstractNote note;
	
	public TextNoteView(Context context, AbstractNote note) {
		super(context);
		this.note = note;
		
		if (Note.STRING.equals(note.getType())) {
//			setBackgroundResource(R.drawable.note);
//			setTextColor(context.getResources().getColor(R.color.virtual_blue));
			setPadding(30, 30, 30, 30);
			setTypeface(Typeface.MONOSPACE);
			setTextSize(20);
			setId(1 + note.getId()); // make sure that it is 1-indexed instead of 0-indexed
			setText(Note.binaryToString(note.getContent()));
		}
	}
	
	public AbstractNote getNote() {
		return note;
	}
	
	public String getType() {
		return note.getType();
	}
}