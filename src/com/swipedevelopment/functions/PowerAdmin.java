package com.swipedevelopment.functions;

import android.content.Context;
import android.os.PowerManager;

public class PowerAdmin {
	private PowerManager powerManager;
	private PowerManager.WakeLock wakeLock1,wakeLock2,wakeLock3;
//  PARTIAL_WAKE_LOCK: Screen off, keyboard light off  
//  SCREEN_DIM_WAKE_LOCK: screen dim, keyboard light off  
//  

	public PowerAdmin(Context context){
		powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
	}
	

    public void powerOption(int sp2Int){

       switch(sp2Int){
       case 0:
    	   System.out.println("Hey you didn't choose any thing for power option.");
    	   break;
       case 1:
	//high power
    //  FULL_WAKE_LOCK: screen bright, keyboard bright 	   
	   wakeLock1 = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, this.getClass().getCanonicalName());	
	   wakeLock1.acquire();
	   System.out.println("Hey, you are using high power!");
	   break;
       case 2:
    //medium power;
    //SCREEN_BRIGHT_WAKE_LOCK: screen bright, keyboard light off  
	   wakeLock2 = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
	   wakeLock2.acquire();
	   System.out.println("Hey, you are using Medium power!");
	   break;
       case 3:
    //low power;
    
	   wakeLock3 = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
	   wakeLock3.acquire();
	   System.out.println("Hey, you are using low power!");
	   break;

       }
    }


    public void releasePower(int sp2Int){
    	
    	switch(sp2Int){
    	case 0:
    		break;
    	case 1:
    		if(wakeLock1 != null){
    			wakeLock1.release();
    			System.out.println("power released 1");
    			break;
    		}else{
    			break;
    		}
    	case 2:
    		if(wakeLock2 != null){
    			wakeLock2.release();
    			System.out.println("power released 1");
    			break;
    		}else{
    			break;
    		}
    		
    		
    	case 3:
    		if(wakeLock3 != null){
    			wakeLock3.release();
    			System.out.println("power released 1");
    	        break;
    		}else{
    			break;
    		}	
    	}
    	
    	
        	
    }
    }