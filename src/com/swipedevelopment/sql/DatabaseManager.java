package com.swipedevelopment.sql;

import static com.swipedevelopment.sql.SystemInfoDatabase.TABLE_NAME;
import static com.swipedevelopment.sql.SystemInfoDatabase.COLUMN_APP;
import static com.swipedevelopment.sql.SystemInfoDatabase.COLUMN_TIME;
import static com.swipedevelopment.sql.SystemInfoDatabase.COLUMN_CHECK;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

	SQLiteDatabase sql_db;//used to write queries
	SystemInfoDatabase sys_info_db;//get the table from databse
	boolean closed;
	
	public DatabaseManager(Context context) 
	{
		 sys_info_db = new SystemInfoDatabase(context);
	}
	
	/*
	 * Add functions to test the drain on phone
	 */
	public void addTestFunction()
	{
		sql_db =  sys_info_db.getWritableDatabase();
		sql_db.execSQL("insert into " +  TABLE_NAME + " values ('hey' , 'dude', 3);");
		//sql_db.close();
	}
	
	/*
	 * Return of all the functions to test
	 */
	public Cursor getTestFunctions()
	{
		sql_db = sys_info_db.getWritableDatabase();
		Cursor cursor = sql_db.rawQuery("select * from " + TABLE_NAME + ";",null);
		//sql_db.close();
		return cursor;
	}
	
	/*
	 * close the database
	 */
	public void close()
	{
		sys_info_db.close();
		closed = true;
	}
	
	public boolean isClosed()
	{
		return closed;
	}
	
	/*
	 * re-open a closed database
	 */
	public void reopenDatabase(Context context)
	{
		sys_info_db = new SystemInfoDatabase(context);
		closed = false;
	}
}
