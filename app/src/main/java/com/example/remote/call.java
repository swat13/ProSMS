package com.example.remote;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class call extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call);
		final SharedPreferences pref = getSharedPreferences("SPY", 0);
		
		final EditText et_email = (EditText) findViewById(R.id.mail_call);
		final EditText et_call = (EditText) findViewById(R.id.call_num);
		final EditText et_active = (EditText) findViewById(R.id.active_word_call);
		
		final CheckBox ch = (CheckBox) findViewById(R.id.call_ch);
		final CheckBox ch1 = (CheckBox) findViewById(R.id.mail_ch_call);
		final CheckBox ch2 = (CheckBox) findViewById(R.id.ac_deac_word_check_call);
		
		et_email.setText(pref.getString("mail_call_ed", ""));
		et_call.setText(pref.getString("call_num_ed", ""));
		et_active.setText(pref.getString("active_call_ed", ""));
		
		ch.setChecked(pref.getBoolean("call_num_ch", false));
		ch1.setChecked(pref.getBoolean("mail_call_ch", false));
		ch2.setChecked(pref.getBoolean("active_call_ch", false));
		
		if(!ch.isChecked()) 
		{
			et_call.setEnabled(false);
			et_call.setHint("غیرفعال");
		}
		else et_call.setHint("شماره تلفن");
		
		if(!ch1.isChecked()) 
		{
			et_email.setEnabled(false);
			et_email.setHint("غیرفعال");
		}
		else et_email.setHint("ایمیل");
		
		if(!ch2.isChecked()) 
		{
			et_active.setEnabled(false);
			et_active.setHint("غیرفعال");
		}
		else et_active.setHint("کلمه کلیدی ");
		
		
		ch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(ch1.isChecked()) 
				{
					et_email.setEnabled(true);
					et_email.setHint("ایمیل");
				}
				else 
				{
					et_email.setEnabled(false);
					et_email.setHint("غیرفعال");
				}
			}
		});
		
		ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(ch.isChecked()) 
				{
					et_call.setEnabled(true);
					et_call.setHint("شماره تلفن");
				}
				else 
				{
					et_call.setEnabled(false);
					et_call.setHint("غیرفعال");
				}
			}
		});
		
		ch2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(ch2.isChecked()) 
				{
					et_active.setEnabled(true);
					et_active.setHint("کلمه کلیدی ");
				}
				else 
				{
					et_active.setEnabled(false);
					et_active.setHint("غیرفعال");
				}
			}
		});
		

		Button bt = (Button)findViewById(R.id.active_call);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pref.edit().putString("call_num_ed", et_call.getText().toString()).commit();
				pref.edit().putString("active_call_ed", et_active.getText().toString()).commit();
				pref.edit().putString("mail_call_ed", et_email.getText().toString()).commit();
				
				pref.edit().putBoolean("call_num_ch", ch.isChecked()).commit();
				pref.edit().putBoolean("mail_call_ch", ch1.isChecked()).commit();
				pref.edit().putBoolean("active_call_ch", ch2.isChecked()).commit();
				
				Toast.makeText(call.this, "تغییرات انجام شد", Toast.LENGTH_SHORT).show();
			}
		});
	}
	

}
