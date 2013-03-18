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
            ,"Video","Music","GPS","Website","NFC","3D"};
	private final String[] duration = {"0 Min","5 Min","10 Min", "15 Min",
			"25 Min", "30 Min","35 Min","40 Min","45 Min","50 Min", "55 Min"};
	
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
