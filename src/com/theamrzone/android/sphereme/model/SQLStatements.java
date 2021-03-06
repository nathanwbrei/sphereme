package com.theamrzone.android.sphereme.model;

public class SQLStatements {
	
	static final String DATABASE_NAME = "NotesDatabaseTest";
    static final String NOTE_TABLE_NAME = "notes";
    static final String NOTE_TABLE_CREATE =
                "CREATE TABLE " + NOTE_TABLE_NAME + " (" +
                "r double, " +
                "t double, " +
                "z double, " +
                "nx double, " +
                "ny double, " +
                "nz double, " +
                "type text, " +
                "thumbnail blob, " +
                "content text,"+
                "_id int primary key" +
                ");";

    static final String GET_NOTE_EXISTS = "SELECT count(*) from notes where _id = ?";
    static final String INSERT_NOTE = "INSERT INTO notes (r,t,z,nx,ny,nz,type,thumbnail,content, _id) " +
    								  " values (?,?,?,?,?,?,?,?,?,?)";
    static final String UPDATE_NOTE = "UPDATE notes set r=?,t=?,z=?,nx=?,ny=?,nz=?, type=?, thumbnail=?, content=? where _id=?";
    static final String GET_ALL_NOTES = "SELECT r,t,z,nx,ny,nz,type,thumbnail,content,_id FROM notes";
    static final String GET_COUNTER = "SELECT COALESCE(MAX(_id)+1,0) FROM notes";
    static final String DELETE_NOTE = "DELETE FROM notes where _id=?";
    static final String FLUSH_DATABASE = "DELETE FROM notes";
}