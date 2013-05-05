package com.swipedevelopment.functions;

import com.swipedevelopment.phonetester.R;

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class VideoRecorderAdmin extends Activity implements Callback{
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	public MediaRecorder mediaRecorder;
	public Camera camera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_recorder);
		surfaceView = (SurfaceView)findViewById(R.id.videorecorder);
		camera = Camera.open();
		
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
//		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
