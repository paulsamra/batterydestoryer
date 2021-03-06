package com.swipedevelopment.phonetester;

import com.swipedevelopment.functions.AudioAdmin;
import com.swipedevelopment.functions.PowerAdmin;
import com.swipedevelopment.functions.WifiAdmin;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity{
	public static String TAG = "MainActivity";
	TextView telephoneID,voltage,network,temperature,batteryStatus,batteryLevel,signalStrength_view;
	public static TextView progressbar_text;
	TelephonyManager telephonyManager;
	SignalStrengthListener signalListener;
	int dbm_result,asu_result, battery_voltage,level,scale,current_progress;
	int wifiLevel,wifiSpeed;
	String wifiSSID,wifiUnits;
	double temper;
	String battery_status;
	public static Button run_btn;
	public static ProgressBar progressbar;
	SharedPreferences mySharedPreferences;
	boolean loop_preference,web_wifi_preference,video_wifi_preference,lock_screen_preference;
	String telephone_preference,sms_preference,sms_num_preference,ringtone_preference,address_preference1,address_preference2,city_preference,state_preference,volume_preference,
	power_preference,web_preference,email_preference1,video_preference, brightness_preference;
	DatabaseManager db_man;
	public static Context context;
	PowerAdmin powerAdmin;
	AudioAdmin audioAdmin;
	WifiAdmin wifiAdmin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = this;
		telephoneID = (TextView)findViewById(R.id.textView2);
		voltage = (TextView)findViewById(R.id.textView4);
		network = (TextView)findViewById(R.id.textView6);
		temperature = (TextView)findViewById(R.id.textView8);
		batteryStatus = (TextView)findViewById(R.id.textView10);
		signalStrength_view = (TextView)findViewById(R.id.textView12);
		batteryLevel = (TextView)findViewById(R.id.textView14);
		telephonyManager = (TelephonyManager)getSystemService(MainActivity.TELEPHONY_SERVICE);
		wifiAdmin = new WifiAdmin(MainActivity.this);
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
		Log.d(TAG, "brightness: " + brightness_preference + "power: " + power_preference + "volume: " + volume_preference + "lock screen: " + lock_screen_preference);
		setBrightness(brightness_preference);
		setPower(power_preference);
		setVolume(volume_preference);
		if(lock_screen_preference){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			Log.d(TAG, "screen will keep on");
		}else{
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
			Log.d(TAG, "allow screen locked");
		}
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
		loadPref();
		registerReceiver(batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)); 
		telephonyManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		setBrightness(brightness_preference);
		setVolume(volume_preference);
		if(lock_screen_preference){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			Log.d(TAG, "screen will keep on");
		}else{
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
			Log.d(TAG, "allow screen locked");
		}
	}
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		loadPref();
		telephonyManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		setBrightness(brightness_preference);
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
	        
	        //	int wifiLevel,wifiSpeed; String wifiSSID,wifiUnits;
	        wifiLevel = wifiAdmin.getWifiStrength();
	        wifiSpeed = wifiAdmin.getWifiSpeed();
	        wifiUnits = wifiAdmin.getWifiUnits();
	        wifiSSID  = wifiAdmin.getWifiSSID();
	        network.setText(wifiSSID + " Level:" + wifiLevel + " " + wifiSpeed + " " +wifiUnits);
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
		serviceIntent.putExtra("smsNUM", sms_num_preference);
		serviceIntent.putExtra("ringtone", ringtone_preference);
		serviceIntent.putExtra("address1", address_preference1);
		serviceIntent.putExtra("address2", address_preference2);
		serviceIntent.putExtra("city", city_preference);
		serviceIntent.putExtra("state", state_preference);
//		serviceIntent.putExtra("volumeLevel", volume_preference);
//		serviceIntent.putExtra("brightness", brightness_preference);
//		serviceIntent.putExtra("powerMode", power_preference);
		serviceIntent.putExtra("webBrowser", web_preference);
		serviceIntent.putExtra("switchWifi", web_wifi_preference);
		serviceIntent.putExtra("emailAddress", email_preference1);
		serviceIntent.putExtra("videoId", video_preference);
		serviceIntent.putExtra("videoWifi", video_wifi_preference);
		if(loop_preference){
			int loop = 0;
			do {
				this.startService(serviceIntent);
				loop++;
				Log.d(TAG, "Loop time: " + loop);
			}while(loop<1000);
		}else{
			this.startService(serviceIntent);
			Log.d(TAG, "no Loop");
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
			
			loadPref();
		}
	
	private void loadPref(){
		mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		
		loop_preference = mySharedPreferences.getBoolean("Checkbox1", false);
		telephone_preference = mySharedPreferences.getString("EditTextPreference1", "");
		sms_preference = mySharedPreferences.getString("EditTextPreference2", "");
		sms_num_preference = mySharedPreferences.getString("ListPreference2", "1");
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
		brightness_preference = mySharedPreferences.getString("ListPreference4", "0.25");
		power_preference = mySharedPreferences.getString("ListPreference5", "1");
		
		//web address and wifi use switch
		web_preference = mySharedPreferences.getString("EditTextPreference3", "www.swipedevelopment.com");
		web_wifi_preference = mySharedPreferences.getBoolean("SwitchPreference2", false);
		//youtube video id
		video_preference = mySharedPreferences.getString("ListPreference6", "dKi5XoeTN0k");
		video_wifi_preference = mySharedPreferences.getBoolean("SwitchPreference3", false);
		//lock screen
		lock_screen_preference = mySharedPreferences.getBoolean("SwitchPreference4", false);
		Log.d(TAG, "sms num " + sms_num_preference);
		Log.d(TAG, "loop: " + loop_preference + "wifi_preference: " + web_wifi_preference + "video_wifi: " + video_wifi_preference +"lock_screen_preference: " + lock_screen_preference);
	}
	private void setBrightness(String arg1){
		float value = Float.valueOf(arg1);
		System.out.println("value " + value);
		int brightnessValue = (int) (value*255);
		android.provider.Settings.System.putInt(getContentResolver(),
				android.provider.Settings.System. SCREEN_BRIGHTNESS, brightnessValue);
		Log.d(TAG, "setBrightness: " + brightnessValue);
	}
	private void setPower(String arg1){
		
		int mode = Integer.parseInt(arg1);
		powerAdmin = new PowerAdmin(MainActivity.this);
		powerAdmin.powerOption(mode);
		Log.d(TAG, "setPower: " + mode);
	}
	private void setVolume(String arg1){
		int level = Integer.parseInt(arg1);
		audioAdmin = new AudioAdmin(MainActivity.this);
		audioAdmin.volumeOptionForSystem(level);
		Log.d(TAG, "audio system setting");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		int mode = Integer.parseInt(power_preference);
		powerAdmin.releasePower(mode);
		Log.d(TAG, "release power" + mode);
	}
	
}
