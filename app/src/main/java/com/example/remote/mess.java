package com.example.remote;


import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class mess extends Activity {
	
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mess);
		pref  = getSharedPreferences("SPY", 0);
		
		final EditText et_divert = (EditText) findViewById(R.id.num);
		final EditText et_spec = (EditText) findViewById(R.id.spec_num);
		final EditText et_active = (EditText) findViewById(R.id.active_word_mess);
		final EditText et_email = (EditText) findViewById(R.id.mail_mess);
		final EditText et_send_mess = (EditText) findViewById(R.id.sms_send);
		
		final CheckBox ch = (CheckBox) findViewById(R.id.divert_ch);
		final CheckBox ch2 = (CheckBox) findViewById(R.id.spec_ch);
		final CheckBox ch3 = (CheckBox) findViewById(R.id.ac_deac_word_check_mess);
		final CheckBox ch4 = (CheckBox) findViewById(R.id.send_mess_word_check);
		final CheckBox ch5 = (CheckBox) findViewById(R.id.mail_ch_mess);
		
		et_divert.setText(pref.getString("divert_num_ed", ""));
		et_spec.setText(pref.getString("spec_num_ed", ""));
		et_active.setText(pref.getString("active_mess_ed", ""));
		et_send_mess.setText(pref.getString("send_mess_ed", ""));
		et_email.setText(pref.getString("mail_mess_ed", ""));
		
		ch.setChecked(pref.getBoolean("divert_num_ch", false));
		ch2.setChecked(pref.getBoolean("spec_num_ch", false));
		ch3.setChecked(pref.getBoolean("active_mess_ch", false));
		ch4.setChecked(pref.getBoolean("send_mess_ch", false));
		ch5.setChecked(pref.getBoolean("mail_mess_ch", false));
		
		if(!ch.isChecked()) 
		{
			et_divert.setEnabled(false);
			et_divert.setHint("غیرفعال");
		}
		else et_divert.setHint("شماره تلفن");
		
		if(!ch2.isChecked()) 
		{
			et_spec.setEnabled(false);
			et_spec.setHint("غیرفعال");
		}
		else et_spec.setHint("شماره تلفن");
		
		if(!ch3.isChecked()) 
		{
			et_active.setEnabled(false);
			et_active.setHint("غیرفعال");
		}
		else et_active.setHint("کلمه کلیدی");
		
		if(!ch4.isChecked()) 
		{
			et_send_mess.setEnabled(false);
			et_send_mess.setHint("غیرفعال");
		}
		else et_send_mess.setHint("کلمه کلیدی");
		
		if(!ch5.isChecked()) 
		{
			et_email.setEnabled(false);
			et_email.setHint("غیرفعال");
		}
		else et_email.setHint("ایمیل");
		
		
		ch5.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(ch5.isChecked()) 
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
					et_divert.setEnabled(true);
					et_divert.setHint("شماره تلفن");
				}
				else 
				{
					et_divert.setEnabled(false);
					et_divert.setHint("غیرفعال");
				}
			}
		});
		
		ch2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(ch2.isChecked()) 
				{
					et_spec.setEnabled(true);
					et_spec.setHint("شماره تلفن");
				}
				else 
				{
					et_spec.setEnabled(false);
					et_spec.setHint("غیرفعال");
				}
			}
		});
		
		ch3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(ch3.isChecked()) 
				{
					et_active.setEnabled(true);
					et_active.setHint("کلمه کلیدی");
				}
				else 
				{
					et_active.setEnabled(false);
					et_active.setHint("غیرفعال");
				}
			}
		});
		
		
		ch4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(ch4.isChecked()) 
				{
					et_send_mess.setEnabled(true);
					et_send_mess.setHint("کلمه کلیدی");
				}
				else 
				{
					et_send_mess.setEnabled(false);
					et_send_mess.setHint("غیرفعال");
				}
			}
		});
		
		Button bt = (Button)findViewById(R.id.active_mess);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pref.edit().putString("divert_num_ed", et_divert.getText().toString()).commit();
				pref.edit().putString("spec_num_ed", et_spec.getText().toString()).commit();
				pref.edit().putString("active_mess_ed", et_active.getText().toString()).commit();
				pref.edit().putString("send_mess_ed", et_send_mess.getText().toString()).commit();
				pref.edit().putString("mail_mess_ed", et_email.getText().toString()).commit();
				
				pref.edit().putBoolean("divert_num_ch", ch.isChecked()).commit();
				pref.edit().putBoolean("spec_num_ch", ch2.isChecked()).commit();
				pref.edit().putBoolean("active_mess_ch", ch3.isChecked()).commit();
				pref.edit().putBoolean("send_mess_ch", ch4.isChecked()).commit();
				pref.edit().putBoolean("mail_mess_ch", ch5.isChecked()).commit();
				
				Toast.makeText(mess.this, "تغییرات انجام شد", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
