package com.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


/**
 * Created by malik on 2/21/2015.
 */
public class SmsReceiver extends BroadcastReceiver {

    Context context;
    private String TAG = SmsReceiver.class.getSimpleName();

    public SmsReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        try {

        Bundle bundle = intent.getExtras();
        this.context = context;
        SmsMessage[] msgs = null;

        String str = "";

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i = 0; i < msgs.length; i++) {

                //Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getMessageBody().toString();
            }

            if(str.contains(":")) {
                Intent intentBroad = new Intent("contact_receiver");
                intentBroad.putExtra("code", str.substring(str.indexOf(":")+1));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intentBroad);
            }
        }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}