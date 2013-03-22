package com.swipedevelopment.phonetester;

import java.util.ArrayList;

import com.swipedevelopment.sql.DatabaseManager;
import com.swipedevelopment.sql.RowInfo;
import com.swipedevelopment.sql.SystemInfoAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class SystemInfoActivity extends Activity{
	ListView listview;
	private ArrayList<RowInfo> row_state = new ArrayList<RowInfo>();
	DatabaseManager db_man;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info);
		
		db_man = new DatabaseManager(this);
		initialListview();
	}
	
	private void initialListview() {
		try
		{
			
			Cursor c = db_man.getTestFunctions();
			for(int i = 0; c.moveToNext(); i++)
			{
				row_state.add(new RowInfo());
				RowInfo ri = row_state.get(i);
				ri.setApp(c.getInt(1));
				ri.setDuration(c.getInt(2));
				ri.setChecked(c.getInt(3) == 1);
				Log.d("CURSOR INFO", c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
			}
			
		} catch(Exception e) {
			
			//TODO
			
		} finally {
			
			db_man.close();
			
			if(row_state.size() != 20)
			{
				//TODO ERROR!
			}
		}
		
		listview = (ListView) findViewById(R.id.listview);
		
		SystemInfoAdapter adapter = new SystemInfoAdapter(row_state,this);
		listview.setAdapter(adapter);
	}
	
	
	@Override
	protected void onPause() {		
		db_man = new DatabaseManager(this);
		int id = 0;
		for(RowInfo ri : row_state)
		{
			db_man.addTestFunction(ri.getApp(), ri.getDuration(), ri.isChecked() ? 1 : 0,id);
			id++;
		}
		db_man.close();
		
		super.onPause();
	}
}