package com.swipedevelopment.phonetester;


import com.swipedevelopment.functions.CameraAdmin;

import android.app.Activity;
import android.os.Bundle;

public class CameraViewer extends Activity {
	CameraAdmin cameraAdmin;
	boolean isClicked = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cameraAdmin = new CameraAdmin(this);
		setContentView(cameraAdmin);
        
		
	}
	public void action(){
		if(!isClicked){
			cameraAdmin.takePicture();
			isClicked = true;
		}else{
			cameraAdmin.voerTack();
			isClicked = false;
		}
	}
	
	
}
