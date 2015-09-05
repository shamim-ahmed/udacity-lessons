package edu.udacity.android.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import edu.udacity.android.example.util.IOUtils;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = ImageDownloadTask.class.getSimpleName();
    private final ImageView imageView;

    public ImageDownloadTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params.length < 1) {
            return null;
        }

        InputStream inputStream = null;
        Bitmap bitmap = null;

        try {
            URL url = new URL(params[0]);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException ex) {
            Log.e(TAG, "Error while downloading image");
        } finally {
            IOUtils.close(inputStream);
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
