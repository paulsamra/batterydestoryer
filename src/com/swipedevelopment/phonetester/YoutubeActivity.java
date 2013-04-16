package com.swipedevelopment.phonetester;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	    
	}
}
