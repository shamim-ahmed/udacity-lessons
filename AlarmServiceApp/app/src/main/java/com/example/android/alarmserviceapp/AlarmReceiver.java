package com.example.android.alarmserviceapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by shamim on 5/26/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.example.android.alarmserviceapp.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, CustomService.class);
        context.startService(service);
    }
}
