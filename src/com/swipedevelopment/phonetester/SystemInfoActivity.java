package com.swipedevelopment.phonetester;

import java.util.ArrayList;

import com.swipedevelopment.sql.DatabaseManager;
import com.swipedevelopment.sql.SystemInfoDatabase;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class SystemInfoActivity extends Activity{
	ListView listview;
	BaseAdapter baseAdapter;
	Component comp;
	private final String[] action={"None","Call","SMS","Wifi","Bluetooth","Camera"
			                          ,"Video","Music","GPS","Website","NFC","3D"};
	private final String[] duration = {"0 min","0.5 min","1 min","2 min","3 min","4 min","5 min","6 min",
			                          "7 min","8 min","9 min","10 min"};
	private ArrayAdapter<String> adapter1, adapter2;
	private final ArrayList<Integer> spinner1States = new ArrayList<Integer>();
	private final ArrayList<Integer> spinner2States = new ArrayList<Integer>();
	SharedPreferences sharedPreference, sharedPreference2;
	SharedPreferences.Editor editor;
	DatabaseManager db_man;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//getFragmentManager().beginTransaction().replace(android.R.id.content, new SystemFragment()).commit();
		setContentView(R.layout.system_info);
		initialListview();
		db_man = new DatabaseManager(this);
		db_man.addTestFunction();
		Cursor c = db_man.getTestFunctions();
		while(c.moveToNext())
		{
			Log.d("CURSOR INFO", c.getString(0) + " " + c.getString(1) + " " + c.getString(2));
		}
		db_man.close();
	}
	private void initialListview() {
		// TODO Auto-generated method stub
		listview = (ListView)findViewById(R.id.listview);
		for(int i = 0; i< 20; ++ i ){
			
			spinner1States.add(i, 0);
			spinner2States.add(i, 0);

		}
		baseAdapter =  new BaseAdapter(){

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 20;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater)SystemInfoActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.rowview,null);
				comp = new Component();
				comp.spinner1 = (Spinner)convertView.findViewById(R.id.spinner1);
			    comp.spinner2 = (Spinner)convertView.findViewById(R.id.spinner2);
				adapter1 = new ArrayAdapter<String>(SystemInfoActivity.this,android.R.layout.simple_spinner_item,action);
				adapter2 = new ArrayAdapter<String>(SystemInfoActivity.this,android.R.layout.simple_spinner_item,duration);
				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    comp.spinner1.setAdapter(adapter1);
			    comp.spinner2.setAdapter(adapter2);
			    convertView.setTag(comp);
			    comp = (Component) convertView.getTag();
			    
 				comp.spinner1.setSelection(spinner1States.get(position));
 				comp.spinner2.setSelection(spinner2States.get(position));
 				final int pos = position;
 				comp.spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						// TODO Auto-generated method stub
						Spinner sp01 = (Spinner) parent;
						spinner1States.set(pos, (int) sp01.getItemIdAtPosition(position));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
 					
 				});
 				comp.spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						// TODO Auto-generated method stub
						Spinner sp02 = (Spinner) parent;
						spinner2States.set(pos, (int) sp02.getItemIdAtPosition(position));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
 					
 				});
				return convertView;
			}
			
		};
		listview.setAdapter(baseAdapter); 
	}
	
    public void saveValues(){
    	sharedPreference = getSharedPreferences("SystemInfo", Activity.MODE_PRIVATE);
    	editor = sharedPreference.edit();
    	int i = 0;
    	while(i<20){		
    		editor.putInt("sp1Status" + i, spinner1States.get(i));
    		editor.putInt("sp2Status" + i, spinner2States.get(i));
    		i++;
    	}
    	editor.commit();
    	System.out.println("saveValues");
    }
//    public void initialValue(){
//    	sharedPreference = getSharedPreferences("SystemInfo",Activity.MODE_PRIVATE);
////    	spinner1Stat = sharedPreference.getAll();
//    	
//    }
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		System.out.println("check onRestoreInstanceState");
		}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		System.out.println("check onSaveInstanceState" );
		//save value;
		outState.putIntegerArrayList("spinner01States", spinner1States);
		outState.putIntegerArrayList("spinner02States", spinner2States);

	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		saveValues();
		System.out.println("SystemInfo: onPause");

	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();	
		saveValues();
		System.out.println("SystemInfo: onstop");
	}
}
