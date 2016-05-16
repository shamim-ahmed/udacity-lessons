package com.example.udacity.android_watchface_demo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.Collections;
import java.util.Map;

/**
 * Created by shamim on 5/15/16.
 */
public class CustomWatchFaceApplication extends Application {
    private static final String TAG = CustomWatchFaceApplication.class.getSimpleName();
    private Map<String, Object> forecastDataMap = Collections.emptyMap();

    public synchronized Map<String, Object> getForecastDataMap() {
        return forecastDataMap;
    }

    public synchronized void setForecastDataMap(Map<String, Object> forecastDataMap) {
        this.forecastDataMap = forecastDataMap;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "application created");
        startService(new Intent(this, DataLayerListenerService.class));
    }
}
