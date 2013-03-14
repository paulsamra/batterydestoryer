package com.swipedevelopment.phonetester;

import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;	
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView telephoneID,voltage,current,temperature,batteryStatus,batteryLevel,signalStrength_view;
	TelephonyManager telephonyManager;
	SignalStrengthListener signalListener;
	int dbm_result,asu_result, battery_voltage,level,scale;
	double temper;
	String battery_status;
	Button run_btn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		telephoneID = (TextView)findViewById(R.id.textView2);
		voltage = (TextView)findViewById(R.id.textView4);
		current = (TextView)findViewById(R.id.textView6);
		temperature = (TextView)findViewById(R.id.textView8);
		batteryStatus = (TextView)findViewById(R.id.textView10);
		signalStrength_view = (TextView)findViewById(R.id.textView12);
		batteryLevel = (TextView)findViewById(R.id.textView14);
		telephonyManager = (TelephonyManager)getSystemService(MainActivity.TELEPHONY_SERVICE);
		signalListener = new SignalStrengthListener();
		telephonyManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		String deviceId = telephonyManager.getDeviceId();
		telephoneID.setText(deviceId);
		
		
		
		
	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(batteryInfoReceiver);
		telephonyManager.listen(signalListener, PhoneStateListener.LISTEN_NONE);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)); 
		telephonyManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		
	}
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		telephonyManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		telephonyManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver(){

		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)){
				battery_voltage = intent.getIntExtra("voltage",0);
				temper = intent.getIntExtra("temperature", 0);
				level =intent.getIntExtra("level", 0);
				scale = intent.getIntExtra("scale", 100);
				String tempStr = Double.toString(temper/10);
				
				switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN))   
                {  
                case BatteryManager.BATTERY_STATUS_CHARGING:  
                	battery_status = "Charging..";  
                    break;  
                case BatteryManager.BATTERY_STATUS_DISCHARGING:  
                	battery_status = "Discharging";  
                    break;  
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:  
                	battery_status = "No charging";  
                    break;  
                case BatteryManager.BATTERY_STATUS_FULL:  
                	battery_status = "Battery full";  
                    break;  
                case BatteryManager.BATTERY_STATUS_UNKNOWN:  
                	battery_status = "Status unknown";  
                    break;  
                }  
				temperature.setText(tempStr + "¡æ");
				voltage.setText(battery_voltage + " mv");
				batteryStatus.setText(battery_status);
				batteryLevel.setText(String.valueOf(level*100/scale) + "%");
			}
		}
		
	};
	
	public class SignalStrengthListener extends PhoneStateListener{

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			// TODO Auto-generated method stub
			super.onSignalStrengthsChanged(signalStrength);	
			asu_result = signalStrength.getGsmSignalStrength();
			dbm_result = -113 + 2*asu_result;
//	        System.out.println("signal is " + dbm_result + " dBm"  + asu_result + " asu");
	        signalStrength_view.setText(dbm_result + " dBm  " + asu_result+" asu");
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()){
		case R.id.menu_settings:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, PreferencesActivity.class);
			startActivityForResult(intent, 0); 
        break;
		case R.id.menu_system:
			Intent intent2 = new Intent();
			intent2.setClass(MainActivity.this, SystemInfoActivity .class);
			startActivityForResult(intent2,0);
		break;
		case R.id.menu_about:
			Intent intent3 = new Intent();
			intent3.setClass(MainActivity.this, AboutActivity.class);
			startActivityForResult(intent3,0);
		break;
		}
        return true;
	}
}
