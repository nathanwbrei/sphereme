package com.theamrzone.android.sphereme.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class Note extends AbstractNote {

	private final int id;
	private double r;
	private double t;
	private double z;
	private NormalVector n;
	private NoteType type;
	private Bitmap thumbnail;
	private Bitmap image;
	private String content; // content as a string, or the file location

	//RELIES ON THE FACT THAT THE DATABASE HAS ALREADY BEEN INITIATED
	public Note(double r, double t, double z, double nx, double ny,
			double nz, NoteType type, Bitmap thumbnail, byte[] content, Context c) {

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
		this.image = null;

		if (type == NoteType.STRING) {
			this.content=Note.binaryToString(content);
		} else {
			setInternalContent(content,c);
		}
	}
	private void setInternalContent(byte[] content, Context c) {
		String file_location="note"+id;
		
		File file = new File(Environment.getExternalStorageDirectory(), file_location);
		FileOutputStream fos = null;
		try {
		    fos = new FileOutputStream(file);
		    fos.write(content);
		    fos.flush();
		} catch (FileNotFoundException e) {
		    Log.d("Note", e.getMessage());
		} catch (IOException e) {
		    Log.d("Note", e.getMessage());
		} finally {
			if (fos != null) {
			    try { // kat thinks this is dumb
					fos.close();
				} catch (IOException e) {} 
			}
		}
		
		Log.d("Note", "Supposedly saved an image.");
		this.content=file_location;
	}

	// SHOULD ONLY BE CALLED BY NOTEDATEBASEHELPER
	public Note(double r, double t, double z, double nx, double ny,
			double nz, NoteType type, byte[] thumbnail, String content, int id) {

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
	public NoteType getType() {
		return type;
	}
	
	public String getTypeAsString() {
		return type.toString();
	}

	@Override
	public void setContent(String s) {
		this.type=NoteType.STRING;
		this.content=s;
		setThumbnail(Note.generateThumbnail(s));
	}
	
	@Override
	public void setContent(Bitmap b, Context c) throws IOException
	{
		this.type=NoteType.IMAGE;
		setInternalContent(Note.bitmapToBinary(b),c);
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

	public static Bitmap getBitmapFromStorage(String fileName) {
		File file = new File(Environment.getExternalStorageDirectory(), fileName);
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = new FileInputStream(file);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			bitmap = BitmapFactory.decodeStream(fis, null, opts); 
		} catch (FileNotFoundException e) {
			Log.d("Note", "Cannot get image" + e.getMessage());
		} finally {
			if (fis != null) {
				try { fis.close(); } catch (IOException e) {} // this is still way dumb.
			}
		}
		return bitmap;
	}
	
	@Override
	public Bitmap getBitmapContent() {
		if (image != null) {
			return image;
		} 
		
		if (type == NoteType.IMAGE) {
			image = getBitmapFromStorage(content);
			
		} else {
			image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		}
		
		return image;
	}
	
	@Override
	public boolean isStringContent()
	{
		return type == NoteType.STRING;
	}
	
	@Override
	public boolean isBitmapContent()
	{
		return type == NoteType.IMAGE;
	}
}