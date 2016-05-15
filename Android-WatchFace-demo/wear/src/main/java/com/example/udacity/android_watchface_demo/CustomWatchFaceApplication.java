package com.example.udacity.android_watchface_demo;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by shamim on 5/15/16.
 */
public class CustomWatchFaceApplication extends Application {
    private static final String TAG = CustomWatchFaceApplication.class.getSimpleName();

    private Bitmap iconBitmap;

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public void setIconBitMap(Bitmap iconBitMap) {
        this.iconBitmap = iconBitMap;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "application created");
        startService(new Intent(this, DataLayerListenerService.class));
    }
}
