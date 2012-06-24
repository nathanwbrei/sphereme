package com.theamrzone.android.sphereme;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class NoteView extends TextView {

	public NoteView(Context context) {
		super(context);
		
		setBackgroundResource(R.drawable.note);
		setTextColor(context.getResources().getColor(R.color.virtual_blue));
		setTypeface(Typeface.MONOSPACE);
	}

}
