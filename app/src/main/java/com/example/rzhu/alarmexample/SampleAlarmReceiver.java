package com.example.rzhu.alarmexample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class SampleAlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "SampleAlarmReceiver";
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;

    private static final String ACTION = "SEND_LOCATION";
    private static final int UNIQUE_ID = 1234;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppController.isActivityVisible()) {
            Log.e(TAG, "onReceive in foreground, send location to server");
            Intent service = new Intent(context, SampleSchedulingService.class);
            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, service);
        } else {
            Log.e(TAG, "onReceive in background, cancel alarm");
            cancelAlarm(context);
        }
    }


    public boolean isAlarmRunning(Context context) {
        Intent intent = new Intent(context, SampleAlarmReceiver.class);
        intent.setAction(ACTION);//the same as up
        boolean isWorking = (PendingIntent.getBroadcast(context, UNIQUE_ID, intent, PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag
        Toast.makeText(context, "alarm is " + (isWorking ? "" : "not ") + "working...", Toast.LENGTH_SHORT).show();
        return isWorking;
    }

    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SampleAlarmReceiver.class);
        intent.setAction(ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, UNIQUE_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                5000,
                60 * 1000, pendingIntent);
    }

    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SampleAlarmReceiver.class);
        intent.setAction(ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, UNIQUE_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.e(TAG, "Alarm canceled");

    }
}
