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
import com.swipedevelopment.phonetester.WebViewActivity;
import com.swipedevelopment.phonetester.YoutubeActivity;
import com.swipedevelopment.sql.DatabaseManager;
import com.swipedevelopment.sql.RowInfo;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
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
import android.widget.ProgressBar;
import android.widget.Toast;

public class MyService extends IntentService{
	private static final String TAG = "MyService";
	public static String SUBJECT ="Swipe Development Bettery Testing";
	public static String CONTENT = "Testing";
	String telephoneNum,smsDetail,smsNum,ringtone,address1,address2,city,state,webBrowser,emailAddress,videoId;
	boolean loopStatus,switchWifi, switchVideoWifi;
	boolean timer = true;
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
	Thread t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14;
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
		loopStatus = intent.getBooleanExtra("loopStatus", false);
		telephoneNum = intent.getStringExtra("telephoneNum");
		smsDetail = intent.getStringExtra("smsDetail");
		smsNum = intent.getStringExtra("smsNUM");
		ringtone = intent.getStringExtra("ringtone");
		
		address1 = intent.getStringExtra("address1");
		address2 = intent.getStringExtra("address2");
		city = intent.getStringExtra("city");
		state = intent.getStringExtra("state");
		
//		volume = intent.getStringExtra("volumeLevel");
//		brightness = intent.getStringExtra("brightness");
//		powermode = intent.getStringExtra("powerMode");
		webBrowser = intent.getStringExtra("webBrowser");
		switchWifi = intent.getBooleanExtra("switchWifi", false);
		videoId = intent.getStringExtra("videoId");
		switchVideoWifi = intent.getBooleanExtra("videoWifi", false);
		emailAddress =intent.getStringExtra("emailAddress");
 		Log.d(TAG, "loopStatus " + loopStatus + " tele:" + telephoneNum + " smsDetail: " + smsDetail +
 				" ringtone "+ ringtone + " location "+ address1 + " " + address2 + " " + city + " " + state + "Web " +webBrowser + "wifiSwitch " + switchWifi + " email " + emailAddress + "videoId " + videoId +"videoWifi: " + switchVideoWifi);
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
//				System.out.println("action()");
				
			}
		}
		c.close();
		db_man.close();
		updateProgressBar(true);
	}

	static Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			Log.d("HANDLER", msg.getData().getString("toast"));
			
			Toast.makeText(MainActivity.context,msg.getData().getString("toast"), 
						Toast.LENGTH_LONG).show();
//			if(msg.getData().getString("toast").contains("End of Testing")){
//				MainActivity.run_btn.setClickable(true);
//				Log.d(TAG, "set Run Btn clickable");
//			}else{
//				MainActivity.run_btn.setClickable(false);
//				Log.d(TAG, "set Run Btn non-clickable");
//			}
			if(msg.getData().getString("toast").contains("SMS")){
				MainActivity.progressbar_text.setText("Current Progress: SMS");
			}else if(msg.getData().getString("toast").contains("Wifi")){
				MainActivity.progressbar_text.setText("Current Progress: Wifi");
			}else if(msg.getData().getString("toast").contains("Bluetooth")){
				MainActivity.progressbar_text.setText("Current Progress: Bluetooth");
			}else if(msg.getData().getString("toast").contains("Ringtone")){
				MainActivity.progressbar_text.setText("Current Progress: Ringtone");
			}else if(msg.getData().getString("toast").contains("GPS")){
				MainActivity.progressbar_text.setText("Current Progress: GPS");
			}else if(msg.getData().getString("toast").contains("Voice Recorder")){
				MainActivity.progressbar_text.setText("Current Progress: Voice Recorder");
			}else if(msg.getData().getString("toast").contains("Phone Call")){
				MainActivity.progressbar_text.setText("Current Progress: Phone Call");
			}else if(msg.getData().getString("toast").contains("Email")){
				MainActivity.progressbar_text.setText("Current Progress: Email");
			}else if(msg.getData().getString("toast").contains("Web")){
				MainActivity.progressbar_text.setText("Current Progress: Web Browser");
			}else if(msg.getData().getString("toast").contains("NFC")){
				MainActivity.progressbar_text.setText("Current Progress: NFC");
			}else if(msg.getData().getString("toast").contains("Camera")){
				MainActivity.progressbar_text.setText("Current Progress: Camera");
			}else if(msg.getData().getString("toast").contains("Video Recording")){
				MainActivity.progressbar_text.setText("Current Progress: Video Recording");
			}else if(msg.getData().getString("toast").contains("Youtube")){
				MainActivity.progressbar_text.setText("Current Progress: Youtube Player");
			}
		}
		
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//registerObserver((Observer) getApplicationContext());
		Toast.makeText(getApplicationContext(), "start...", Toast.LENGTH_SHORT).show();
		teleAdmin = new TelephonyAdmin(this);
		smsAdmin = new SMSAdmin(this);
		wifiAdmin = new WifiAdmin(this);
		bluetoothAdmin = new BluetoothAdmin(this);
		cameraAdmin = new CameraAdmin(this);
		musicAdmin = new MusicAdmin(this);
		recorderAdmin = new RecorderAdmin();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
				createMessage("Phone Call is starting...");
			
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

							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
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
			createMessage("Phone Call has ended...");
			break;
			}else{
				createMessage("Phone Call has ended...");
				break;
			}
		case 2:
			//SMS
