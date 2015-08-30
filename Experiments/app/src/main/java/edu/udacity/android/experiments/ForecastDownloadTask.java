package edu.udacity.android.experiments;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class ForecastDownloadTask extends AsyncTask<String, Void, String[]> {
    private static final String LOG_TAG = ForecastDownloadTask.class.getSimpleName();
    private ArrayAdapter<String> adapter;

    public ForecastDownloadTask(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
    }

    @Override
    protected String[] doInBackground(String... params) {
        String result = readForecastData(params[0], params[1]);
        Log.i(LOG_TAG, result);

        String[] forecasts = null;

        try {
            forecasts = WeatherDataParser.getWeatherDataFromJson(result, 7);
            Log.i(LOG_TAG, "the forecast data : " + Arrays.deepToString(forecasts));
        } catch (JSONException ex) {
            Log.e(LOG_TAG, "error while parsing JSON", ex);
        }
        return forecasts;
    }

    @Override
    protected void onPostExecute(String[] forecasts) {
        adapter.clear();
        adapter.addAll(Arrays.asList(forecasts));
    }

    private String readForecastData(String postCode, String country) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            Uri.Builder uriBuilder = new Uri.Builder();
            String zip = postCode + "," + country;
            uriBuilder.scheme("http").authority("api.openweathermap.org").appendPath("data").appendPath("2.5").appendPath("forecast").appendPath("daily");
            uriBuilder.appendQueryParameter("zip", zip).appendQueryParameter("mode","json").appendQueryParameter("units", "metric").appendQueryParameter("cnts", "7");

            String serviceLocation = uriBuilder.build().toString();
            Log.i(LOG_TAG, serviceLocation);

            URL url = new URL(serviceLocation);
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = builder.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return forecastJsonStr;
    }
}
