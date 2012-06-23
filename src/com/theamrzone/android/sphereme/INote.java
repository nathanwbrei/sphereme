package com.theamrzone.android.sphereme;

public interface INote {
	
	public int getId();

	public double getR();
	public double getZ();
	public double getT();

	public NormalVector getNormalVector();
	
	public String getType();
	public void setType(String type);
	
	public byte[] getContent();
	public byte[] getThumbnail();
	
	public boolean save();
	
	public void setRTZ(double r, double t, double z);
	
}
