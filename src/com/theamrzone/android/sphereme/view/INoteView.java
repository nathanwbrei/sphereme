package com.theamrzone.android.sphereme.view;

import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.model.NoteType;


public interface INoteView{

	public AbstractNote getNote();
	public NoteType getType();
	public int getId();
}
