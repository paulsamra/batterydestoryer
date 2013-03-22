package com.swipedevelopment.functions;

import android.content.Context;
import android.os.PowerManager;

public class PowerAdmin {
	private PowerManager powerManager;
	private PowerManager.WakeLock wakeLock1,wakeLock2,wakeLock3;
 

	public PowerAdmin(Context context){
		powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
	}
	

    
	public void powerOption(int mode){

       switch(mode){
       case 0:
    //low power;
    
	   wakeLock1 = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
	   wakeLock1.acquire();
	   System.out.println("Hey, you are using low power!");
	   break;

       case 1:
    //medium power;
    //SCREEN_BRIGHT_WAKE_LOCK: screen bright, keyboard light off  
	   wakeLock2 = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
	   wakeLock2.acquire();
	   System.out.println("Hey, you are using Medium power!");
	   break;

       case 2:
	//high power
    //  FULL_WAKE_LOCK: screen bright, keyboard bright 	   
	   wakeLock2 = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, this.getClass().getCanonicalName());	
	   wakeLock2.acquire();
	   System.out.println("Hey, you are using high power!");
	   break;
       }
    }


    public void releasePower(int mode){
    	
    	switch(mode){

    	case 0:
    		if(wakeLock1 != null){
    			wakeLock1.release();
    			System.out.println("power released 1");
    			break;
    		}else{
    			break;
    		}
    	case 1:
    		if(wakeLock2 != null){
    			wakeLock2.release();
    			System.out.println("power released 2");
    			break;
    		}else{
    			break;
    		}
    		
    		
    	case 2:
    		if(wakeLock3 != null){
    			wakeLock3.release();
    			System.out.println("power released 3");
    	        break;
    		}else{
    			break;
    		}	
    	}
    	
    	
        	
    }
    }