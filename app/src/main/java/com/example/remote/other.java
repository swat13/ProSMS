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

public class other extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		final SharedPreferences pref = getSharedPreferences("SPY", 0);
		
		final EditText et_sim = (EditText) findViewById(R.id.sim_ed);
		final EditText et_ring = (EditText) findViewById(R.id.ring_aloud);
		
		final CheckBox ch = (CheckBox) findViewById(R.id.sim_ch);
		
		et_sim.setText(pref.getString("sim_ed", ""));
		et_ring.setText(pref.getString("ring_ed", ""));
		
		ch.setChecked(pref.getBoolean("sim_ch", false));
		
		if(!ch.isChecked()) 
		{
			et_sim.setEnabled(false);
			et_sim.setHint("غیرفعال");
		}
		else et_sim.setHint("شماره تلفن");
		
		
		ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(ch.isChecked()) 
				{
					et_sim.setEnabled(true);
					et_sim.setHint("شماره تلفن");
				}
				else 
				{
					et_sim.setEnabled(false);
					et_sim.setHint("غیرفعال");
				}
			}
		});
		
		Button bt = (Button)findViewById(R.id.active_pos);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pref.edit().putString("sim_ed", et_sim.getText().toString()).commit();
				pref.edit().putString("ring_ed", et_ring.getText().toString()).commit();
				
				pref.edit().putBoolean("sim_ch", ch.isChecked()).commit();
				
				Toast.makeText(other.this, "تغییرات انجام شد", Toast.LENGTH_SHORT).show();
			}
		});
	}
	

}
