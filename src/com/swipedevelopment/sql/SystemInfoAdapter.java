package com.swipedevelopment.sql;

import java.util.ArrayList;

import com.swipedevelopment.phonetester.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

public class SystemInfoAdapter extends BaseAdapter 
{

	private ArrayList<RowInfo> row_state;
	private Context context;
	private final String[] action = {"None","Call","SMS","Wifi","Bluetooth","Camera"
            ,"Video Recorder","Ringtone","GPS","Web Browser","Email","NFC","3D","Voice Recorder"};
	private final String[] duration = 
		   {"0 Min","1 Min","2 Min","3 Min","4 Min","5 Min","6 Min","7 Min","8 Min","9 Min","10 Min", 
			"11 Min","12 Min","13 Min","14 Min","15 Min","16 Min","17 Min","18 Min","19 Min","20 Min",
			"21 Min","22 Min","23 Min","24 Min","25 Min","26 Min","27 Min","28 Min","29 Min","30 Min",
			"31 Min","32 Min","33 Min","34 Min","35 Min","36 Min","37 Min","38 Min","39 Min","40 Min",
			"41 Min","42 Min","43 Min","44 Min","45 Min","46 Min","47 Min","48 Min","49 Min","50 Min",
			"51 Min","52 Min","53 Min","54 Min","55 Min","56 Min","57 Min","58 Min","59 Min","60 Min",
	};
	
	public SystemInfoAdapter(ArrayList<RowInfo> row_state,Context context) {
		this.row_state = row_state;
		this.context = context;
	}

	@Override
	public int getCount() {
		return row_state.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		
		convertView = inflater.inflate(R.layout.rowview,null);
		Spinner spinner_app = (Spinner) convertView.findViewById(R.id.spinner);
		Spinner spinner_duration = (Spinner) convertView.findViewById(R.id.duration);
		CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkBox);
		
		ArrayAdapter<String> spinner_adapter_app = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,action);
		ArrayAdapter<String> spinner_adapter_duration = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,duration);
		
		spinner_adapter_duration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_adapter_app.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner_duration.setAdapter(spinner_adapter_duration);	
	    spinner_app.setAdapter(spinner_adapter_app);
	    
		spinner_app.setSelection(row_state.get(position).getApp());
		spinner_duration.setSelection(row_state.get(position).getDuration());
		checkbox.setChecked(row_state.get(position).isChecked());
			
		final int row_pos = position;
		
		spinner_app.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				RowInfo ri = row_state.get(row_pos);
				ri.setApp(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
				
		});
		
		spinner_duration.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				RowInfo ri = row_state.get(row_pos);
				ri.setDuration(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
				
		});
		
		checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				RowInfo ri = row_state.get(row_pos);
				ri.setChecked(isChecked);
			}
		});
		
		return convertView;
	}

}
