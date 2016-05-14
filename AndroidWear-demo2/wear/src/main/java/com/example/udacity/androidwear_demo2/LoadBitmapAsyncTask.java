package com.example.udacity.androidwear_demo2;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by shamim on 5/14/16.
 */
public class LoadBitmapAsyncTask extends AsyncTask<Asset, Void, Bitmap> {
    private static final String TAG = LoadBitmapAsyncTask.class.getSimpleName();
    private final Activity activity;
    private final GoogleApiClient googleApiClient;

    public LoadBitmapAsyncTask(Activity activity, GoogleApiClient googleApiClient) {
        this.activity = activity;
        this.googleApiClient = googleApiClient;
    }

    @Override
    protected Bitmap doInBackground(Asset... params) {
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
        if (bitmap == null) {
            Log.w(TAG, "bitmap is null");
            return;
        }

        ImageView imageView = (ImageView) activity.findViewById(R.id.icon);

        if (imageView == null) {
            Log.w(TAG, "cannot find ImageView to load bitmap");
            return;
        }

        imageView.setImageBitmap(bitmap);
        Log.i(TAG, "bitmap loaded successfully");
    }
}
