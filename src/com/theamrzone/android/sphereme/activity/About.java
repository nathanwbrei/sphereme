package com.theamrzone.android.sphereme.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.theamrzone.android.sphereme.R;
import com.theamrzone.android.sphereme.view.WorldView;

public class About extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// make the image with default black bacground
		ImageView view = new ImageView(this);
		view.setBackgroundColor(Color.BLACK);
		
		// set background maybe
		Intent intent = getIntent();
		if (intent != null) {
			int vc = intent.getIntExtra(Main.NOTE_VISUAL_COLUMN, 0);
			Drawable gradient = WorldView.getGradient(vc);
			view.setBackgroundDrawable(gradient);
		}
		
		// about page!!
		view.setImageResource(R.drawable.about);
		setContentView(view);
	}
	
	// -- MENU STUFF
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about_menu, menu);
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
		case R.id.options_how_to:
			intent = new Intent(this, HowTo.class);
			break;
		case R.id.options_about:
			intent = new Intent(this, About.class);
			break;
		}
			
		if (intent != null) {
			startActivity(intent);
		}
			
		return false;
	}
}