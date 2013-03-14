package com.swipedevelopment.functions;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraAdmin extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder holder;
	private SurfaceView surfaceView;
	private Camera camera;
	private Camera.Parameters parameters;
    private boolean preview = false;
//    int defaultCameraId;
    
    private ShutterCallback shutter = new ShutterCallback(){

		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			
		}
    	
    };
    private PictureCallback raw = new PictureCallback(){

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    private PictureCallback jpeg = new PictureCallback(){

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			
		}
    	
    };

    public CameraAdmin(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
//		surfaceView = this;
//		this.context = context;
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		

        }

	@Override
	public void surfaceChanged(final SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		try{
		parameters = camera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
        camera.setParameters(parameters);
        camera.startPreview();
        System.out.println("check surface changed");
		}catch(Exception e){
		e.printStackTrace();	
		}
		camera.takePicture(shutter, null, null);
		
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		// TODO Auto-generated method stub
		int numberOfCameras = Camera.getNumberOfCameras();
		System.out.println("check number: " + numberOfCameras);
		CameraInfo cameraInfo = new CameraInfo();
		for(int i = 0; i< numberOfCameras; i ++){
			Camera.getCameraInfo(i, cameraInfo);
			if(cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK){
				int defaultCameraId = i;
				try{
				if(camera == null){
				camera = Camera.open(defaultCameraId);
				System.out.println("back camera: "+ camera);
				}	
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
			}else if(cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT){
				int defaultCameraId = i;
				try{
				if(camera == null){
				camera = Camera.open(defaultCameraId);
				System.out.println("front camera: " + camera);
				}	
				}catch(Exception e){
					e.printStackTrace();
				}

			}
		try{
			camera.setPreviewDisplay(holder);
			System.out.println("check preview");
		}catch(Exception e){
			e.printStackTrace();
			camera.release();
			camera = null;
			System.out.println("check preview fails");
		}
		}


	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
             camera.stopPreview();
             camera.release();
             camera = null;

		System.out.println("check camera destoryed");
	} 
    
       
    public void takePicture() { 
        // Log.e(TAG, "==takePicture=="); 
    	
    	System.out.println("check take1: " + camera);
        if (camera != null) { 
        	camera.takePicture(shutter, null, null);
        	System.out.println("check take2");
        } 
    } 
    
    public void voerTack(){
    	camera.startPreview();
    }
    public void releaseCamera(){
    	if(camera != null){
    		camera.release();
    		camera = null;
    		System.out.println("CameraAdmin: release camera.");
    	}
    }
} 
    
    
    
