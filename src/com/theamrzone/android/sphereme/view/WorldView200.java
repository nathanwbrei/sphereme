package com.theamrzone.android.sphereme.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.theamrzone.android.sphereme.model.WorldModel;

/**
 * 200 refers to the height of the notes
 */
public class WorldView200 extends WorldView {
	
	private ArrayList<TextNoteView> textNotes;
	private ArrayList<ImageNoteView> imageNotes;
	
	public WorldView200(Context context, WorldModel worldModel){
		super(context, worldModel);
		
		textNotes = new ArrayList<TextNoteView>();
		imageNotes = new ArrayList<ImageNoteView>();
	}
	
	private RelativeLayout.LayoutParams generateTextNoteViewParams(int screenIndex) {
		int height = 200;
		int margin = 10;
		
		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
		params.topMargin = margin+(screenIndex-1)*(height+margin);  // 10 * (int) nv.getNote().getZ(); // 100+(i+1)*30; // zs[i]*maxY/5;
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		return params;
	}
	
	@Override
	public void addNoteView(TextNoteView nv) {
		textNotes.add(nv);
		RelativeLayout.LayoutParams params = generateTextNoteViewParams(nv.getId());
		addView(nv, params);
	}
	
	private RelativeLayout.LayoutParams generateImageNoteViewParams(int screenIndex) {
		int height = 200;
		int margin = 10;
		
		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
		params.topMargin = margin+(screenIndex-4)*(height+margin);  // 10 * (int) nv.getNote().getZ(); // 100+(i+1)*30; // zs[i]*maxY/5;
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		Log.d("WorldView", "" + params.topMargin);
		return params;
	}
	
	@Override
	public void addNoteView(ImageNoteView nv) {
		imageNotes.add(nv);
		
		RelativeLayout.LayoutParams params = generateImageNoteViewParams(nv.getId());
		addView(nv, params);
	}
	
	@Override
	protected void drawNotes() {
		drawTextNotes();
		drawImageNotes();
	}
	
	protected void drawTextNotes() {		
		// Now we draw all of the strings which are in our bucket
		for (int i=0; i<textNotes.size(); i++){
			TextNoteView noteView = textNotes.get(i);
			
			//if our test value matches our discretized azimuth
			if (noteView.getNote().getT() == (int) model.getDisplayVisualColumn()){  
				noteView.setVisibility(View.VISIBLE);
			} else {
				noteView.setVisibility(View.GONE);
			}
		}
	}
	
	protected void drawImageNotes() {		
		// Now we draw all of the strings which are in our bucket
		for (int i=0; i<imageNotes.size(); i++){
			ImageNoteView noteView = imageNotes.get(i);
			
			//if our test value matches our discretized azimuth
			if (noteView.getNote().getT() == (int) model.getDisplayVisualColumn()){  
				noteView.setVisibility(View.VISIBLE);
			} else {
				noteView.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	public INoteView getCurrentNoteView() { return null; }
}