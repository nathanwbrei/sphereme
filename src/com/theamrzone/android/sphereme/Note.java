package com.theamrzone.android.sphereme;

public class Note implements INote {

	private final int id;
	private double r;
	private double t;
	private double z;
	private NormalVector n;
	private String type;
	private byte [] thumbnail;
	private byte [] content;

	//RELIES ON THE FACT THAT THE DATABASE HAS ALREADY BEEN INITIATED
	public Note(double r, double t, double z, double nx, double ny,
			double nz, String type, byte[] thumbnail, byte[] content) {
		
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
		this.content=content;
		
	}
	
	// SHOULD ONLY BE CALLED BY NOTEDATEBASEHELPER
	public Note(double r, double t, double z, double nx, double ny,
			double nz, String type, byte[] thumbnail, byte[] content, int id) {
		
		this.id=id;
		this.r=r;
		this.t=t;
		this.z=z;
		n= new NormalVector(nx,ny,nz);
		this.type=type;
		this.thumbnail=thumbnail;
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
	public boolean save() {
		// TODO Auto-generated method stub
		return false;
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
	public void setType(String type) {
		this.type=type;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public byte[] getThumbnail() {
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
}
