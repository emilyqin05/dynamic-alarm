package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
// import the vibrate & sound modules
//import android.os.Vibrator;
//import android.media.MediaPlayer;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ALARM_MESSAGE_EXTRA = "ALARM_MESSAGE_EXTRA";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive triggered."); // Log that the receiver was hit        if (intent == null) return;

        String message = intent.getStringExtra(ALARM_MESSAGE_EXTRA);
        if (message == null) return;

        Log.d("AlarmReceiver", "Alarm triggered: " + message);
    }
}
