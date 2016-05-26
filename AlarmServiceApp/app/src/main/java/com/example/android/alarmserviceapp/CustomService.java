package com.example.android.alarmserviceapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shamim on 5/26/16.
 */
public class CustomService extends IntentService {
    private static final String TAG = CustomService.class.getSimpleName();

    public CustomService() {
        super(CustomService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "the intent has been received !!");
    }
}
