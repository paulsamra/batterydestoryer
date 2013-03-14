package com.swipedevelopment.functions;

import android.content.Context;
import android.net.wifi.WifiManager;


public class WifiAdmin {
	private WifiManager wifiManager;
	  
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
    
}
