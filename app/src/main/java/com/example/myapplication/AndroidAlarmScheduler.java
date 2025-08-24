package com.example.myapplication;

//import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//import androidx.annotation.RequiresPermission;

import java.time.ZoneId;

public class AndroidAlarmScheduler implements AlarmScheduler {
    private final AlarmManager alarmManager;
    private final Context context;
    // constructor
    // check gdocs for context info
    public AndroidAlarmScheduler(Context context) {
        this.context = context;
        this.alarmManager = context.getSystemService(AlarmManager.class);
    }
    // TODO: Added RequiresPermission due to error, ensure that is dealing with it properly
    // Philipp says that we can change the permission which is in the AndroidMaifest.xml.
    // Google Play will approve the app because this app is meant to be an alarm app
    //@RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    @Override
    public void schedule(AlarmItem item) {
        Log.d("AlarmScheduler", "Scheduling alarm ID: " + item.getId() + "for: " + item.getTime() + " with message: " + item.getMessage());
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ALARM_MESSAGE_EXTRA", item.getMessage());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                item.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.getTime().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                pendingIntent
        );
    }

    @Override
    public void cancel(AlarmItem item) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                item.getId(),
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.cancel(pendingIntent);

    }
}


