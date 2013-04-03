package com.swipedevelopment.service;

import java.util.ArrayList;

import com.swipedevelopment.email.EmailAdmin;
import com.swipedevelopment.functions.AudioAdmin;
import com.swipedevelopment.functions.BluetoothAdmin;
import com.swipedevelopment.functions.CameraAdmin;
import com.swipedevelopment.functions.MusicAdmin;
import com.swipedevelopment.functions.NFCAdmin;
import com.swipedevelopment.functions.PowerAdmin;
import com.swipedevelopment.functions.RecorderAdmin;
import com.swipedevelopment.functions.SMSAdmin;
import com.swipedevelopment.functions.TelephonyAdmin;
import com.swipedevelopment.functions.WifiAdmin;
import com.swipedevelopment.phonetester.CameraViewer;
import com.swipedevelopment.phonetester.MainActivity;
import com.swipedevelopment.sql.DatabaseManager;
import com.swipedevelopment.sql.RowInfo;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class MyService extends IntentService{
	private static final String TAG = "MyService";
	String telephoneNum,smsDetail,smsNum,ringtone,address1,address2,city,state,volume,brightness,powermode,webBrowser,emailAddress,emailDetails;
	boolean loopStatus,switchWifi,timer = true;
	TelephonyAdmin teleAdmin;
	SMSAdmin smsAdmin;
	WifiAdmin wifiAdmin;
	PowerAdmin powerAdmin;
	BluetoothAdmin bluetoothAdmin;
	CameraAdmin cameraAdmin;
	EmailAdmin emailAdmin;
	NFCAdmin nfcAdmin;
	AudioAdmin audioAdmin;
	MusicAdmin musicAdmin;
	RecorderAdmin recorderAdmin;
	Thread t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13;
	DatabaseManager db_man;
	int sp1Int,sp2Int,checkInt;
	static Context context;

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
		loopStatus = intent.getBooleanExtra("loopStatus", false);
		telephoneNum = intent.getStringExtra("telephoneNum");
		smsDetail = intent.getStringExtra("smsDetail");
		smsNum = intent.getStringExtra("smsNumber");
		ringtone = intent.getStringExtra("ringtone");
		
		address1 = intent.getStringExtra("address1");
		address2 = intent.getStringExtra("address2");
		city = intent.getStringExtra("city");
		state = intent.getStringExtra("state");
		
		volume = intent.getStringExtra("volumeLevel");
		brightness = intent.getStringExtra("brightness");
		powermode = intent.getStringExtra("powerMode");
		webBrowser = intent.getStringExtra("webBrowser");
		switchWifi = intent.getBooleanExtra("switchWifi", false);
		
		emailAddress =intent.getStringExtra("emailAddress");
		emailDetails = intent.getStringExtra("emailDetails");
		context = getApplicationContext();
 		Log.d(TAG, "loopStatus " + loopStatus + " tele:" + telephoneNum + " smsDetail: " + smsDetail + " number " + smsNum +
 				" ringtone "+ ringtone + " location "+ address1 + " " + address2 + " " + city + " " + state + " volume " + volume + " brightness " + brightness +" power "+ powermode
 				+ "Web " +webBrowser + "wifiSwitch " + switchWifi + " email " + emailAddress + emailDetails);
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
//				System.out.println(sp1Int);
			}
		}
	}

	static Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			Log.d("HANDLER", msg.getData().getString("toast"));
			
			Toast.makeText(context,msg.getData().getString("toast"), 
							Toast.LENGTH_LONG).show();
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
		musicAdmin = new MusicAdmin(this);
