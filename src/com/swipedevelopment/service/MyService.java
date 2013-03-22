package com.swipedevelopment.service;

import java.util.ArrayList;

import com.swipedevelopment.email.EmailAdmin;
import com.swipedevelopment.functions.AudioAdmin;
import com.swipedevelopment.functions.BluetoothAdmin;
import com.swipedevelopment.functions.CameraAdmin;
import com.swipedevelopment.functions.MusicAdmin;
import com.swipedevelopment.functions.NFCAdmin;
import com.swipedevelopment.functions.PowerAdmin;
import com.swipedevelopment.functions.SMSAdmin;
import com.swipedevelopment.functions.TelephonyAdmin;
import com.swipedevelopment.functions.WifiAdmin;
import com.swipedevelopment.phonetester.CameraViewer;
import com.swipedevelopment.phonetester.MainActivity;
import com.swipedevelopment.sql.DatabaseManager;
import com.swipedevelopment.sql.RowInfo;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class MyService extends IntentService{
	private static final String TAG = "MyService";
	String telephoneNum,smsDetail,smsNum,ringtone,location,volume,brightness,powermode;
	boolean loopStatus;
	TelephonyAdmin teleAdmin;
	SMSAdmin smsAdmin;
	WifiAdmin wifiAdmin;
	PowerAdmin powerAdmin;
	BluetoothAdmin bluetoothAdmin;
	CameraAdmin cameraAdmin;
	EmailAdmin emailAdmin;
	NFCAdmin nfcAdmin;
	AudioAdmin audioAdmin;
	Thread t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12;
	DatabaseManager db_man;
	int sp1Int,sp2Int,checkInt;

	private ArrayList<RowInfo> row_state = new ArrayList<RowInfo>();
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public MyService(String myService) {
		super(myService);
		// TODO Auto-generated constructor stub
	}
    public MyService(){
    	super(TAG);
    }
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("onHandleIntent");
		
		loopStatus = intent.getBooleanExtra("loopStatus", false);
		telephoneNum = intent.getStringExtra("telephoneNum");
		smsDetail = intent.getStringExtra("smsDetail");
		smsNum = intent.getStringExtra("smsNumber");
		ringtone = intent.getStringExtra("ringtone");
		location = intent.getStringExtra("location");
		volume = intent.getStringExtra("volumeLevel");
		brightness = intent.getStringExtra("brightness");
		powermode = intent.getStringExtra("powerMode");
		
 		Log.d(TAG, "loopStatus " + loopStatus + " tele:" + telephoneNum + " smsDetail: " + smsDetail + " number " + smsNum +
 				" ringtone "+ ringtone + " location "+ location + " volume " + volume + " brightness " + brightness +" power "+ powermode);
		db_man = new DatabaseManager(this);
		Cursor c = db_man.getTestFunctions();
		for(int i = 0; c.moveToNext(); i++){
			row_state.add(new RowInfo());
			RowInfo ri = row_state.get(i);
			ri.setApp(c.getInt(1));
			ri.setDuration(c.getInt(2));
			ri.setChecked(c.getInt(3) == 1);
			Log.d("CURSOR MAIN INFO", c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
			int sp1Int = Integer.parseInt(c.getString(1));
			int sp2Int = Integer.parseInt(c.getString(2));
			int checkInt = Integer.parseInt(c.getString(3));
			if(checkInt == 1){
				action(sp1Int, sp2Int);
				System.out.println(sp1Int);
			}
		}
	}

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
//			Toast.makeText(getApplicationContext(), "handler..", Toast.LENGTH_SHORT).show();
		}
		
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Toast.makeText(getApplicationContext(), "start...", Toast.LENGTH_SHORT).show();
		teleAdmin = new TelephonyAdmin(this);
		smsAdmin = new SMSAdmin(this);
		wifiAdmin = new WifiAdmin(this);
		bluetoothAdmin = new BluetoothAdmin(this);
		cameraAdmin = new CameraAdmin(this);
//		emailAdmin = new EmailAdmin(this);
//		nfcAdmin = new NFCAdmin(this);
		
		
		powerOptions(powermode);
		audioOptions(volume);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		powerRelease(powermode);
	}

