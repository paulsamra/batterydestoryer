package com.swipedevelopment.service;



import com.swipedevelopment.functions.AudioAdmin;
import com.swipedevelopment.functions.BluetoothAdmin;
import com.swipedevelopment.functions.CameraAdmin;
import com.swipedevelopment.functions.EmailAdmin;
import com.swipedevelopment.functions.MusicAdmin;
import com.swipedevelopment.functions.NFCAdmin;
import com.swipedevelopment.functions.PowerAdmin;
import com.swipedevelopment.functions.SMSAdmin;
import com.swipedevelopment.functions.TelephonyAdmin;
import com.swipedevelopment.functions.WifiAdmin;
import com.swipedevelopment.phonetester.MainActivity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;

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
	MusicAdmin musicAdmin;
	NFCAdmin nfcAdmin;
	AudioAdmin audioAdmin;
	Thread t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11;
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
//		action();
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
		}
		
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("onCreate");
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("onDestroy");
	}

public void action(int sp1IntCom, final int sp2Int, int sp3Int, final int durationInt) {
		
		boolean canSwitch = true;
		switch(sp1IntCom){
		
		case 0:
			//None
			break;
		case 1:
			//Pause
			if(canSwitch){
				canSwitch=false;
			
				System.out.println("actionController: Pause is chosen.");
				powerAdmin.powerOption(sp2Int);
				
				//Pause
				t2 = new Thread(new Runnable(){
					
					@Override
					public  void run() {
						// TODO Auto-generated method stub
						int j = durationInt;
						System.out.println("t2 thread is " + Thread.currentThread().getId());
						System.out.println("initial pause run time = " + j);
						for(;j>= 0 ; j--){
							System.out.println("pause time left: " + j);
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
				t2.start();
				int j = durationInt;
				for(;j >= 0; j--){
	    			System.out.println("pause time left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				powerAdmin.releasePower(sp2Int);
	    				canSwitch = true;
	    			}
			    }
			break;
			}
			else{
				break;
			}
		case 2:
			//Call
			if(canSwitch){
				canSwitch = false;
				audioAdmin.volumeOptionForCall(sp3Int);
				powerAdmin.powerOption(sp2Int);
//				try {
//					Thread.sleep(2500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				try {
					teleAdmin.callPhone(telephoneNum);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("actionController: Call is chosen ");
				
		        //call
		        t1 = new Thread(new Runnable(){

					@Override
					public  void run() {
						// TODO Auto-generated method stub
						int j = durationInt;
//						System.out.println("t1 thread is " + Thread.currentThread().getId());
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

			int j = durationInt;
			for(;j >= 0; j--){
    			System.out.println("Rob's timer call time left: " + j);
    			try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			if (j==0){
    				teleAdmin.endCall();
    				powerAdmin.releasePower(sp2Int);
    				canSwitch = true;
    			}
		    }
			break;
			}else{
				break;
			}
		case 3:
			//SMS
			if(canSwitch){
				canSwitch = false;
			
				System.out.println("actionController: SMS is chosen.");
				powerAdmin.powerOption(sp2Int);
				int s = 1;
				int smsNum = (int)(durationInt/60);
				while(s < smsNum + 1){
					System.out.println("actionController: SMS is chosen");
					smsAdmin.sendSMS(telephoneNum, smsDetail);
					System.out.println("actionController: this is No. " + s + " SMS.");
				try {
					Thread.sleep(1000);
					System.out.println("actionController: SMS is done, thread sleep 1 secs");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
					s++;
				}
    			
    			powerAdmin.releasePower(sp2Int);
				break;
			}else{
				break;
			}

		case 4:
			//wifi
			if(canSwitch){
				canSwitch=false;			
				System.out.println("actionController: wifi is chosen.");
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				wifiAdmin.openWifi();
				powerAdmin.powerOption(sp2Int);

				t3 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = durationInt;
						System.out.println("t3 thread is " + Thread.currentThread().getId());
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
				int j = durationInt;
				for(;j >= 0; j--){
	    			System.out.println("wifi time left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				wifiAdmin.closeWifi();
	    				powerAdmin.releasePower(sp2Int);
	    				canSwitch = true;
	    			}
			    }
				break;
			}else{
				break;
			}
			case 5:
			//bluetooth
			if(canSwitch){
				canSwitch=false;

				System.out.println("actionController: bluetooth is chosen");
				powerAdmin.powerOption(sp2Int);
				audioAdmin.volumeOptionForMusic(sp3Int);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//BT
				t4 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = durationInt;
						System.out.println("t4 thread is " + Thread.currentThread().getId());
						System.out.println("initial BT run time = " + j);
						for(;j>= 0 ; j--){
							System.out.println("BT time left: " + j);
							Message msg = Message.obtain(handler);
							msg.what = j;
							try {
							     Thread.sleep(1000);
						    }catch (InterruptedException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
						     }
							if(j == durationInt){
								bluetoothAdmin.openBluetooth();
							}
//							else if(j == durationInt -5){
//								musicAdmin.playMusic(musicFile);
//							}
							handler.sendMessage(msg);

						}
					}
					
				});
				t4.start();
				int j = durationInt;
				for(;j >= 0; j--){
	    			System.out.println("BT time left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				musicAdmin.stopMusic();
	    				bluetoothAdmin.closeBluetooth();
	    				powerAdmin.releasePower(sp2Int);
	    				canSwitch = true;
	    			}
			    }
				break;
			}else{
				break;
			}
		case 6:
			//camera
//			if(canSwitch){
//				canSwitch=false;
//			    powerAdmin.powerOption(sp2Int);
////			    try {
////					Thread.sleep(1000);
////				} catch (InterruptedException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//				int c = 0;
//				int shotNum = (int)(durationInt/60);
//				while(c< shotNum){
//					    takePic();
//					try {
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//    				returnMain();
//    				cameraAdmin.releaseCamera();
//					try {
//						Thread.sleep(1500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//    				c++;
//    				}
//				
//    			powerAdmin.releasePower(sp2Int);
//    			
//		        canSwitch = true;
//				break;
//			}else{
//				break;
//			}
			case 7:
				//camera snapshot
				if(canSwitch){
					canSwitch=false;
//					audioAdmin.playSnapshotVoice(this);
				    powerAdmin.powerOption(sp2Int);
				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				t10 = new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int c = (durationInt + 2)*10;
						System.out.println("t10 thread snapshot is: " + Thread.currentThread().getId());
						System.out.println("initial snapshot run time is: " + c);
						for(;c >= 0; c--){
							if(c == durationInt*10 ){
								audioAdmin.stopVoice1();
							}
							snapshot();
							System.out.println("timer1 snapshot time left:" + c);
							Message msg = Message.obtain(handler);
							msg.what= c;
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handler.sendMessage(msg);
							if(c == 0){
								powerAdmin.releasePower(sp2Int);
								returnMain();
							
								}
						}
					}
					
				});
				t10.start();
				
					int c = durationInt + 3;
					for(; c >= 0; c--){
						System.out.println("timer2 snapshot left time: " + c);
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
			case 8:
				//camera recording
				if(canSwitch){
					canSwitch=false;
					
//					powerAdmin.powerOption(sp2Int);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					audioAdmin.playVideoVoice(this);
					t11 = new Thread(new Runnable(){

						@Override
						public void run() {
							
							// TODO Auto-generated method stub
							int c = durationInt + 5;
							System.out.println("t11 thread videoRecording is: " + Thread.currentThread().getId());
							System.out.println("initial videoRecording run time is: " + c);
							for(;c>= 0; c--){
								if(c == durationInt + 2){
									audioAdmin.stopVoice2();
								}else if(c == durationInt){
									videoRecording();
								}
							
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
					
					int c = durationInt + 3;
			    	for(; c >= 0; c--){
					System.out.println("timer2 videoRecording time left: " + c);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(c == 0){
//					powerAdmin.releasePower(sp2Int);
					canSwitch = true;	
					}
				}
					break;
				}else{
					break;
				}
		case 9:
			//video
			if(canSwitch){
				canSwitch=false;
				
				powerAdmin.powerOption(sp2Int);
				audioAdmin.volumeOptionForMusic(sp3Int);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				System.out.println("check videoFile " + videoFile);
//				playVideo();
//				videoAdmin.playVideo(videoFile);
				t6 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = durationInt;
						System.out.println("t6 thread is " + Thread.currentThread().getId());
						System.out.println("initial video run time = " + j);
						for(;j>= 0 ; j--){
							System.out.println("video time left: " + j);
							Message msg = Message.obtain(handler);
							msg.what = j;
							try {
							     Thread.sleep(1000);
						    }catch (InterruptedException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
						     }
							if(j == durationInt - 1){
//								playVideo();
							}
							handler.sendMessage(msg);

						}
					}
					
				});
				t6.start();
				int j = durationInt;
				for(;j >= 0; j--){
	    			System.out.println("video time left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				powerAdmin.releasePower(sp2Int);
//	    				videoAdmin.stopVideo();
	    				returnMain();
	    				canSwitch = true;
	    			}
			    }
				break;
			}else{
				break;
			}
		case 10:
			//music
			if(canSwitch){
				canSwitch=false;
			
				System.out.println("actionController: music is chosen.");
				powerAdmin.powerOption(sp2Int);
				audioAdmin.volumeOptionForMusic(sp3Int);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!musicAdmin.checkPlaying()){
//					musicAdmin.playMusic(musicFile);
					//music
					t7 = new Thread(new Runnable(){
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int j = durationInt;
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
					int j = durationInt;
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
		    				powerAdmin.releasePower(sp2Int);
		    				canSwitch = true;
		    			}
				    }
				}
			break;
			}else {
				break;
			}
			case 11:
			//GPS
				if(canSwitch){
					canSwitch=false;
			
				System.out.println("actionController: GPS is chosen");
				powerAdmin.powerOption(sp2Int);
				if(sp3Int == 4){
					wifiAdmin.openWifi();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				openGPS(sp3Int);

				t8 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int j = durationInt;
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
				int j = durationInt;
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
	    				powerAdmin.releasePower(sp2Int);
//	    				wifiAdmin.closeWifi();
	    				if(sp3Int == 4){
	    					wifiAdmin.closeWifi();
	    					returnMain();
//	    					turnGPSOnOff();
	    				}
	    				if(sp3Int == 5){
//	    					turnGPSOnOff();
	    				}
	    				canSwitch = true;
	    			}
			    }
				break;
				}
				else{
					break;
				}
		case 12:
			//website
			if(canSwitch){
				canSwitch=false;
			
				System.out.println("actionController: website is chosen");
				powerAdmin.powerOption(sp2Int);
				wifiAdmin.openWifi();
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				

				t9 = new Thread(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println("t9 thread is " + Thread.currentThread().getId());
						if(durationInt >= 10){
							System.out.println("initial website loop times = " + durationInt);
							for(int i = 0; i<durationInt ; i++){
							Message msg = Message.obtain(handler);
							msg.what = i;
							
							if(i%10 == 0){								
//								switchWeb(count);								
//								count++;
							}

							try {
							     Thread.sleep(1000);
						    }catch (InterruptedException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
						     }
							handler.sendMessage(msg);

						    }	
						}else{
							System.out.println("website time is less than 10 secs");
							int j = durationInt;
							System.out.println("actionController: switch to web01...");
//							openWebsite(web01);
							for(;j>=0;j--){
								
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
					}
					
				});
				t9.start();
				int j = durationInt + 2;
				for(;j >= 0; j--){
	    			System.out.println("Rob's timer website time left: " + j);
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			if (j==0){
	    				
	    				wifiAdmin.closeWifi();
	    				powerAdmin.releasePower(sp2Int);
	    				canSwitch = true;
	    				returnMain();
	    			}
			    }
				break;
			
		  }

		}
		
	}
public void snapshot(){
	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用照相机
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	startActivity(intent);
}
public void returnMain(){
	   Intent intent = new Intent(this, MainActivity.class);
	   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   startActivity(intent);
}
public void videoRecording(){
	Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	startActivity(intent);
}
public void openGPS(String location){
	
}
}
