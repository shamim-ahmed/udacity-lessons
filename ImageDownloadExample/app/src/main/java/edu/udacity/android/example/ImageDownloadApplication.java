package edu.udacity.android.example;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageDownloadApplication extends Application {
    private LruCache<String, Bitmap> imageCache;

    @Override
    public void onCreate() {
        super.onCreate();

        // image cache setup
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            public int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return imageCache.get(key);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            imageCache.put(key, bitmap);
        }
    }
}