public void action(final int sp1Int, final int sp2Int) {
		
		boolean canSwitch = true;
		switch(sp1Int){
		
		case 0:
			//None
			break;
		case 1:
			//Call
			if(canSwitch){
				
				Toast.makeText(getApplicationContext(), "call starts...", Toast.LENGTH_SHORT).show();
				canSwitch = false;
				try {
					teleAdmin.callPhone(telephoneNum);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		        t1 = new Thread(new Runnable(){

					@Override
					public  void run() {
						// TODO Auto-generated method stub
						int j = sp2Int *60;
						System.out.println("initial call run time = " + j);
						for(; j>=0;j--){
							System.out.println("call time left: " + j);
							Message msg = Message.obtain(handler);
							
							msg.what = j;
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handler.sendMessage(msg);
						}
					}
		        	
		        });
		        t1.start();

			int j = sp2Int*60;
			for(;j >= 0; j--){
    			try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			if (j==0){
    				teleAdmin.endCall();
    				Toast.makeText(getApplicationContext(), "call ends...", Toast.LENGTH_SHORT).show();
    				canSwitch = true;
    			}
		    }
			break;
			}else{
				break;
			}
		case 2:
			//SMS
			if(canSwitch){
				canSwitch = false;
			
				System.out.println("actionController: SMS is chosen.");
				int s = 1;
				int smsNumInt = Integer.parseInt(smsNum);
				while(s < smsNumInt + 1){
					System.out.println("actionController: SMS is chosen");
					Toast.makeText(getApplicationContext(), "Messages are sending...", Toast.LENGTH_SHORT).show();
					smsAdmin.sendSMS(telephoneNum, smsDetail);
					System.out.println("actionController: this is No. " + s + " SMS.");
				try {
					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
					s++;
				}

				Toast.makeText(getApplicationContext(), "SMS is done...", Toast.LENGTH_SHORT).show();

				break;
			}else{
				break;
			}

		case 3:
			//wifi
			if(canSwitch){
				canSwitch=false;			
				System.out.println("actionController: wifi is chosen.");

				Toast.makeText(getApplicationContext(), "Wifi is on...", Toast.LENGTH_SHORT).show();

				wifiAdmin.openWifi();
				t3 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = sp2Int*60;
						System.out.println("initial wifi run time = " + j);
						for(;j>= 0 ; j--){
							System.out.println("wifi time left: " + j);
							Message msg = Message.obtain(handler);
							msg.what = j;
							try {
							     Thread.sleep(1000);
						    }catch (InterruptedException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
						     }
							handler.sendMessage(msg);

						}
					}
					
				});
				t3.start();
				int j = sp2Int*60;
				for(;j >= 0; j--){
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				wifiAdmin.closeWifi();

	    				Toast.makeText(this.getApplication(), "Wifi is off...", Toast.LENGTH_SHORT).show();
	    				canSwitch = true;
	    			}
			    }
				break;
			}else{
				break;
			}
			case 4:
			//bluetooth
			if(canSwitch){
				canSwitch=false;
				System.out.println("actionController: bluetooth is chosen");
				Toast.makeText(getApplicationContext(), "Bluetooth is on...", Toast.LENGTH_SHORT).show();

				bluetoothAdmin.openBluetooth();
				t4 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = sp2Int*60;
						System.out.println("initial BT run time = " + j);
						for(;j>= 0 ; j--){
							System.out.println("BT time left: " + j);
							Message msg = Message.obtain(handler);
							msg.what = j;
			    			try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							}
							handler.sendMessage(msg);

						}
					}
					
				});
				t4.start();
				int j = sp2Int*60;
				for(;j >= 0; j--){
	    			System.out.println("BT time left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){

	    				bluetoothAdmin.closeBluetooth();
	    				Toast.makeText(getApplicationContext(), "Bluetooth is off...", Toast.LENGTH_SHORT).show();
//	    				powerAdmin.releasePower(sp2Int);
	    				canSwitch = true;
	    			}
			    }
				break;
			}else{
				break;
			}
		case 5:
			//camera
			if(canSwitch){
				canSwitch=false;
//			    powerAdmin.powerOption(sp2Int);
				Toast.makeText(getApplicationContext(), "Camera is on...", Toast.LENGTH_SHORT).show();
			    try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int c = 0;
				int shotNum = (int)(sp2Int/60);
				while(c< shotNum){
					    takePic();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				returnMain();
    				cameraAdmin.releaseCamera();
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				c++;
    				}
				Toast.makeText(getApplicationContext(), "Camera is off...", Toast.LENGTH_SHORT).show();
//    			powerAdmin.releasePower(sp2Int);
    			
		        canSwitch = true;
				break;
			}else{
				break;
			}

			case 6:
				//camera recorder
				if(canSwitch){
					canSwitch=false;
					Toast.makeText(getApplicationContext(), "Video recorder is on...", Toast.LENGTH_SHORT).show();
//					powerAdmin.powerOption(sp2Int);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					t11 = new Thread(new Runnable(){

						@Override
						public void run() {
							
							// TODO Auto-generated method stub
							int c = sp2Int*60;
							System.out.println("initial videoRecording run time is: " + c);
							for(;c>= 0; c--){
								videoRecording();
							System.out.println("video recording time left: " + c);
							Message msg = Message.obtain(handler);
							msg.what = c;
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handler.sendMessage(msg);
							if(c == 0){
								
								returnMain();
								
							}
							}
							
						}
						
					});
					t11.start();
					
					int c = sp2Int*60;
			    	for(; c >= 0; c--){
					System.out.println("videoRecording time left: " + c);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(c == 0){
//					powerAdmin.releasePower(sp2Int);
					Toast.makeText(getApplicationContext(), "Video recorder is off...", Toast.LENGTH_SHORT).show();
					canSwitch = true;	
					}
				}
					break;
				}else{
					break;
				}
		case 7:
			//ringtone
