package com.example.rzhu.alarmexample;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;

    private static boolean activityVisible;
    private static Context mContext;


    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        Log.i(TAG,"activityResumed");
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
    }
}