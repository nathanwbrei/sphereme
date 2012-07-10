package com.theamrzone.android.sphereme.activity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.samsung.sdraw.PenSettingInfo;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.HistoryUpdateListener;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;
import com.theamrzone.android.sphereme.R;
import com.theamrzone.android.sphereme.model.Note;
import com.theamrzone.android.sphereme.view.WorldView;

public class ImageEditor extends Activity implements SCanvasInitializeListener, OnClickListener, HistoryUpdateListener {
    
	private LinearLayout layout;
	private SCanvasView canvas;
	private Button penButton;
	private Button eraseButton;
	private Button undoButton;
	private Button redoButton;
	
	private int visualColumn;
	private Bitmap bitmap;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pencanvas);
		
		// initialize everything
		layout = (LinearLayout) findViewById(R.id.pencanvas_layout);
		bitmap = null;
		
		// initialize the canvas
		canvas = (SCanvasView) findViewById(R.id.canvas_view);
		canvas.setSCanvasInitializeListener(this);

		// more init
		HashMap<String,Integer> settingResourceMap = new HashMap<String, Integer>();
		// Locale
		settingResourceMap.put(SCanvasConstants.LOCALE_PEN_SETTING_TITLE, R.string.pen_settings);
		settingResourceMap.put(SCanvasConstants.LOCALE_ERASER_SETTING_TITLE, R.string.eraser_settings);
		settingResourceMap.put(SCanvasConstants.LOCALE_ERASER_SETTING_CLEARALL, R.string.clear_all);   
		settingResourceMap.put(SCanvasConstants.LOCALE_ERASER_SETTING_CLEARALL_MESSAGE, R.string.confirm_clear_all); 
		// Create Setting View
		boolean bClearAllVisibileInEraserSetting = true;
		RelativeLayout settingViewContainer = (RelativeLayout) findViewById(R.id.canvas_container);
		canvas.createSettingView(settingViewContainer, settingResourceMap, bClearAllVisibileInEraserSetting);
		
		// initialize the buttons
		penButton = (Button) findViewById(R.id.pen_button);
		eraseButton   = (Button) findViewById(R.id.erase_button);
		undoButton    = (Button) findViewById(R.id.undo_button);
		redoButton    = (Button) findViewById(R.id.redo_button);
		penButton.setOnClickListener(this);
		eraseButton.setOnClickListener(this);
		undoButton.setOnClickListener(this);
		redoButton.setOnClickListener(this);
	}
	
	public void exitAndCreateNewPenNote(View view) {
		Intent intent = new Intent(this, Main.class);
		Bitmap bitmap  = canvas.getCanvasBitmap(true);
		Log.d("ImageEditor", "I'm curious: " + (bitmap == this.bitmap));
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteStream);
		intent.putExtra(Main.NOTE_IMAGE, byteStream.toByteArray());
		
		Log.d("VC", "ImageEditor exit:" + visualColumn);
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
		// get information from intent
		Intent intent = getIntent();
		if (intent != null) {
			// Set background if you can
			visualColumn = intent.getIntExtra(Main.NOTE_VISUAL_COLUMN, 0);
			Drawable gradient = WorldView.getGradient(visualColumn);
			layout.setBackgroundDrawable(gradient);
			
			// Set initial image if there is any
	    	String fileName = intent.getStringExtra(Main.NOTE_IMAGE_FILE);
	    	if (fileName != null) {
	    		bitmap = Note.getBitmapFromStorage(fileName);
	    		canvas.setClearImageBitmap(bitmap);
	    	}
		}
		
		if (bitmap != null) {
    		canvas.setClearImageBitmap(bitmap);			
		}
		
		Log.d("VC", "ImageEditor entry:" + visualColumn);
		
		// initialize the pen
		PenSettingInfo penInfo = new PenSettingInfo();
		penInfo.setPenColor(Color.WHITE);
		canvas.setPenSettingInfo(penInfo);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == penButton.getId()) { 
			pressPenButton();
		} else if (id == eraseButton.getId()) {
			pressEraseButton();
		} else if (id == undoButton.getId()) {
			pressUndoButton();
		} else if (id == redoButton.getId()) {
			pressRedoButton();
		}
	}
	
	private void pressPenButton() {
		if (canvas.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_PEN) {
			canvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN);
		} else {
			canvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
			canvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
			updateModeState();
		}
	}
	
	private void pressEraseButton() {
		if(canvas.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER){
			canvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER);
		} else {
			canvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
			canvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
			updateModeState();
		}
	}
	
	private void updateModeState() {
		int mode = canvas.getCanvasMode();
		switch(mode) {
		case SCanvasConstants.SCANVAS_MODE_INPUT_PEN:
			penButton.setTextColor(Color.WHITE);
			eraseButton.setTextColor(Color.BLACK);
			break;
		case SCanvasConstants.SCANVAS_MODE_INPUT_ERASER:
			penButton.setTextColor(Color.BLACK);
			eraseButton.setTextColor(Color.WHITE);
			break;
		}
	}
	
	private void pressUndoButton() {
		canvas.undo();
		checkUndoRedoButtons();
	}
	
	private void pressRedoButton() {
		canvas.redo();
		checkUndoRedoButtons();
	}
	
	private void checkUndoRedoButtons() {
		undoButton.setEnabled(canvas.isUndoable());
		redoButton.setEnabled(canvas.isRedoable());
	}
	
	@Override
	public void onHistoryChanged(boolean undoable, boolean redoable) {
		undoButton.setEnabled(undoable);
		redoButton.setEnabled(redoable);
	}
}