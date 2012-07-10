package com.theamrzone.android.sphereme;

import android.content.Context;

import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.model.NoteType;
import com.theamrzone.android.sphereme.view.INoteView;
import com.theamrzone.android.sphereme.view.ImageNoteView;
import com.theamrzone.android.sphereme.view.TextNoteView;

public class NoteView {
	public static INoteView createNoteView(Context context, AbstractNote note) {
		if (note.getType() == NoteType.STRING) {
			return new TextNoteView(context, note);
		} else if (note.getType() == NoteType.IMAGE) {
			return new ImageNoteView(context, note);
		}
		return null;
	}
}
