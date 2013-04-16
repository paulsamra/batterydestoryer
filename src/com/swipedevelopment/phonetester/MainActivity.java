package com.swipedevelopment.phonetester;

import com.swipedevelopment.service.MyService;
import com.swipedevelopment.sql.DatabaseManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;	
import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	TextView telephoneID,voltage,current,temperature,batteryStatus,batteryLevel,signalStrength_view,progressbar_text;
	TelephonyManager telephonyManager;
	SignalStrengthListener signalListener;
	int dbm_result,asu_result, battery_voltage,level,scale,current_progress;
	double temper;
	String battery_status;
	Button run_btn;
	public static ProgressBar progressbar;
	SharedPreferences mySharedPreferences;
	boolean loop_preference,web_wifi_preference,video_wifi_preference;
	String telephone_preference,sms_preference,smsNum_preference,ringtone_preference,address_preference1,address_preference2,city_preference,state_preference,volume_preference,
	power_preference,brightness_preference,web_preference,email_preference1,video_preference;
	DatabaseManager db_man;
	public static Context context;
//	private ArrayList<RowInfo> row_state = new ArrayList<RowInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = this;
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
		progressbar = (ProgressBar)findViewById(R.id.progressbar);
		progressbar.setVisibility(ProgressBar.INVISIBLE);
		progressbar_text = (TextView)findViewById(R.id.progressbar_text);
		run_btn = (Button)findViewById(R.id.button1);
		run_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				progressbar.setVisibility(ProgressBar.VISIBLE);
				DatabaseManager db_man = new DatabaseManager(MainActivity.this);
				Cursor c = db_man.getTestFunctions();
				int totalprogress = 0;
				while(c.moveToNext()) {
					if(c.getInt(3) == 1)
					  totalprogress+=c.getInt(2);
				}
				totalprogress*=60;
				progressbar.setMax(totalprogress);
				progressbar.setProgress(0);
				c.close();
				db_man.close();
				run();
			}
			
		});
		
		loadPref();

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
				temperature.setText(tempStr + "��");
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
	public void run(){
		Intent serviceIntent = new Intent();
		serviceIntent.setClass(MainActivity.this, MyService.class);
		
		serviceIntent.putExtra("loopStatus", loop_preference);
		serviceIntent.putExtra("telephoneNum",telephone_preference);
		serviceIntent.putExtra("smsDetail", sms_preference);
		serviceIntent.putExtra("smsNumber", smsNum_preference);
		serviceIntent.putExtra("ringtone", ringtone_preference);
		serviceIntent.putExtra("address1", address_preference1);
		serviceIntent.putExtra("address2", address_preference2);
		serviceIntent.putExtra("city", city_preference);
		serviceIntent.putExtra("state", state_preference);
		serviceIntent.putExtra("volumeLevel", volume_preference);
		serviceIntent.putExtra("brightness", brightness_preference);
		serviceIntent.putExtra("powerMode", power_preference);
		serviceIntent.putExtra("webBrowser", web_preference);
		serviceIntent.putExtra("switchWifi", web_wifi_preference);
		serviceIntent.putExtra("emailAddress", email_preference1);
		serviceIntent.putExtra("videoId", video_preference);
		serviceIntent.putExtra("videoWifi", video_wifi_preference);
		
		this.startService(serviceIntent);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
			
			loadPref();
		}
	
	private void loadPref(){
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		loop_preference = mySharedPreferences.getBoolean("Checkbox1", false);
		telephone_preference = mySharedPreferences.getString("EditTextPreference1", "");
		sms_preference = mySharedPreferences.getString("EditTextPreference2", "");
		smsNum_preference = mySharedPreferences.getString("ListPreference2", "");
		ringtone_preference = mySharedPreferences.getString("ringtone", "<unset>");
		//location including 4 parts: address line1&2, city, and state
		address_preference1 = mySharedPreferences.getString("EditTextPreference4", "");
		address_preference2 = mySharedPreferences.getString("EditTextPreference5", "");
		city_preference = mySharedPreferences.getString("EditTextPreference6", "");
		state_preference = mySharedPreferences.getString("EditTextPreference7", "");
		//email address as receiver
		email_preference1 = mySharedPreferences.getString("EditTextPreference8", "");
		//system settings preferences
		volume_preference = mySharedPreferences.getString("ListPreference3", "1");
		brightness_preference = mySharedPreferences.getString("ListPreference4", "1");
		power_preference = mySharedPreferences.getString("ListPreference5", "1");
		//web address and wifi use switch
		web_preference = mySharedPreferences.getString("EditTextPreference3", "www.swipedevelopment.com");
		web_wifi_preference = mySharedPreferences.getBoolean("SwitchPreference2", false);
		//youtube video id
		video_preference = mySharedPreferences.getString("ListPreference6", "dKi5XoeTN0k");
		video_wifi_preference = mySharedPreferences.getBoolean("SwitchPreference3", false);
		System.out.println("web: " + web_wifi_preference + "video:" + video_wifi_preference);
	}
	//disenable start btn when system running..
//	private void enableButton(int id, boolean isEnable) {
//    ((Button) findViewById(id)).setEnabled(isEnable);
//}
	
	//private void enableButtons(boolean isRecording) {
//  enableButton(R.id.btnStart, !isRecording);
//  enableButton(R.id.btnFormat, !isRecording);
//  enableButton(R.id.btnStop, isRecording);
//}
}
