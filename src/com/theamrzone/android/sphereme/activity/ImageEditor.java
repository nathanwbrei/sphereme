package com.theamrzone.android.sphereme.activity;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.samsung.sdraw.PenSettingInfo;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;
import com.theamrzone.android.sphereme.R;
import com.theamrzone.android.sphereme.model.Note;
import com.theamrzone.android.sphereme.view.WorldView;

public class ImageEditor extends Activity implements SCanvasInitializeListener {
    
	private SCanvasView canvas;
	private int visualColumn;
	private Bitmap bitmap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pencanvas);
		
		canvas = (SCanvasView) findViewById(R.id.canvas_view);
		canvas.setSCanvasInitializeListener(this);
		bitmap = null;
		
		// Set background if you can
		Intent intent = getIntent();
		if (intent != null) {
			visualColumn = intent.getIntExtra(Main.NOTE_VISUAL_COLUMN, 0);
			Drawable gradient = WorldView.getGradient(visualColumn);
			canvas.setBackgroundDrawable(gradient);
			
	    	String fileName = intent.getStringExtra(Main.NOTE_IMAGE_FILE);
	    	if (fileName != null) {
	    		bitmap = Note.getBitmapFromStorage(fileName);
	    		canvas.setClearImageBitmap(bitmap);
	    		Log.d("ImageEditor", "Bitmap shold be set.");
	    	}
		}

		PenSettingInfo penInfo = new PenSettingInfo();
		penInfo.setPenColor(Color.WHITE);
		canvas.setPenSettingInfo(penInfo);
	}
	
	public void exitAndCreateNewPenNote(View view) {
		Intent intent = new Intent(this, Main.class);
		Bitmap bitmap  = canvas.getCanvasBitmap(true);
		Log.d("ImageEditor", "I'm curious: " + (bitmap == this.bitmap));
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteStream);
		intent.putExtra(Main.NOTE_IMAGE, byteStream.toByteArray());
		
		if (visualColumn > 0) {
			intent.putExtra(Main.NOTE_VISUAL_COLUMN, visualColumn);
		}
		
		if (this.bitmap != null) {
			this.bitmap.recycle();
		}
		
		startActivity(intent);
	}

	// -- MENU STUFF
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.return_to_world, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.options_return_to_world:
			intent = new Intent(this, Main.class);
			break;
		}
		
		if (intent != null) {
			startActivity(intent);
		}
		
		return false;
	}

	@Override
	public void onInitialized() {
		if (bitmap != null) {
    		canvas.setClearImageBitmap(bitmap);
    		Log.d("ImageEditor", "Bitmap shold be set for reals.");			
		}
	}
}