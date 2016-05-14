/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.udacity.androidwear_demo2;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The main activity with a view pager, containing three pages:<p/> <ul> <li> Page 1: shows a list
 * of DataItems received from the phone application </li> <li> Page 2: shows the photo that is sent
 * from the phone application </li> <li> Page 3: includes two buttons to show the connected phone
 * and watch devices </li> </ul>
 */
public class MainActivity extends Activity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        DataApi.DataListener {

    private static final String TAG = "MainActivity";
    private static final String FORECAST_PATH = "/forecast";

    private static final String SUMMARY_KEY = "summary";
    private static final String TEMPERATURE_KEY = "temperature";
    private static final String HUMIDITY_KEY = "humidity";

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        if ((mGoogleApiClient != null) && mGoogleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        super.onPause();
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

                    LoadBitmapAsyncTask loadImageTask = new LoadBitmapAsyncTask(this, mGoogleApiClient);
                    loadImageTask.execute(asset);
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