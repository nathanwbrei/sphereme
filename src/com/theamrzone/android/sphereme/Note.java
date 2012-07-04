package com.theamrzone.android.sphereme;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class Note extends AbstractNote {

	public static final String STRING = "STRING";
	public static final String IMAGE = "IMAGE";
	
	private final int id;
	private double r;
	private double t;
	private double z;
	private NormalVector n;
	private String type;
	private Bitmap thumbnail;
	private String content; // content as a string, or the file location

	//RELIES ON THE FACT THAT THE DATABASE HAS ALREADY BEEN INITIATED
	public Note(double r, double t, double z, double nx, double ny,
			double nz, String type, Bitmap thumbnail, byte[] content, Context c) throws IOException {

		this.id=NoteDatabaseHelper.incrementCounter();

		if (id<0)
		{
			throw new RuntimeException("Database has not been setup. Cannot create a note before init'ing db");
		}
		this.r=r;
		this.t=t;
		this.z=z;
		n= new NormalVector(nx,ny,nz);
		this.type=type;
		this.thumbnail=thumbnail;

		if (type.equals(STRING))
		{
			this.content=Note.binaryToString(content);
		}
		else
		{
			_setInternalContent(content,c);
		}

	}

	private void _setInternalContent(byte[] content, Context c) throws IOException {
		String file_location="note"+id;
		FileOutputStream fos=null;
		
		try
		{
			fos = c.openFileOutput(file_location, Context.MODE_PRIVATE);
			fos.write(content);
		}
		catch (FileNotFoundException e)
		{
			Log.e("Should not ever be getting this.", e.toString());
		}
		finally
		{
			fos.close();
		}
		
		this.content=file_location;
		
	}

	// SHOULD ONLY BE CALLED BY NOTEDATEBASEHELPER
	public Note(double r, double t, double z, double nx, double ny,
			double nz, String type, byte[] thumbnail, String content, int id) {

		this.id=id;
		this.r=r;
		this.t=t;
		this.z=z;
		n= new NormalVector(nx,ny,nz);
		this.type=type;

		this.thumbnail=AbstractNote.binaryToBitmap(thumbnail);

		this.content=content;

	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public double getR() {
		return r;
	}

	@Override
	public double getT() {
		return t;
	}

	@Override
	public double getZ() {
		return z;
	}

	@Override
	public NormalVector getNormalVector() {
		return n;
	}


	@Override
	public void setRTZ(double r, double t, double z) {
		this.r=r;
		this.z=z;
		this.t=t;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setContent(String s) {
		this.type=STRING;
		this.content=s;
		setThumbnail(Note.generateThumbnail(s));
	}
	
	@Override
	public void setContent(Bitmap b, Context c) throws IOException
	{
		this.type=IMAGE;
		_setInternalContent(Note.bitmapToBinary(b),c);
		this.thumbnail=Note.generateThumbnail(b);
	}

	@Override
	public String getStringContent(){
		
		return content;
	}

	@Override
	public Bitmap getThumbnail() {
		return thumbnail;
	}

	//FOR DEBUGGING PURPOSES ONLY
	public String toString()
	{
		return  "r: "  + r + 
				", t:" + t +
				", z:" + z +
				", nx: " + n.getX() +
				", ny: " + n.getY() +
				", nz: " + n.getZ() +
				", type: " + type +
				", id: " + id;
	}


	@Override
	public void setThumbnail(Bitmap bmp) {
		this.thumbnail=bmp;

	}

	@Override
	public Bitmap getBitmapContent() throws Exception {
		if (type.equals(IMAGE))
		{
			return null;
		}
		else
		{
			throw new Exception("Can only call this method if type is Image");
		}
	}
	
	@Override
	public boolean isStringContent()
	{
		return this.type.equals(STRING);
	}
	
	@Override
	public boolean isBitmapContent()
	{
		return this.type.equals(IMAGE);
	}
}