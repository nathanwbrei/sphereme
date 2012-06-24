package com.theamrzone.android.sphereme;

import com.samsung.spensdk.SCanvasView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PenActivity extends Activity  {
    public final static String NEW_IMAGE = "com.theamrzone.android.sphereme.IMAGE";
    
	private SCanvasView msCanvas;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pencanvas);
		
		msCanvas = (SCanvasView) findViewById(R.id.canvas_view);
	}
	
	public void exitAndCreateNewPenNote(View view) {
		Intent intent = new Intent(this, SpheremeActivity.class);
	    Bitmap b= msCanvas.getCanvasBitmap(true);
	    intent.putExtra(NEW_IMAGE, b);
	    startActivity(intent);
	}
	
}