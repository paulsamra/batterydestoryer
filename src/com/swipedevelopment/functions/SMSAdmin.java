package com.swipedevelopment.functions;

import android.content.Context;
import android.telephony.SmsManager;

public class SMSAdmin {
	private SmsManager smsManager;
	public SMSAdmin(Context context){
		smsManager = SmsManager.getDefault();	
	}
	public void sendSMS(String phoneNumber,String details){
		String smsNumber="sms:" + phoneNumber;
		String smsText= "Block Your Phone: " + details;
		try{
			smsManager.sendTextMessage(smsNumber, null, smsText, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("sendSMS: " + smsText);
	 }
}
