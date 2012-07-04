package com.theamrzone.android.sphereme;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;

public class NoteView extends TextView {

	private AbstractNote note;
	
	public NoteView(Context context, AbstractNote note) throws Exception {
		super(context);
		this.note = note;
		
		if (note.isStringContent()) {
			setText(note.getStringContent());
			setBackgroundResource(R.drawable.note);
			setTextColor(context.getResources().getColor(R.color.virtual_blue));
			setTypeface(Typeface.MONOSPACE);
		} else {
			BitmapDrawable bd = new BitmapDrawable(note.getBitmapContent());
			setBackgroundDrawable(bd);
		}
	}
	
	public AbstractNote getNote() {
		return note;
	}
}