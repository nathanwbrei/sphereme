package com.theamrzone.android.sphereme;

import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.model.Note;
import com.theamrzone.android.sphereme.view.INoteView;
import com.theamrzone.android.sphereme.view.ImageNoteView;
import com.theamrzone.android.sphereme.view.TextNoteView;

import android.content.Context;

public class NoteView {
	public static INoteView createNoteView(Context context, AbstractNote note) {
		if (note.getType() == Note.STRING) {
			return new TextNoteView(context, note);
		} else if (note.getType() == Note.IMAGE) {
			return new ImageNoteView(context, note);
		}
		return null;
	}
}
