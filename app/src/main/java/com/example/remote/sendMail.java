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

import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;

public class sendMail {
	
	public void sendAccelerationData(String mail_body ,String to)
	{
		StrictMode.enableDefaults();
		
		/*Mail m = new Mail("backup.andapp@gmail.com", "32154603215460"); 

		Log.e("@@@@@@", to);
	      String[] toArr = {to}; 
	      m.setTo(toArr); 
	      m.setFrom("backup.andapp@gmail.com"); 
	      m.setSubject("From SMS Divert ANDROID APP"); 
	      m.setBody(mail_body); 

	      try { 
//	        m.addAttachment("/sdcard/filelocation"); 

	        if(m.send()) { 
	        } else { 
	        } 
	      } catch(Exception e) { 
	        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
	        Log.e("MailApp", "Could not send email", e); 
	      }*/
	      
	    //Add data to be send.
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		nameValuePairs.add(new BasicNameValuePair("mess",mail_body));
    	nameValuePairs.add(new BasicNameValuePair("to",to));
    	
	    HttpResponse response = null;
	    
	    try
	    {
	    	
	    	HttpClient httpclient = new DefaultHttpClient();
	    	HttpPost httppost = new HttpPost("http://officechair.ir/contact2.php");
	    	//httppost.setHeader( "Content-Type", "application/json;charset=UTF-8" );
	    	httppost.setEntity( new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));//);
	    	response = httpclient.execute(httppost);
			Log.e("postData", response.getStatusLine().toString());
	    	
	    }
	    catch(Exception e)
	    {
	    	//Toast.makeText(IncomingSms.this, response.getStatusLine().toString(), Toast.LENGTH_SHORT).show();
	    }
	    	
	}

}
