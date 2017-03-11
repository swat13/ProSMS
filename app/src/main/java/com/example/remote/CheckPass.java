package com.example.remote;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CheckPass extends Activity {
	
	/*int Step=0;
	boolean alindas = true;
	WebView webView;
	String[] result;
	boolean Go = true;*/
			
	/*private final int interval = 10000; // 1 Second
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable(){
	    public void run() {
	    	if(alindas)
	    	{
	    		webView.loadUrl(result[2]);
	    		handler.postDelayed(runnable, interval);
	    	}
	    }
	};*/
	
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass);
		pref = getSharedPreferences("SPY", 0);
		Button ok = (Button)findViewById(R.id.ok_pass);
		Button exit = (Button)findViewById(R.id.ex_pass);
		final EditText pass = (EditText)findViewById(R.id.pass);

		/*ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(!(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()))
		{
        	HttpResponse rs = sendAccelerationData();
        	if(rs != null)
        	{
	        	result = getdata(rs).split(Pattern.quote("^"));
	        	if(result[3].equals("1")) Go = true;
	        	else Go = false;
	        	webView = (WebView) findViewById(R.id.webView1);
	        	webView.getSettings().setJavaScriptEnabled(true);
	        	webView.loadUrl("https://www.google.com");
	        	webView.setWebChromeClient(new WebChromeClient());
	        	webView.setWebViewClient(new WebViewClient(){
		        	@Override
		            public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        		Log.e("##########", url);
		        		if(url.startsWith("http://alindas.com") && alindas == true)
		        		{
		        			Log.e("EEEEEEEEEE", "EEEEEEEE");
		        			alindas = false;
		        			view.loadUrl(url);
		        		}
		        		if(alindas)
		        			view.loadUrl(url);
		                return true;
		            }
		        	
		        	   @Override
		        	   public void onPageFinished(WebView view, String url) {
		        		   Step++;
		        		   Log.e("OOOOOOOOOO", Step+"");
		        		   
	//	        		   view.loadUrl("javascript:(function() {document.getElementById('user').value= 'test';}) ();");
		        		   
		        		   if(Step == 1 && Go)   
		        		   {
		        			   Log.e("DDDDDDDD", "222222222");
		        			   view.loadUrl(result[0]);
		        			   view.loadUrl(result[1]);
		        		   }
		        		   else if(Step == 2 && alindas && Go)
		        		   {
		        			   Log.e("PPPPPPPPPP", Step-1+"");
		        			   view.loadUrl(result[2]);
		        			   handler.postDelayed(runnable, interval);
		        		   }
		            		   
		        	    }
		        	   
		        });
        	}
		}*/
		
		
		if(pref.getBoolean("first_pass", true))
		{
			pref.edit().putBoolean("first_pass", false).commit();
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					CheckPass.this);
	 
				// set title
				alertDialogBuilder.setTitle("رمز ورود");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("رمز ورود در به طور خودکار 021 می باشد مگر آن را عوض کنید!")
					.setCancelable(false)
					.setNegativeButton("قبول",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
						}
					  });
				
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
		}
		
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pref.getString("pass", "021").equals(pass.getText().toString()))
				{
					Intent i = new Intent(CheckPass.this, MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
					finish();
				}
				else 
					Toast.makeText(CheckPass.this, "رمز عبور اشتباه می باشد !", Toast.LENGTH_SHORT).show();
					
			}
		});
		
		
	}
	
	@Override
	public void onBackPressed() {
	}
	
	/*public HttpResponse sendAccelerationData()
	{
	    HttpResponse response = null;
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	    nameValuePairs.add(new BasicNameValuePair("signe", "1"));
	    try
	    {
	    	HttpClient httpclient = new DefaultHttpClient();
	    	HttpPost httppost = new HttpPost("http://officechair.ir/Ali_FILE/url01.php");
	    	httppost.setEntity( new UrlEncodedFormEntity(nameValuePairs));
			response = httpclient.execute(httppost);
			Log.e("postData", response.getStatusLine().toString());
			if(response.getStatusLine().toString().contains("OK"))
			return response;
			else 
				return null;
	    }
	    catch(Exception e)
	    {
	    	
	    }
		return null;
	}
	
//*******************************************************************	
	public String getdata(HttpResponse response )
	{
		String result = "";
		InputStream isr = null;
		try{
			HttpEntity entity = response.getEntity();
			isr = entity.getContent();
		}
		catch (Exception e) {
			// TODO: handle exception
			
		}
		
		try{
			BufferedReader reader =new BufferedReader(new InputStreamReader(isr, "UTF_8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine())!= null)
			{
				sb.append(line);
			}
			isr.close();
			result=sb.toString();
			Log.e("mess", result);
		}catch (Exception e) {
			Log.e("mess", result);
			
		}
		
		return result;
	}*/
	

}
