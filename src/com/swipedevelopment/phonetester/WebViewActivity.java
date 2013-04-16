package com.swipedevelopment.phonetester;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity{
	public final String TAG ="Webview";
	WebView webView;
	String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		webView = (WebView) findViewById(R.id.webview);
		webView.setVisibility(View.VISIBLE);
		WebSettings setting = webView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setPluginState(WebSettings.PluginState.ON);
		setting.setDomStorageEnabled(true);
		String userAgent = setting.getUserAgentString();
		setting.setUserAgentString(userAgent);
		webView.setWebChromeClient(new mWebChromeClient());
		webView.setWebViewClient(new mWebViewClient());
		url = getIntent().getStringExtra("url");
		Log.d(TAG, "url is " + url);
		webView.loadUrl(url);
	}
	
	private class mWebChromeClient extends WebChromeClient {

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			// TODO Auto-generated method stub
			super.onShowCustomView(view, callback);
		}
		
	}
	private class mWebViewClient extends WebViewClient {

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			return super.shouldOverrideUrlLoading(view, url);
		}
		
	}
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {   
	            webView.goBack();   
	            return true;   
	        }else if(keyCode == KeyEvent.KEYCODE_BACK){
	        	ConfirmExit();
	        	return true; 
	        }   
	        return super.onKeyDown(keyCode, event);   
	    }
	 public void ConfirmExit(){
	    	AlertDialog.Builder ad=new AlertDialog.Builder(WebViewActivity.this);
	    	ad.setTitle("Exit");
	    	ad.setMessage("Are you sure to exit?");
	    	ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i) {
					// TODO Auto-generated method stub
					WebViewActivity.this.finish();
				
				}
	    	});
	    	ad.setNegativeButton("No",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i) {
					
				}
			});
	    	ad.show();
	    	
	 }
}
