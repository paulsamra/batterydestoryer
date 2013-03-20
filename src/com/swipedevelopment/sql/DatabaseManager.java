package com.swipedevelopment.sql;

import static com.swipedevelopment.sql.SystemInfoDatabase.TABLE_NAME;
import static com.swipedevelopment.sql.SystemInfoDatabase.COLUMN_ID;
import static com.swipedevelopment.sql.SystemInfoDatabase.COLUMN_APP;
import static com.swipedevelopment.sql.SystemInfoDatabase.COLUMN_DURATION;
import static com.swipedevelopment.sql.SystemInfoDatabase.COLUMN_CHECK;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

	SQLiteDatabase query_db;//used to write queries
	SystemInfoDatabase sys_info_db;//get the table from database
	boolean closed;
	
	public DatabaseManager(Context context) 
	{
		 sys_info_db = new SystemInfoDatabase(context);
	}
	
	/*
	 * Add functions to test the drain on phone
	 */
	public void addTestFunction(int app, int dur, int check, int id)
	{
		query_db =  sys_info_db.getWritableDatabase();
		query_db.execSQL("update " + TABLE_NAME + " set "
						 + COLUMN_APP + " = " + app + " , "
						 + COLUMN_DURATION + " = " + dur + " , "
						 + COLUMN_CHECK + " = " + check
						 + " where " + COLUMN_ID + " = " + id + ";");
	}
	
	/*
	 * Return of all the functions to test
	 */
	public Cursor getTestFunctions()
	{
		query_db = sys_info_db.getWritableDatabase();
		Cursor cursor = query_db.rawQuery("select * from " + TABLE_NAME + ";",null);
		//sql_db.close();
		return cursor;
	}

/*****************My Testing Functions**********************/
	public void createTable()
	{
		query_db = sys_info_db.getWritableDatabase();
		query_db.execSQL("create table " + TABLE_NAME
				   + "( " + COLUMN_ID + " integer primary key"
				   + ", " + COLUMN_APP + " integer"
				   + ", " + COLUMN_DURATION + " integer"
				   + ", " + COLUMN_CHECK + " integer);");
		for(int i=0; i < 25; i++)
		{
			query_db.execSQL("insert into " +  TABLE_NAME + " values( " 
						+ i + " , 0, 0, 0);");
		}
	}
	
	public void dropTable()
	{
		query_db =  sys_info_db.getWritableDatabase();
		query_db.execSQL("drop table " + TABLE_NAME + " ;");
	}
/******************My Testing Functions END*****************/
	
	/*
	 * close the database
	 */
	public void close()
	{
		sys_info_db.close();
		closed = true;
	}
	
	/*
	 * check is database has been closed
	 */
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
