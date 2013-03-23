package com.swipedevelopment.service;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastMaker {

	public static void makeToast(String message,Context context)
	{
		Toast toast = Toast.makeText(context,message,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
}
