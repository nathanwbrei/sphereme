package com.theamrzone.android.sphereme;

import java.io.ByteArrayInputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class AbstractNote {
	
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
	
	public static byte[] BitmapToBinary(Bitmap bmp)
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
	
	
	
}
