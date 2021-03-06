package com.theamrzone.android.sphereme;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.samsung.spensdk.SCanvasView;
import com.theamrzone.android.sphereme.test.TestDisplaySPenActivity;

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
		Intent intent = new Intent(this, TestDisplaySPenActivity.class);
		Bitmap bitmap  = msCanvas.getCanvasBitmap(false);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteStream);
		intent.putExtra(NEW_IMAGE, byteStream.toByteArray());
		startActivity(intent);
	}	
}