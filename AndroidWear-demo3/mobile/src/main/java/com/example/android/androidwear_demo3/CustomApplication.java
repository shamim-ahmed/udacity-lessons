package com.example.android.androidwear_demo3;

import android.app.Application;
import android.content.Intent;

/**
 * Created by shamim on 5/28/16.
 */
public class CustomApplication extends Application {

    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, DataLayerListenerService.class));
    }
}
