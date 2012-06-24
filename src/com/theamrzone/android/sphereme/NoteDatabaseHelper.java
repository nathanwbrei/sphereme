package com.theamrzone.android.sphereme;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.view.View;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

	private static NoteDatabaseHelper INSTANCE = null;
    
    private static int s_note_counter=-1;
    
    private SQLiteStatement update;
    private SQLiteStatement add;
    
    private NoteDatabaseHelper(Context context) {
        super(context, SQLStatements.DATABASE_NAME, null, DATABASE_VERSION);
        Cursor c= this.getReadableDatabase().rawQuery(SQLStatements.GET_COUNTER, new String[] {});
        c.moveToNext();
        s_note_counter=c.getInt(0);
        
        update=this.getWritableDatabase().compileStatement(SQLStatements.UPDATE_NOTE);
        add= this.getWritableDatabase().compileStatement(SQLStatements.INSERT_NOTE);
    }

    public static NoteDatabaseHelper getInstance(Context context)
    {
    	if (INSTANCE==null)
    	{
    		INSTANCE= new NoteDatabaseHelper(context);
    	}
    	
    	return INSTANCE;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLStatements.NOTE_TABLE_CREATE);
        
        //debugging purposes only
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SQLStatements.DATABASE_NAME);
 
        // Create tables again
        onCreate(db);	
    }
	
	public static int incrementCounter()
	{
		int i=s_note_counter;
		s_note_counter++;
		return i;
	}
	
	public void addNote(AbstractNote n)
	{
		add.clearBindings();
		add.bindDouble(1, n.getR());
		add.bindDouble(2, n.getT());
		add.bindDouble(3, n.getZ());
		Log.d("DEBUG", "a");
		NormalVector normal=n.getNormalVector();
		add.bindDouble(4, normal.getX());
		add.bindDouble(5, normal.getY());
		add.bindDouble(6, normal.getZ());
		Log.d("DEBUG", "b");
		add.bindString(7, n.getType());
		Log.d("DEBUG", "c");
		

		add.bindBlob(8, AbstractNote.bitmapToBinary(n.getThumbnail()));
		add.bindBlob(9, n.getContent());
		Log.d("DEBUG", "d");
		add.bindLong(10, n.getId());
		add.executeInsert();
	}
	
	/**
	 * Updates note. Assume it already exists.
	 * @param n the current note
	 * @return whether the update was succesful or not
	 */
	public void updateNote(AbstractNote n)
	{
		update.clearBindings();
		update.bindDouble(1, n.getR());
		update.bindDouble(2, n.getT());
		update.bindDouble(3, n.getZ());
		
		NormalVector normal=n.getNormalVector();
		update.bindDouble(4, normal.getX());
		update.bindDouble(5, normal.getY());
		update.bindDouble(6, normal.getZ());
		
		update.bindString(7, n.getType());

		update.bindBlob(8, AbstractNote.bitmapToBinary(n.getThumbnail()));
		update.bindBlob(9, n.getContent());
		
		update.bindLong(10, n.getId());
		update.executeInsert();
	}
	
	public ArrayList<AbstractNote> getNotes()
	{
		Cursor c= this.getReadableDatabase().rawQuery(SQLStatements.GET_ALL_NOTES, new String[] {});
		
		ArrayList<AbstractNote> notes = new ArrayList<AbstractNote> (s_note_counter);
		
		while(c.moveToNext())
		{
			double r=c.getDouble(0);
			Log.d("get",""+r);
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
	
	public void debug(View view)
	{
		
	}
}