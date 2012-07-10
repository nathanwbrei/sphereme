package com.theamrzone.android.sphereme.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.model.NoteType;

public class FakeImageNoteView extends ImageNoteView {

	private AbstractNote note;
	
	public FakeImageNoteView(Context context, AbstractNote note) {
		super(context, note);
		this.note = note;
		
		Bitmap b = note.getBitmapContent();
	    setImageBitmap(b);
	    setId(100 + note.getId()); 
	}
	
	public AbstractNote getNote() {
		return note;
	}
	
	public NoteType getType() {
		return note.getType();
	}
}