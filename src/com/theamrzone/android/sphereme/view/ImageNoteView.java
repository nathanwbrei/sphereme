package com.theamrzone.android.sphereme.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.widget.ImageView;

import com.theamrzone.android.sphereme.model.AbstractNote;
import com.theamrzone.android.sphereme.model.Note;

public class ImageNoteView extends ImageView implements INoteView {

	private AbstractNote note;
	
	public ImageNoteView(Context context, AbstractNote note) {
		super(context);
		this.note = note;
		
		if (Note.IMAGE.equals(note.getType())) {
	    	Bitmap b = Note.binaryToBitmap(note.getContent());
//	        b = Bitmap.createScaledBitmap(b, 700, 200, true);
//			setBackgroundResource(R.drawable.note);
//			setBackgroundColor(0xffffffff);
//	    	setScaleType(ImageView.ScaleType.CENTER);
	        ColorFilter filter = new LightingColorFilter(Color.WHITE, Color.WHITE);
	        setColorFilter(filter);
	        setImageBitmap(b);
			setId(1 + note.getId()); // make sure that it is 1-indexed instead of 0-indexed
		}
	}
	
	public AbstractNote getNote() {
		return note;
	}
	
	public String getType() {
		return note.getType();
	}
}