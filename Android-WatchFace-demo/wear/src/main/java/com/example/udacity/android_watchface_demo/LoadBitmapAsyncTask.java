package com.example.udacity.android_watchface_demo;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by shamim on 5/14/16.
 */
public class LoadBitmapAsyncTask extends AsyncTask<Asset, Void, Bitmap> {
    private static final String TAG = LoadBitmapAsyncTask.class.getSimpleName();
    private final CustomWatchFaceApplication application;
    private final GoogleApiClient googleApiClient;

    public LoadBitmapAsyncTask(CustomWatchFaceApplication application, GoogleApiClient googleApiClient) {
        this.application = application;
        this.googleApiClient = googleApiClient;
    }

    @Override
    protected Bitmap doInBackground(Asset... params) {
        if (!googleApiClient.isConnected()) {
            Log.w(TAG, "google api client not connected");
            return null;
        }

        if (params.length < 1) {
            Log.w(TAG, "no asset provided");
            return null;
        }

        Asset asset = params[0];
        InputStream inStream = Wearable.DataApi.getFdForAsset(googleApiClient, asset).await().getInputStream();

        if (inStream == null) {
            Log.w(TAG, "cannot retrieve inputStream for asset");
            return null;
        }

        return BitmapFactory.decodeStream(inStream);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.i(TAG, "storing the received bitmap...");
        application.setIconBitMap(bitmap);
    }
}
