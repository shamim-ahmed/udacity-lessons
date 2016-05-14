package com.example.twittershare.androidwear_demo;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Date;

/**
 * Created by shamim on 5/11/16.
 */
public class SendDataTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = SendDataTask.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;

    public SendDataTask(GoogleApiClient pGoogleApiClient) {
        mGoogleApiClient = pGoogleApiClient;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (!mGoogleApiClient.isConnected()) {
            Log.i(TAG, "client not connected...");
            return null;
        }

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/count");
        putDataMapRequest.getDataMap().putLong("count", new Date().getTime());
        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        putDataRequest.setUrgent();

        Log.i(TAG, "data is being sent....");

        Wearable.DataApi.putDataItem(mGoogleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                Log.i(TAG, "the status is : " + dataItemResult.getStatus());
                if (!dataItemResult.getStatus().isSuccess()) {
                    Log.e(TAG, "Error while sending data !!");
                }
            }
        });

        return null;
    }
}
