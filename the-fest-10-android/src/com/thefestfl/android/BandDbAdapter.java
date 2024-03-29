/*
 *  John R. Flynn
 *  "The Fest! 10"
 * 
 */

package com.thefestfl.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Adapter for working with the database of band information
// including band name, info, picture, and sample MP3

public class BandDbAdapter {
	
	private SQLiteDatabase bandDb;
	private DatabaseHelper mDbHelper;
//	private static final String TAG = "BandDbAdapter";
	
	static final String KEY_ID = "_id";
	static final String KEY_NAME = "name";
	static final String KEY_PHOTO = "photo";
	static final String KEY_MP3 = "mp3";
	static final String KEY_DESC = "description";
	static final String KEY_SONG_URL = "songURL";
	static final String KEY_SONG_NAME = "songName";
//	static final String KEY_DATE = "date";
//	static final String KEY_DURATION = "length";
//	static final String KEY_VENUE = "venue";
//	static final String KEY_ACOUSTIC = "acoustic";
	
	private static final String BANDSDB_CREATE =
        "create table if not exists bands (" + 
        "_id integer primary key autoincrement, " +
        "name text not null, " + 
        "photo text, " +
        "description text, " +
        "mp3 text, " +
        "songURL text, " +
        "songName text" +
        ");";
	
	private static final String BANDSDB_TABLE = "bands";
	private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;
    
    // Sub-class for helping out with the DB
    private static class DatabaseHelper extends SQLiteOpenHelper {

    	DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
    	
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(BANDSDB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}    	
		
		@Override
		public void onOpen(SQLiteDatabase db){
			db.execSQL(BANDSDB_CREATE);
		}
    }
    
    BandDbAdapter (Context c){
    	this.mCtx = c;
    }
    
    public BandDbAdapter open(){
    	// opens a writeable database
    	mDbHelper = new DatabaseHelper(mCtx);
    	bandDb = mDbHelper.getWritableDatabase();
    	return this;
    }
    
    public void close(){
    	// closes the database
    	mDbHelper.close();
    }
    
    public long addBands(String name, String photo, String info, String mp3, String mp3Url, String mp3Name){
    	// Adds a band to the database from all the individual values
    	// addBands(cv) is the preferred method, though
    	ContentValues cv = new ContentValues();
    	
    	cv.put(KEY_NAME, name);
    	cv.put(KEY_PHOTO, photo);
    	cv.put(KEY_DESC, info);
    	cv.put(KEY_MP3, mp3);
    	cv.put(KEY_SONG_URL, mp3Url);
    	cv.put(KEY_SONG_NAME, mp3Name);
    	
    	return bandDb.insert(BANDSDB_TABLE, null, cv);
    }
    
    public long addBand(ContentValues cv){
    	// Same as above, but the cv is provided outside of the method
    	
    	return bandDb.insert(BANDSDB_TABLE, null, cv);
    }
    
    public Cursor fetchBands(){
    	// returns a list of every band in the database
    	return bandDb.query(BANDSDB_TABLE, new String[] {KEY_NAME}, null, null, null, null, KEY_NAME+" ASC");
    }
    
    public Cursor fetchBand(String bandName){
    	// returns all the info on a single band
    	return bandDb.query(BANDSDB_TABLE, new String[] {KEY_NAME, KEY_PHOTO, KEY_DESC, KEY_MP3, KEY_SONG_URL, KEY_SONG_NAME},
    			KEY_NAME+"='"+bandName+"'", null, null, null, null);
    }
    
    public void destroyDB(){
    	// Armageddon, for the band table
    	bandDb.execSQL("DROP TABLE "+BANDSDB_TABLE+";");
    }
    
    public void createDB(){
    	bandDb.execSQL(BANDSDB_CREATE);
    }
    
    public boolean checkBand(String name){
    	Cursor c = bandDb.query(BANDSDB_TABLE, new String[] {KEY_NAME}, KEY_NAME+"='"+name+"'", null, null, null, null);
    	if (c.getCount() > 0)
    		return true;
    	else
    		return false;
    }
    
}
