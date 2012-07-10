package com.theamrzone.android.sphereme.model;

import android.graphics.Bitmap;

public class FakeNote extends AbstractNote {

	private final int id;
	private double r;
	private double t;
	private double z;
	private NormalVector n;
	private Bitmap thumbnail;
	private Bitmap image;
	private String content; // content as a string, or the file location

	public FakeNote(double r, double t, double z, double nx, double ny,
			double nz, Bitmap thumbnail, Bitmap content) {
		this.r=r;
		this.t=t;
		this.z=z;
		n= new NormalVector(nx,ny,nz);
		this.thumbnail=thumbnail;
		this.image = content;
		this.id = -1;
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
		return NoteType.FAKE;
	}
	
	@Override
	public void setContent(String s) {}
	
	@Override
	public void setContent(Bitmap b) {
		this.image = b;
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
				", type: " + NoteType.FAKE +
				", id: " + id;
	}


	@Override
	public void setThumbnail(Bitmap bmp) {
		this.thumbnail=bmp;
	}
	
	@Override
	public Bitmap getBitmapContent() {
		return image;
	}
	
	@Override
	public boolean isStringContent() {
		return false;
	}
	
	@Override
	public boolean isBitmapContent() {
		return false;
	}

	@Override
	public String getTypeAsString() {
		return NoteType.FAKE.toString();
	}
}