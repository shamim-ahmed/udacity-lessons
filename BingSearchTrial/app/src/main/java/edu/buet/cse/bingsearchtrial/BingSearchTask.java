package edu.buet.cse.bingsearchtrial;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by shamim on 6/11/16.
 */
public class BingSearchTask extends AsyncTask<String, Void, String> {
    private static final String TAG = BingSearchTask.class.getSimpleName();
    private static final String API_KEY = "vMxn7HtSEMFKZPHMYO+ULN3YfePDenhDoPA22uULpyU";


    @Override
    protected String doInBackground(String... params) {
        if (params.length < 1) {
            Log.w(TAG, "search URI not specified");
            return null;
        }

        BufferedReader reader = null;
        StringBuilder resultBuilder = new StringBuilder();

        try {
            URL searchUrl = new URL(params[0]);
            HttpsURLConnection connection = (HttpsURLConnection) searchUrl.openConnection();

            String keyStr = String.format("%s:%s", API_KEY, API_KEY);
            String encodedKeyStr = Base64.encodeToString(keyStr.getBytes(), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", String.format("Basic %s", encodedKeyStr));

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
            }
        } catch (Exception ex) {
            Log.e(TAG, "error while performing search", ex);
        }

        return resultBuilder.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i(TAG, "the search result : " + result);
    }
}
