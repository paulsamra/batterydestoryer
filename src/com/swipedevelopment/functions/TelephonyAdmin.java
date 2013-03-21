package com.swipedevelopment.functions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class TelephonyAdmin {
	TelephonyManager telManager;
	boolean isCalling;
	ITelephony iTelephony;
	public TelephonyAdmin(Context context){
		telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		isCalling = false;
	}

	 public void getTelephony(){
	    	PhoneStateListener listener = new PhoneStateListener(){

				@Override
				public void onSignalStrengthsChanged(SignalStrength signalStrength) {
					// TODO Auto-generated method stub
					super.onSignalStrengthsChanged(signalStrength);
					telManager.listen(this, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
				    
				}

				@Override
				public void onCallStateChanged(int state, String incomingNumber) {
					// TODO Auto-generated method stub
					super.onCallStateChanged(state, incomingNumber);
					switch(state){
					case TelephonyManager.CALL_STATE_IDLE:
						//hand up
						System.out.println("callstate:hand up " + state);
						isCalling = false;
						break;
					case TelephonyManager.CALL_STATE_OFFHOOK:
						//go through;
						System.out.println("callstate:go through " + state);
						isCalling = true;
						break;
					case TelephonyManager.CALL_STATE_RINGING:
						// ringing is coming;
						System.out.println("callstate:comingcall " + state);
						isCalling = false;
						break;
					}
				}
	    		
	    	};
	    	telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	    	Class<TelephonyManager> c = TelephonyManager.class;
	    	Method getITelephonyMethod = null;
	    	try {
	            getITelephonyMethod = c.getDeclaredMethod("getITelephony",(Class[]) null);
	            getITelephonyMethod.setAccessible(true);
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        } catch (NoSuchMethodException e) {
	            e.printStackTrace();
	        }
	    	try {
	            iTelephony = (ITelephony) getITelephonyMethod.invoke(telManager,(Object[])null);
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        }
	    }
	  public void callPhone(String callphoneNumber) throws RemoteException{
		  		
				String callnumber = callphoneNumber;	
				
				if(isCalling == false){ 
					getTelephony();
					iTelephony.call(callnumber);					
					isCalling = true;
					System.out.println("TeleAdmin: Call is going through.");
				}else if(isCalling == true){
					System.out.println("TeleAdmin: Call is already go through.");
				}
	    }
	  public void reDial(String callphoneNumber) throws RemoteException{
		  String callnumber = "+1" + callphoneNumber;
		  int state = telManager.getCallState();
		  if(state == TelephonyManager.CALL_STATE_IDLE ){
			  System.out.println("redial..");
//			  getTelephony();
			  iTelephony.call(callnumber);
		  }else if(state == TelephonyManager.CALL_STATE_OFFHOOK){
			  System.out.println("a call exits..");
		  }else if(state == TelephonyManager.CALL_STATE_RINGING){
			  System.out.println("a call is ringing..");
		  }
	  }
	 public void endCall(){
		 try {
			iTelephony.endCall();
			System.out.println("Tele: end call");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 isCalling = false;
		 System.out.println("TeleAdmin: call ends");
	 }

}