//		emailAdmin = new EmailAdmin(this);
//		nfcAdmin = new NFCAdmin(this);
		recorderAdmin = new RecorderAdmin();
		
		powerOptions(powermode);
		audioOptions(volume);
		brightnessOptions(brightness);
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

				break;
			}else{
				break;
			}

		case 3:
			//wifi
			if(canSwitch){
				canSwitch=false;			
				System.out.println("actionController: wifi is chosen.");
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
	    				canSwitch = true;
	    			}
			    }
				break;
			}else{
				break;
			}
		case 5:
			//camera snapshot
			if(canSwitch){
				canSwitch=false;
				t5 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = sp2Int*30;
						System.out.println("camera run time = " + j);
						for(;j>= 0 ; j--){
							System.out.println("camera time left: " + j);
							Message msg = Message.obtain(handler);
							msg.what = j;
							takePic();
			    			try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    			returnMain();
			    			cameraAdmin.releaseCamera();
			    			try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handler.sendMessage(msg);

						}
					}
					
				});
				t5.start();
				int j = sp2Int*60;
				for(;j >= 0; j--){
	    			System.out.println("camera time left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				returnMain();
	    				canSwitch = true;
	    			}
			    }
				break;
			}else{
				break;
			}

			case 6:
				//camera recorder
				if(canSwitch){
					canSwitch=false;	
					t6 = new Thread(new Runnable(){

						@Override
						public void run() {
							
							// TODO Auto-generated method stub
							int c = sp2Int*60;
							System.out.println("initial videoRecording run time is: " + c);
							for(;c>= 0; c--){
								
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
							if(c == sp2Int*60-1){
								videoRecording();
							}else if(c == 0){
								returnMain();
							}
							}
						}
						
					});
					t6.start();
					
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
					canSwitch = true;	
					}
				}
					break;
				}else{
					break;
				}
		case 7:
			//ringtone
			if(canSwitch){
				canSwitch=false;
				System.out.println("actionController: ringtone is chosen.");
				if(!musicAdmin.checkPlaying()){
					musicAdmin.playMusic(this, ringtone);
					
					t7 = new Thread(new Runnable(){
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int j = sp2Int*60;
							System.out.println("t7 thread is " + Thread.currentThread().getId());
							System.out.println("initial music run time = " + j);
							for(;j>= 0 ; j--){
								System.out.println("Wei's timer music time left: " + j);
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
					t7.start();
					int j = sp2Int*60;
					for(;j >= 0; j--){
		    			System.out.println("time left: " + j);
		    			try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			if (j==0){
		    				musicAdmin.stopMusic();
		    				canSwitch = true;
		    			}
				    }
				}
			break;
			}else {
				break;
			}
			case 8:
			//GPS
				if(canSwitch){
					canSwitch=false;
			
				System.out.println("actionController: GPS is chosen");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				turnGPSOnOff();
				openGPS(address1,address2,city,state);

				t8 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = sp2Int*60;
						System.out.println("t8 thread is " + Thread.currentThread().getId());
						System.out.println("initial gps run time = " + j);
						for(;j>= 0 ; j--){
							System.out.println("gps time left: " + j);
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
				t8.start();
				int j = sp2Int*60;
				for(;j >= 0; j--){
	    			System.out.println("gps time left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				//gps close
	    				returnMain();
	    				turnGPSOnOff();
	    				canSwitch = true;
	    			}
			    }
				break;
				}
				else{
					break;
				}
		case 9:
			//web browser
			if(canSwitch){
				canSwitch=false;
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}			
				System.out.println("actionController: website is chosen");
				if(switchWifi){
					wifiAdmin.openWifi();	
					openWebBrowser(webBrowser);
				}else{
					openWebBrowser(webBrowser);
				}
				t9 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = sp2Int*60;
						System.out.println("t9 thread is " + Thread.currentThread().getId());
						
							System.out.println("initial website loop times = " + j);
							for(; j>=0; j--){
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
				t9.start();
				int j = sp2Int*60;
				for(;j >= 0; j--){
	    			System.out.println("website time left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				if(switchWifi){
	    					wifiAdmin.closeWifi();	
	    					canSwitch = true;
	    					returnMain();
	    				}else{
	    					canSwitch = true;
	    					returnMain();
	    				}
	    					
	    			}
			    }
				break;
			
		  }else{
				break;
			}
		case 10:
			createMessage("Email is starting...");
			{ 
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						EmailAdmin emailAdmin = new EmailAdmin(
								"mudvainian@gmail.com", "My090591");
						int numEmailSent = 0;
						while(timer) {
							try {
								
								emailAdmin.sendMail("Battery Destroyer", "Test","r4castil@gmail.com");
								numEmailSent++;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						System.out.println("Num emails sent: " + numEmailSent);
						timer = true;
					}
				}).start();
				
				int j = sp2Int*60;
				for(;j >= 0; j--){
					System.out.println("email time left: " + j + " " + timer);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				timer = false;
			}
			createMessage("Email has ended...");
			break;
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
		case 13: 
			//voice recorder
			if(canSwitch){
				canSwitch=false;
				System.out.println("actionController:Voice recorder is chosen.");
				recorderAdmin.startRecording();	
					
					t13 = new Thread(new Runnable(){
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int j = sp2Int*60;
							System.out.println("initial music run time = " + j);
							for(;j>= 0 ; j--){
								System.out.println("recorder time left: " + j);
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
					t13.start();
					int j = sp2Int*60;
					for(;j >= 0; j--){
		    			System.out.println("recorder time left: " + j);
		    			try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			if (j==0){
		    				recorderAdmin.stopRecording();
		    				canSwitch = true;
		    			}
				    }
				
				break;
			}else{
				break;
			}
		}
		
	}

	private void createMessage(String messageToDisplay) {
		Message message = new Message();
		Bundle b = new Bundle();
		b.putString("toast", messageToDisplay);
		message.setData(b);
		handler.sendMessage(message);
	}

	private void openWebBrowser(String address){
		String url = "http://"+ address;
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("android.intent.action.VIEW");
		Uri uri = Uri.parse(url);
		intent.setData(uri);
		startActivity(intent);
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
	private void openGPS(String obj1, String obj2,String obj3,String obj4){
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + obj1 + "+" + obj2 + "+" + obj3+ "+" + obj4 ));
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
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
	public void turnGPSOnOff(){
	    Intent poke = new Intent();
	    poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	    poke.addCategory("android.intent.category.ALTERNATIVE");
	    poke.setData(Uri.parse("custom:3")); 
	    try {  
	        PendingIntent.getBroadcast(this, 0, poke, 0).send();  
	    } catch (CanceledException e) {  
	        e.printStackTrace();  
	    }  
	    
	    System.out.println("timeservice: GPS on or off");

 }
//	private void voiceRecording(){
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.setType(ContentType.AUDIO_AMR); //String AUDIO_AMR = "audio/amr";
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setClassName("com.android.soundrecorder",
//		"com.android.soundrecorder.SoundRecorder");
//		startActivityForResult(intent, requestCode);
//	}
	private void brightnessOptions(String brightness){
		
	}
}
