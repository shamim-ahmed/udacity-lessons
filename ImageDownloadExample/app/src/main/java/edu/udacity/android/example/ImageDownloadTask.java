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
    private final ImageDownloadApplication application;

    public ImageDownloadTask(ImageView imageView, ImageDownloadApplication application) {
        this.imageView = imageView;
        this.application = application;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params.length < 1) {
            return null;
        }

        String imageLocation = params[0];
        Bitmap bitmap = application.getBitmapFromMemoryCache(imageLocation);

        if (bitmap != null) {
            return bitmap;
        }

        InputStream inputStream = null;

        try {
            URL url = new URL(imageLocation);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            application.addBitmapToMemoryCache(imageLocation, bitmap);
        } catch (IOException ex) {
            Log.e(TAG, String.format("Error while downloading image %s", imageLocation));
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
