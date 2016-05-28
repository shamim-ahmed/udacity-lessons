package com.example.android.androidwear_demo3;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Date;

/**
 * Created by shamim on 5/28/16.
 */
public class SendDataTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = SendDataTask.class.getSimpleName();

    private final GoogleApiClient googleApiClient;

    public SendDataTask(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (!googleApiClient.isConnected()) {
            Log.i(TAG, "google api client not connected");
            return null;
        }

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/get-forecast-data");
        DataMap dataMap = putDataMapRequest.getDataMap();

        dataMap.putInt("requestCode", 123456);
        dataMap.putLong("timestamp", new Date().getTime());

        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();

        Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                Log.i(TAG, "the status is : " + dataItemResult.getStatus());

                if (!dataItemResult.getStatus().isSuccess()) {
                    Log.e(TAG, "Error while sending data !");
                }
            }
        });

        return null;
    }
}
