package com.example.remote;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AutoStart extends BroadcastReceiver
{   
	final SmsManager sms = SmsManager.getDefault();
    //Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {   
    	/*context.startService(new Intent(context, OutgoingSMSReceiver.class));*/
    	SharedPreferences values = context.getSharedPreferences("SPY", 0);
    	TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getSimSerialNumber();
		if(values.getBoolean("sim_ch", false) && !mPhoneNumber.equals(values.getString("sim", "")))
		{
			sms.sendTextMessage("09"+values.getString("sim_ed", ""), null, "Sim Card Change", null, null);
		}
    }
}