//			if(canSwitch){
//				canSwitch = false;
//				createMessage("SMS is starting...");
//
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						
//						int numSMSSent = 0;
//						while(timer) {
//							try {
//								smsAdmin.sendSMS(telephoneNum, smsDetail);
////								Log.d(TAG, "msg");
//								try {
//									Thread.sleep(1000);
//								} catch (InterruptedException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//								numSMSSent++;
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//						System.out.println("Num SMS sent: " + numSMSSent);
//						timer = true;
//					}
//				}).start();
//				
//				int j = sp2Int*60;
//				for(;j >= 0; j--){
//					System.out.println("SMS time left: " + j + " " + timer);
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					updateProgressBar(false);
//				}
//				timer = false;
//			
//				createMessage("SMS has ended...");
//				break;
//			}else{
//				break;
//			}
			if(canSwitch){
				createMessage("SMS is staring...");
//				updateProgressBar(true);
				
				canSwitch = false;
				System.out.println("actionController: SMS is chosen.");
				int s = 1;
				Log.d(TAG, "smsNUM " + smsNum);
				int smsNumInt = Integer.parseInt(smsNum);
				while(s < smsNumInt + 1){
					System.out.println("actionController: SMS is chosen");
					
					smsAdmin.sendSMS(telephoneNum, smsDetail);
					System.out.println("actionController: this is No. " + s + " SMS.");
				try {
					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				updateProgressBar(false);
					s++;
				}
				createMessage("SMS has ended...");
				break;
			}else{
				createMessage("SMS has ended...");
				break;
			}

		case 3:
			//wifi
			if(canSwitch){
				canSwitch=false;
				createMessage("Wifi is starting...");
							
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
							try {
							     Thread.sleep(1000);
						    }catch (InterruptedException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
						     }

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
	    			updateProgressBar(false);
	    			if (j==0){
	    				wifiAdmin.closeWifi();
	    				canSwitch = true;
	    			}
			    }
				createMessage("Wifi has ended...");

				break;
			}else{
				createMessage("Wifi has ended...");

				break;
			}
			case 4:
			//bluetooth
			if(canSwitch){
				canSwitch=false;
				createMessage("Bluetooth is starting...");
				
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

			    			try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

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
	    			updateProgressBar(false);
	    			if (j==0){

	    				bluetoothAdmin.closeBluetooth();
	    				canSwitch = true;
	    			}
			    }
				createMessage("Bluetooth has ended...");
				break;
			}else{
				createMessage("Bluetooth has ended...");
				break;
			}
		case 5:
			//camera snapshot
			if(canSwitch){
				createMessage("Camera is starting...");
//				updateProgressBar(true);
				
				canSwitch=false;
				t5 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = sp2Int*30 - 1;
						System.out.println("camera run time = " + j);
						for(;j>= 0 ; j--){
							System.out.println("camera time1 left: " + j);
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


						}
					}
					
				});
				t5.start();
				int j = sp2Int*60;
				for(;j >= 0; j--){
	    			System.out.println("camera time2 left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				returnMain();
	    				canSwitch = true;
	    				System.out.println("camera closed");
	    			}
			    }
				createMessage("Camera has ended...");

				break;
			}else{
				createMessage("Camera has ended...");

				break;
			}

			case 6:
				//camera recorder
				if(canSwitch){
					canSwitch=false;
					createMessage("Video Recoding is starting...");
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					t6 = new Thread(new Runnable(){

						@Override
						public void run() {
							
							// TODO Auto-generated method stub
							int c = sp2Int*60;
							System.out.println("initial videoRecording run time is: " + c);
							for(;c>= 0; c--){
								
							System.out.println("video recording time left: " + c);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(c == sp2Int*60-2){
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
					createMessage("Video Recoding has ended...");
					break;
				}else{
					createMessage("Video Recoding has ended...");
					break;
				}
		case 7:
			//ringtone
			if(canSwitch){
				canSwitch=false;
				createMessage("Ringtone is starting...");
//				updateProgressBar(true);
		
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
								try {
								     Thread.sleep(1000);
							    }catch (InterruptedException e) {
								    // TODO Auto-generated catch block
								    e.printStackTrace();
							     }

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
		    			updateProgressBar(false);
		    			if (j==0){
		    				musicAdmin.stopMusic();
		    				canSwitch = true;
		    			}
				    }
				}
				createMessage("Ringtone has ended...");
				break;
			}else {
				createMessage("Ringtone has ended...");
				break;
			}
			case 8:
			//GPS
				if(canSwitch){
					canSwitch=false;
					createMessage("GPS is starting...");

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
							
							try {
							     Thread.sleep(1000);
						    }catch (InterruptedException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
						     }

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
					createMessage("GPS has ended...");
					break;
				}
				else{
					createMessage("GPS has ended...");
					break;
				}
		case 9:
			//web browser
			if(canSwitch){
				canSwitch=false;	
				createMessage("Web Browser is starting...");

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

							try {
							     Thread.sleep(1000);
						    }catch (InterruptedException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
						     }

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
				createMessage("Web Browser has ended...");

				break;
			
		  }else{
				createMessage("Web Browser has ended...");

				break;
			}
		case 10:
			//email
			if(canSwitch){
				canSwitch = false;
				createMessage("Email is starting...");
				
//				updateProgressBar(true);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						EmailAdmin emailAdmin = new EmailAdmin(
								"swipedevelopmentteam@gmail.com", "radyschool");
						int numEmailSent = 0;
						while(timer) {
							try {
								emailAdmin.sendMail(SUBJECT, CONTENT, emailAddress);
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
					updateProgressBar(false);
				}
				timer = false;
			
				createMessage("Email has ended...");
				break;
			}else{
				break;
			}
		case 11:
			//NFC
			if(canSwitch){
				createMessage("NFC is starting...");
				
				createMessage("NFC has ended...");
				break;
			}else{
				createMessage("NFC has ended...");
				break;
			}
		case 12:
			//3D
			if(canSwitch){
				createMessage("3D Graphics is starting...");
				
				createMessage("3D Graphics has ended...");
				break;
			}else{
				createMessage("3D Graphics has ended...");
				break;
			}
		case 13: 
			//voice recorder
//			if(canSwitch){
//				createMessage("Voice Recorder is starting...");
//				canSwitch=false;
//				System.out.println("actionController:Voice recorder is chosen.");
//				recorderAdmin.startRecording();	
////				updateProgressBar(true);	
//				
//					t13 = new Thread(new Runnable(){
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							int j = sp2Int*60;
//							System.out.println("initial music run time = " + j);
//							for(;j>= 0 ; j--){
//								System.out.println("recorder time left: " + j);
//								try {
//								     Thread.sleep(1000);
//							    }catch (InterruptedException e) {
//								    // TODO Auto-generated catch block
//								    e.printStackTrace();
//							     }
//
//							}
//						}
//						
//					});
//					t13.start();
//					int j = sp2Int*60;
//					for(;j >= 0; j--){
//		    			System.out.println("recorder time left: " + j);
//		    			try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							
//						}
//		    			updateProgressBar(false);
//		    			if (j==0){
//		    				try {
//								recorderAdmin.stopRecording();
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//		    				canSwitch = true;
//		    			}
//				    }
//					createMessage("Voice Recorder has ended...");
//					break;
//			}else{
//				createMessage("Voice Recorder has ended...");
//				break;
//			}
			break;
		case 14:
		//Youtube VideoPlayer
			if(canSwitch){
				canSwitch=false;
				createMessage("Youtube VideoPlayer is starting...");
				try {
				     Thread.sleep(1000);
			    }catch (InterruptedException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
			     }
				System.out.println("actionController:Youtube VideoPlayer is chosen.");
				if(switchVideoWifi)	{
					wifiAdmin.openWifi();
					Log.d(TAG, "Video wifi in on..");
				}
				youtubeViewPlay(videoId);	
					t14 = new Thread(new Runnable(){
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int j = sp2Int*60;
							System.out.println("initial videoPlayer run time = " + j);
							for(;j>= 0 ; j--){
								System.out.println("videoPlayer time left: " + j);
								try {
								     Thread.sleep(1000);
							    }catch (InterruptedException e) {
								    // TODO Auto-generated catch block
								    e.printStackTrace();
							     }

							}
						}
						
					});
					t14.start();
					int j = sp2Int*60;
					for(;j >= 0; j--){
		    			System.out.println("videoPlayer time left: " + j);
		    			try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			if (j==0){
		    				if(switchVideoWifi){
		    					wifiAdmin.closeWifi();
		    				}
		    				returnMain();
		    				canSwitch = true;
		    			}
				    }
					createMessage("Youtube VideoPlayer has ended...");
					break;
			}else{
				createMessage("Youtube VideoPlayer has ended...");
				break;
			}	
		}
	}
	private void youtubeViewPlay(String video_id){
		Intent youtubeIntent = new Intent(getApplicationContext(), YoutubeActivity.class);
		youtubeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		youtubeIntent.putExtra("video_id", video_id);
		this.startActivity(youtubeIntent);
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
		intent.setClass(this, WebViewActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("url", url);
		this.startActivity(intent);
	}
	private void returnMain(){
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	
/********** For updating Progress Bar******************/	
	void updateProgressBar(final boolean end) {
		MainActivity.progressbar.post(new Runnable() {
			@Override
			public void run() {
				int curr_prog = MainActivity.progressbar.getProgress();
				MainActivity.progressbar.setVisibility(ProgressBar.VISIBLE);
				MainActivity.progressbar.setProgress(++curr_prog);
				System.out.println("updateProgressing");
				if(end) {
					MainActivity.progressbar.setProgress(0);
					createMessage("End of Testing...");
					MainActivity.progressbar_text.setText("Current Progress: None");
				}
			}
		});
	}

}