//			if(canSwitch){
//				canSwitch=false;
//			
//				System.out.println("actionController: ringtone is chosen.");
////				powerAdmin.powerOption(sp2Int);
////				audioAdmin.volumeOptionForMusic(sp3Int);
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if(!musicAdmin.checkPlaying()){
////					musicAdmin.playMusic(musicFile);
//					//music
//					t7 = new Thread(new Runnable(){
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							int j = durationInt;
//							System.out.println("t7 thread is " + Thread.currentThread().getId());
//							System.out.println("initial music run time = " + j);
//							for(;j>= 0 ; j--){
//								System.out.println("Wei's timer music time left: " + j);
//								Message msg = Message.obtain(handler);
//								msg.what = j;
//								try {
//								     Thread.sleep(1000);
//							    }catch (InterruptedException e) {
//								    // TODO Auto-generated catch block
//								    e.printStackTrace();
//							     }
//								handler.sendMessage(msg);
//
//							}
//						}
//						
//					});
//					t7.start();
//					int j = durationInt;
//					for(;j >= 0; j--){
//		    			System.out.println("time left: " + j);
//		    			try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//		    			if (j==0){
//		    				musicAdmin.stopMusic();
//		    				powerAdmin.releasePower(sp2Int);
//		    				canSwitch = true;
//		    			}
//				    }
//				}
//			break;
//			}else {
//				break;
//			}
			case 8:
			//GPS
//				if(canSwitch){
//					canSwitch=false;
//			
//				System.out.println("actionController: GPS is chosen");
//				powerAdmin.powerOption(sp2Int);
//				if(sp3Int == 4){
//					wifiAdmin.openWifi();
//				}
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
////				openGPS(sp3Int);
//
//				t8 = new Thread(new Runnable(){
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						int j = durationInt;
//						System.out.println("t8 thread is " + Thread.currentThread().getId());
//						System.out.println("initial gps run time = " + j);
//						for(;j>= 0 ; j--){
//							System.out.println("gps time left: " + j);
//							Message msg = Message.obtain(handler);
//							msg.what = j;
//							
//							try {
//							     Thread.sleep(1000);
//						    }catch (InterruptedException e) {
//							    // TODO Auto-generated catch block
//							    e.printStackTrace();
//						     }
//							
//							handler.sendMessage(msg);
//
//						}
//					}
//					
//				});
//				t8.start();
//				int j = durationInt;
//				for(;j >= 0; j--){
//	    			System.out.println("gps time left: " + j);
//	    			try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//	    			if (j==0){
//	    				//gps close
//	    				powerAdmin.releasePower(sp2Int);
////	    				wifiAdmin.closeWifi();
//	    				if(sp3Int == 4){
//	    					wifiAdmin.closeWifi();
//	    					returnMain();
////	    					turnGPSOnOff();
//	    				}
//	    				if(sp3Int == 5){
////	    					turnGPSOnOff();
//	    				}
//	    				canSwitch = true;
//	    			}
//			    }
//				break;
//				}
//				else{
//					break;
//				}
		case 9:
			//web browser
//			if(canSwitch){
//				canSwitch=false;
//			
//				System.out.println("actionController: website is chosen");
//				powerAdmin.powerOption(sp2Int);
//				wifiAdmin.openWifi();
////				try {
////					Thread.sleep(1000);
////				} catch (InterruptedException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//				
//
//				t9 = new Thread(new Runnable(){
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						System.out.println("t9 thread is " + Thread.currentThread().getId());
//						if(durationInt >= 10){
//							System.out.println("initial website loop times = " + durationInt);
//							for(int i = 0; i<durationInt ; i++){
//							Message msg = Message.obtain(handler);
//							msg.what = i;
//							
//							if(i%10 == 0){								
////								switchWeb(count);								
////								count++;
//							}
//
//							try {
//							     Thread.sleep(1000);
//						    }catch (InterruptedException e) {
//							    // TODO Auto-generated catch block
//							    e.printStackTrace();
//						     }
//							handler.sendMessage(msg);
//
//						    }	
//						}else{
//							System.out.println("website time is less than 10 secs");
//							int j = durationInt;
//							System.out.println("actionController: switch to web01...");
////							openWebsite(web01);
//							for(;j>=0;j--){
//								
//								Message msg = Message.obtain(handler);
//								msg.what = j;
//								try {
//								     Thread.sleep(1000);
//							    }catch (InterruptedException e) {
//								    // TODO Auto-generated catch block
//								    e.printStackTrace();
//							     }
//								handler.sendMessage(msg);
//							}
//							
//							
//						}
//					}
//					
//				});
//				t9.start();
//				int j = durationInt + 2;
//				for(;j >= 0; j--){
//	    			System.out.println("Rob's timer website time left: " + j);
//	    			try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//	    			if (j==0){
//	    				
//	    				wifiAdmin.closeWifi();
//	    				powerAdmin.releasePower(sp2Int);
//	    				canSwitch = true;
//	    				returnMain();
//	    			}
//			    }
//				break;
//			
//		  }else{
//				break;
//			}
		case 10:
			//email
			if(canSwitch){
				Toast.makeText(getApplicationContext(), "email is sending...", Toast.LENGTH_SHORT).show();
				break;
			}else{
				break;
			}
		case 11:
			//NFC
			if(canSwitch){
				Toast.makeText(getApplicationContext(), "NFC is on...", Toast.LENGTH_SHORT).show();
				break;
			}else{
				break;
			}
		case 12:
			//3D
			if(canSwitch){
				Toast.makeText(getApplicationContext(), "3D graphic is playing...", Toast.LENGTH_SHORT).show();
				break;
			}else{
				break;
			}
		}
		
	}

	private void returnMain(){
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	private void videoRecording(){
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	private void openGPS(String location){
	
	}
	private void takePic(){
		Intent intent = new Intent(this, CameraViewer.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	//run power audio and brightness mode
	private void powerOptions(String powermode){
		powerAdmin = new PowerAdmin(this);
		try{
			int mode = Integer.parseInt(powermode);
			powerAdmin.powerOption(mode);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	private void powerRelease(String powermode){
		int mode = Integer.parseInt(powermode);
		powerAdmin.releasePower(mode);
	}
	
	
	private void audioOptions(String audioLevel){
		audioAdmin = new AudioAdmin(this);
		try{
			int vol = Integer.parseInt(audioLevel);
			audioAdmin.volumeOptionForMusic(vol);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	private void brightnessOptions(String brightness){
		
	}
}
