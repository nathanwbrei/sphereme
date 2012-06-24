package com.theamrzone.android.sphereme;

import java.io.ByteArrayInputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Base64;

public abstract class AbstractNote {
	
	//is is unique, hence no setter, calculated form max in database +1
	public abstract int getId();

	public abstract double getR();
	public abstract double getZ();
	public abstract double getT();

	public abstract NormalVector getNormalVector();
	
	public abstract String getType();
	public abstract void setContent(String s);
	public abstract void setContent(Bitmap b);
	
	public abstract void setThumbnail(Bitmap b);
	
	public abstract byte[] getContent();
	public abstract Bitmap getThumbnail();
	
	//must give it a database instance
	public void save(NoteDatabaseHelper h)
	{
		h.updateNote(this);
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
		return Base64.encodeToString(arr, Base64.DEFAULT);
	}
	
	public static byte[] stringToByte(String s)
	{
		return Base64.decode(s, Base64.DEFAULT);
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
}