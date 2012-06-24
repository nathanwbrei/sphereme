package com.theamrzone.android.sphereme;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    
    private static int s_note_counter=-1;
    
    private SQLiteStatement update;
    private SQLiteStatement add;
    
    NoteDatabaseHelper(Context context) {
        super(context, SQLStatements.DATABASE_NAME, null, DATABASE_VERSION);
        Cursor c= this.getReadableDatabase().rawQuery(SQLStatements.GET_COUNTER, new String[] {});
        s_note_counter=c.getInt(0);
        
        update=this.getWritableDatabase().compileStatement(SQLStatements.UPDATE_NOTE);
        add= this.getWritableDatabase().compileStatement(SQLStatements.INSERT_NOTE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLStatements.NOTE_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		//TODO:
	}
	
	public static int incrementCounter()
	{
		int i=s_note_counter;
		s_note_counter++;
		return i;
	}
	
	public void addNote(INote n)
	{
		add.clearBindings();
		add.bindDouble(0, n.getR());
		add.bindDouble(1, n.getT());
		add.bindDouble(2, n.getZ());
		
		NormalVector normal=n.getNormalVector();
		add.bindDouble(3, normal.getX());
		add.bindDouble(4, normal.getY());
		add.bindDouble(5, normal.getZ());
		
		add.bindString(6, n.getType());
		
		add.bindBlob(7, n.getThumbnail());
		add.bindBlob(8, n.getContent());
		
		add.bindLong(9, n.getId());
		add.executeInsert();
	}
	
	/**
	 * Updates note. Assume it already exists.
	 * @param n the current note
	 * @return whether the update was succesful or not
	 */
	public void updateNote(INote n)
	{
		update.clearBindings();
		update.bindDouble(0, n.getR());
		update.bindDouble(1, n.getT());
		update.bindDouble(2, n.getZ());
		
		NormalVector normal=n.getNormalVector();
		update.bindDouble(3, normal.getX());
		update.bindDouble(4, normal.getY());
		update.bindDouble(5, normal.getZ());
		
		update.bindString(6, n.getType());
		
		update.bindBlob(7, n.getThumbnail());
		update.bindBlob(8, n.getContent());
		
		update.bindLong(9, n.getId());
		update.executeInsert();
	}
	
	public ArrayList<INote> getNotes()
	{
		Cursor c= this.getReadableDatabase().rawQuery(SQLStatements.GET_ALL_NOTES, new String[] {});
		
		ArrayList<INote> notes = new ArrayList<INote> (s_note_counter);
		
		while(c.moveToNext())
		{
			double r=c.getDouble(0);
			double t=c.getDouble(1);
			double z=c.getDouble(2);
			double nx=c.getDouble(3);
			double ny=c.getDouble(4);
			double nz=c.getDouble(5);
			String type=c.getString(6);
			byte[] thumbnail = c.getBlob(7);
			byte[] content = c.getBlob(8);
			int id = c.getInt(9);
			
			notes.add(new Note(r,t,z,nx,ny,nz,type,thumbnail,content,id));		
		}
		
		return notes;
	}
}