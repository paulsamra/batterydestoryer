package com.swipedevelopment.phonetester;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnFullscreenListener,OnInitializedListener{
	private static String TAG = "YoutubePlayer";
	private static String DEVELOP_KEY = "AIzaSyBk-X6nccLv-K7yWZgAXfvjID1gZOf3HnY";
	String VIDEO_ID;
	private YouTubePlayer player;
	private YouTubePlayerView youtubeView;
	private boolean fullscreen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youtubevideo);
		VIDEO_ID = getIntent().getStringExtra("video_id");
		//default value could be as: dKi5XoeTN0k;
		Log.d(TAG, "VIDEO_ID: " + VIDEO_ID);
		youtubeView = (YouTubePlayerView) findViewById(R.id.youtubeview);
		youtubeView.initialize(DEVELOP_KEY,this);
	} 
	
    @Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			final YouTubePlayer player, boolean wasRestored) {
		// TODO Auto-generated method stub
		this.player = player;
		player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		player.setOnFullscreenListener(this);
		player.setPlayerStateChangeListener(new PlayerStateChangeListener(){

			@Override
			public void onAdStarted() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(ErrorReason arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoaded(String arg0) {
				// TODO Auto-generated method stub
				player.play();
			}

			@Override
			public void onLoading() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onVideoEnded() {
				// TODO Auto-generated method stub
				player.play();
			}

			@Override
			public void onVideoStarted() {
				// TODO Auto-generated method stub
				
			}
			
		});
		if (!wasRestored) {
		      player.cueVideo(VIDEO_ID);
		    }
		
	}
    
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
      return youtubeView;
    }
    public void onClick(View v) {
      player.setFullscreen(!fullscreen);
    }
	@Override
	public void onFullscreen(boolean isFullscreen) {
		// TODO Auto-generated method stub
		fullscreen = isFullscreen;
	}

    
	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult result) {
		String message = "Error:\n";
		if (result.equals (YouTubeInitializationResult.NETWORK_ERROR) ) {
			message += "Unable to connect to network.  Please check your internet connection.";
		}
		else if (result.equals (YouTubeInitializationResult.SERVICE_DISABLED) ) {
			message += "It appears that the YouTube application on your device is disabled." +
					"  Please enable YouTube application to proceed with playing media.";
		}
		else if (result.equals (YouTubeInitializationResult.SERVICE_INVALID) ) {
			message += "It appears that there is an application installed on this device " +
					"(other than the YouTube application) that has the same name as the" +
					" YouTube application.  Please remove conflicting application to " +
					"proceed with playing media.";
		}
		else if (result.equals (YouTubeInitializationResult.SERVICE_MISSING) ) {
			message += "The official YouTube application was not found on this device.  " +
					"Please install the most up-to-date version of the YouTube " +
					"application to proceed with playing media.";
		}
		else if (result.equals (YouTubeInitializationResult.SERVICE_VERSION_UPDATE_REQUIRED) ) {
			message += "It appears that your YouTube application is out-of-date with the " +
					"most current version.  Please update your YouTube application to " +
					" proceed with playing media.";
		}
		else {
			message = "Failed to play media.\n\nError:\n" + result.toString();
		}
		Toast.makeText (YoutubeActivity.this, message, Toast.LENGTH_LONG).show();
		Log.e (TAG, message);
		finish();

}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	    
	}
}
