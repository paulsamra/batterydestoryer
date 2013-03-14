package com.swipedevelopment.functions;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

public class BluetoothAdmin {
	private BluetoothAdapter bluetoothAdapter;
	public BluetoothAdmin(Context context){
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
	}
	public void openBluetooth(){
		if(bluetoothAdapter != null){
			int state = bluetoothAdapter.getState();
			if(state == BluetoothAdapter.STATE_OFF){
				bluetoothAdapter.enable();
				System.out.println("BluetoothAdmin: Bluetooth is open");
			}
	    }else {
	    	System.out.println("This device doesn't have bluetooth");
	    }
	}
	
	public void closeBluetooth(){
		if(bluetoothAdapter != null){
			int state = bluetoothAdapter.getState();
			if(state == BluetoothAdapter.STATE_ON){
				bluetoothAdapter.disable();
				System.out.println("BluetoothAdmin: bluetooth is OFF");
			}
	    }
	}	
}
