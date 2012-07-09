package com.theamrzone.android.sphereme.activity;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.samsung.spensdk.SCanvasView;
import com.theamrzone.android.sphereme.R;

public class ImageEditor extends Activity  {
    
	private SCanvasView canvas;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pencanvas);
		
		canvas = (SCanvasView) findViewById(R.id.canvas_view);
	}
	
	public void exitAndCreateNewPenNote(View view) {
		Intent intent = new Intent(this, Main.class);
		Bitmap bitmap  = canvas.getCanvasBitmap(false);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteStream);
		intent.putExtra(Main.NOTE_IMAGE, byteStream.toByteArray());
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
}