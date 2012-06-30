package com.theamrzone.android.sphereme.view;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.theamrzone.android.sphereme.model.WorldModel;

/**
 * Shows last note per visual column
 */
public class WorldViewOneNote extends WorldView {
	
	private HashMap<Integer, TextNoteView> textNotes;
	private HashMap<Integer, ImageNoteView> imageNotes;
	
	public WorldViewOneNote(Context context, WorldModel worldModel){
		super(context, worldModel);
		
		textNotes = new HashMap<Integer, TextNoteView>();
		imageNotes = new HashMap<Integer, ImageNoteView>();
	}
	
	private RelativeLayout.LayoutParams generateNoteViewParams(int screenIndex) {
		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		return params;
	}
	
	@Override
	public void addNoteView(TextNoteView nv) {
		textNotes.put((int) nv.getNote().getT(), nv);
		RelativeLayout.LayoutParams params = generateNoteViewParams(nv.getId());
		addView(nv, params);
		nv.setVisibility(View.GONE);
	}
	
	@Override
	public void addNoteView(ImageNoteView nv) {
		imageNotes.put((int) nv.getNote().getT(), nv);
		RelativeLayout.LayoutParams params = generateNoteViewParams(nv.getId());
		addView(nv, params);
		nv.setVisibility(View.GONE);
	}
	
	@Override
	protected void drawNotes() {
		// disappear all notes
		for (TextNoteView nv : textNotes.values()) {
			nv.setVisibility(View.GONE);
		}
		for (ImageNoteView nv : imageNotes.values()) {
			nv.setVisibility(View.GONE);
		}
		
		// get the possible relavent ones
		TextNoteView textView = textNotes.get((int) model.getDisplayVisualColumn());
		ImageNoteView imageView = imageNotes.get((int) model.getDisplayVisualColumn());
		
		// if both exist, display the one with the higher ID
		if (textView != null && imageView != null) {
			if (textView.getId() > imageView.getId()) {
				textView.setVisibility(View.VISIBLE);
			} else {
				imageView.setVisibility(View.VISIBLE);
			}
		
		// only the text exists, so show that one
		} else if (textView != null) {
			textView.setVisibility(View.VISIBLE);
		
		// only the image view exists, so show that one
		} else if (imageView != null) {
			imageView.setVisibility(View.VISIBLE);
		}
	}
}