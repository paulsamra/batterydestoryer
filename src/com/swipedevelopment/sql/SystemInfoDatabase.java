package com.swipedevelopment.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SystemInfoDatabase extends SQLiteOpenHelper
{
	private final static String DATABASE_NAME = "systeminfo.db";
	private final static int DATABASE_VERSION = 1;
	public final static String TABLE_NAME = "testcases";
	public final static String COLUMN_ID = "id";
	public final static String COLUMN_APP = "function";
	public final static String COLUMN_DURATION = "duration";
	public final static String COLUMN_CHECK = "valid";
	

	public SystemInfoDatabase(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("create table " + TABLE_NAME
				   + "( " + COLUMN_ID + " integer primary key"
				   + ", " + COLUMN_APP + " integer"
				   + ", " + COLUMN_DURATION + " integer"
				   + ", " + COLUMN_CHECK + " integer);");
		for(int i=0; i < 25; i++)
		{
			db.execSQL("insert into " +  TABLE_NAME + " values( " 
						+ i + " , 0, 0, 0);");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
	}
}
