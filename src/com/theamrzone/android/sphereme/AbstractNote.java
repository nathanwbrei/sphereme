package com.theamrzone.android.sphereme;

import java.io.ByteArrayInputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;

public abstract class AbstractNote {

	public static final int N_THUMB_WIDTH=100;
	public static final int N_THUMB_HEIGHT=100;

	//is is unique, hence no setter, calculated form max in database +1
	public abstract int getId();

	public abstract double getR();
	public abstract double getZ();
	public abstract double getT();

	public abstract NormalVector getNormalVector();

	public abstract String getType();
	public abstract void setContent(String s);
	public abstract void setContent(Bitmap b);

	//Don't call directly unless you need to
	//Automatically gets called if you call setContent()
	public abstract void setThumbnail(Bitmap b);

	public abstract byte[] getContent();
	public abstract Bitmap getThumbnail();

	//must give it a database instance
	public void save(NoteDatabaseHelper h)
	{
		Log.d("NOTE", "Save called");
		if(h.existsNote(this))
		{
			Log.d("NOTE","Note already exists..");
			h.updateNote(this);
		}
		else
		{
			h.addNote(this);
		}
	}

	public abstract void setRTZ(double r, double t, double z);

	public static Bitmap binaryToBitmap(byte[] input)
	{
		ByteArrayInputStream imageStream = new ByteArrayInputStream(input);
        return BitmapFactory.decodeStream(imageStream);
	}

	// converts a bmp to byte[]
	public static byte[] bitmapToBinary(Bitmap bmp)
	{
		int size = bmp.getRowBytes() * bmp.getHeight();
		ByteBuffer b = ByteBuffer.allocate(size);
		bmp.copyPixelsToBuffer(b);

		byte [] thumbnailArray = new byte[size];

		try {
		    b.get(thumbnailArray, 0, thumbnailArray.length);
		}
		catch (BufferUnderflowException e)
		{
		    //always happens
		}

		return thumbnailArray;
	}

	public static String binaryToString(byte[] arr)
	{
		return new String(arr);
	}

	public static byte[] stringToByte(String s)
	{
		Log.d("NOTE","Trying to convert to byte[]: "+s);

		return s.getBytes();
	}

	public static Bitmap stringToBitmap(String s) {
		int w = 100, h = 100;
		Paint paint = new Paint();
		paint.setColor(0xff00ff00);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);

		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
		Canvas c = new Canvas(bmp);
		c.drawText(s, 0, 0, paint);

		return bmp;
	}

	public static Bitmap generateThumbnail(Bitmap b)
	{
		return Bitmap.createScaledBitmap(b, N_THUMB_WIDTH, N_THUMB_HEIGHT, false);
	}

	public static Bitmap generateThumbnail(String s)
	{
		return generateThumbnail(stringToBitmap(s));
	}

}