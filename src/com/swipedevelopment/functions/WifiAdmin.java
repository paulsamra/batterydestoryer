package com.swipedevelopment.functions;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


public class WifiAdmin {
	public static String TAG = "WifiManager";
	private WifiManager wifiManager;
	public int numLevels = 4;  
    public WifiAdmin(Context context){        
        wifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);    
    } 
    public void openWifi(){  
        if(!wifiManager.isWifiEnabled()){  
            wifiManager.setWifiEnabled(true);  
            System.out.println("WifiAdmin: Your wifiManager is just turn on");
        } else{
        	System.out.println("WifiAdmin: Your wifiManager was already turn on");
        }
    } 
    public void closeWifi(){  
        if(wifiManager.isWifiEnabled()){  
            wifiManager.setWifiEnabled(false); 
            System.out.println("WifiAdmin: Your wifiManager is turn off");
        }  
    }     
    //get Network information;
    public int getWifiStrength(){
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();

    	if(wifiInfo.getBSSID()!= null){
    		int strength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numLevels);
    		return strength;
    	}else{
    		Log.d(TAG, "No network connected");
    		return 0;
    	}
    	
    }
    public int getWifiSpeed(){
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();

    	if(wifiInfo.getBSSID()!= null){
    		int speed = wifiInfo.getLinkSpeed();
    		return speed;
    	}else{
    		Log.d(TAG, "No network connected");
    		return 0;
    	}
    }
    public String getWifiUnits(){
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();

    	if(wifiInfo.getBSSID()!= null){
    		String units = WifiInfo.LINK_SPEED_UNITS;
    		return units;
    	}else{
    		Log.d(TAG, "No network connected");
    		return "";
    	}
    	
    }
    public String getWifiSSID(){
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();

    	if(wifiInfo.getBSSID()!= null){
    		String ssid = wifiInfo.getSSID();
    		return ssid;
    	}else{
    		Log.d(TAG, "No network connected");
    		return "";
    	}
    }
}
