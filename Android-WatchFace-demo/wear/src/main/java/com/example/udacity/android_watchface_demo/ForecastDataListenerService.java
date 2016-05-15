package com.example.udacity.android_watchface_demo;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shamim on 5/15/16.
 */
public class ForecastDataListenerService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";
    private static final String FORECAST_PATH = "/forecast";

    private static final String SUMMARY_KEY = "summary";
    private static final String TEMPERATURE_KEY = "temperature";
    private static final String HUMIDITY_KEY = "humidity";

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        Log.i(TAG, "ForecastDataListenerService initialized successfully");
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "onConnected(): Successfully connected to Google API client");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "onConnectionSuspended(): Connection to Google API client was suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed(): Failed to connect, with result: " + result);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.i(TAG, "onDataChanged() invoked ");

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (FORECAST_PATH.equals(path)) {
                    Log.i(TAG, "Data Changed for FORECAST_PATH");
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    String forecastData = dataMapItem.getDataMap().get("forecast");
                    Log.i(TAG, "forecast received : " + forecastData);

                    Map<String, String> forecastMap = parseForecastData(forecastData);
                    Log.i(TAG, String.format("%s : %s", SUMMARY_KEY, forecastMap.get(SUMMARY_KEY)));
                    Log.i(TAG, String.format("%s : %s", TEMPERATURE_KEY, forecastMap.get(TEMPERATURE_KEY)));
                    Log.i(TAG, String.format("%s : %s", HUMIDITY_KEY, forecastMap.get(HUMIDITY_KEY)));

                    Asset asset = dataMapItem.getDataMap().getAsset("icon");
                    Log.i(TAG, "the asset received is : " + asset);
                } else {
                    Log.i(TAG, "Unrecognized path: " + path);
                }
            } else {
                Log.i(TAG, "event type : " + event.getType());
            }
        }
    }

    private Map<String, String> parseForecastData(String inputStr) {
        Map<String, String> resultMap = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(inputStr);

            resultMap.put(SUMMARY_KEY, jsonObject.getString(SUMMARY_KEY));
            resultMap.put(TEMPERATURE_KEY, jsonObject.getString(TEMPERATURE_KEY));
            resultMap.put(HUMIDITY_KEY, jsonObject.getString(HUMIDITY_KEY));
        } catch (Exception ex) {
            Log.e(TAG, "error while parsing json string");
        }

        return resultMap;
    }
}
