package com.swipedevelopment.functions;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class AudioAdmin {
	private AudioManager audioManager;
	MediaPlayer mediaPlayer,mediaPlayer2;
	public AudioAdmin(Context context){
		audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	}
	
//	public void playSnapshotVoice(Context context){
////		if(mediaPlayer == null){
//			mediaPlayer = MediaPlayer.create(context, R.raw.beep2);
//		    mediaPlayer.setLooping(false);
//		    if(mediaPlayer != null){
//		    	mediaPlayer.stop();
//		    }
//			try {
//				mediaPlayer.prepare();
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    mediaPlayer.start();	
//		    System.out.println("beep2 is playing");
////		}
//
//		  
//	}

	public void stopVoice1(){
		if(mediaPlayer != null){
			mediaPlayer.stop();
			mediaPlayer.release();
			System.out.println("voice1 stop");
		}
	}

	public void volumeOptionForMusic(int audioLevel){
		switch(audioLevel){
		case 0:
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(1), AudioManager.FLAG_SHOW_UI);
			System.out.println("Hey you choose low volume audio option.");
			break;

		case 1:
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(4), AudioManager.FLAG_SHOW_UI);
			System.out.println("Hey you choose medium volume audio option.");
			break;
	    case 2: 
	    	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
	    	System.out.println("Hey you choose high volume audio option.");
	    	break;
			}
	}
	
	
	
	public void volumeOptionForCall(int sp3Int){
		switch(sp3Int){
		case 0:
			audioManager.getMode();
			
			break;
	    case 1: 
	    	audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FLAG_SHOW_UI);
	    	System.out.println("Hey you choose high volume call option.");
	    	break;
		case 2:
			audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 3, AudioManager.FLAG_SHOW_UI);
			System.out.println("Hey you choose medium volume call option.");
			break;
		case 3:
			audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 1, AudioManager.FLAG_SHOW_UI);
			System.out.println("Hey you choose low volume call option.");
			break;
		case 6:
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			audioManager.setSpeakerphoneOn(true);
			System.out.println("Hey you choose speaker on call option.");
			break;
		case 7:
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			audioManager.setSpeakerphoneOn(false);
			System.out.println("Hey you choose speaker off call option.");
			break;
			}
	}
}
