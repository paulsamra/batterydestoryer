package com.swipedevelopment.functions;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class MusicAdmin {
	MediaPlayer mediaPlayer;;
	boolean isPlaying;
	public MusicAdmin(Context context){
		mediaPlayer = new MediaPlayer();
		isPlaying = mediaPlayer.isPlaying();
	}
    public boolean checkPlaying(){
        isPlaying = mediaPlayer.isPlaying();
		return isPlaying;
	}
	public void playMusic(Context context,String path){
      try {
		if(!isPlaying){	
			    Uri uri = Uri.parse(path);
				mediaPlayer.reset();
				mediaPlayer.setDataSource(context,uri);
				mediaPlayer.prepare();
				mediaPlayer.start();
				isPlaying = true;
				mediaPlayer.setLooping(true);
				System.out.println("music is playing " + "Uri: " + uri);
			} 
		else {
			System.out.println("music is already playing");
		
		}
      }catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();		
			} 
	}
	public void stopMusic(){
		System.out.println("check if music playing: " + mediaPlayer.isPlaying());
		if(isPlaying){
			mediaPlayer.stop();
			isPlaying = false;
//			mediaPlayer.release();
			System.out.println("music stop");	
		}


	}

//    class MusicFilter implements FilenameFilter {  
//		@Override
//		public boolean accept(File dir, String filename) {
//			// TODO Auto-generated method stub
//			return (filename.endsWith(".mp3"));
//		}  
//  
//    }


}
