package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
// import the vibrate & sound modules
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ALARM_MESSAGE_EXTRA = "ALARM_MESSAGE_EXTRA";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive triggered.");
        if (intent == null) return;

        String message = intent.getStringExtra(ALARM_MESSAGE_EXTRA);
        if (message == null) return;

        Log.d("AlarmReceiver", "Alarm triggered: " + message);

        // Play default alarm sound
        try {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                // fallback to notification sound if alarm is not set
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }

            MediaPlayer mediaPlayer = MediaPlayer.create(context, alarmUri);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true); // keep ringing
                mediaPlayer.start();
            }
        } catch (Exception e) {
            Log.e("AlarmReceiver", "Failed to play alarm sound", e);
        }

        // Vibrate the phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            VibrationEffect effect = VibrationEffect.createWaveform(
                    new long[]{0, 1000, 1000}, 0); // vibrate 1s, pause 1s, repeat
            vibrator.vibrate(effect);
        }
    }
}
