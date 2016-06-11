package singwaichan.android.usatodayhttpsample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends ActionBarActivity {

    private TextView resultTextView;
    private ImageView resultImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = (TextView) this.findViewById(R.id.result);
        resultImageView = (ImageView) this.findViewById(R.id.imageView);

        BingAsyncTask getNewsUpdate = new BingAsyncTask();
        getNewsUpdate.execute();
    }


    public boolean onCreuateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class BingAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        //private String APILink = "https://api.datamarket.azure.com/Bing/Search/v1/";
        private String APILink = "https://api.datamarket.azure.com/Bing/Search/v1/Image?Query=%27yunus%27&Market=%27en-US%27&Adult=%27Moderate%27&ImageFilters=%27Size%3ASmall%27&$format=json&$top=1";
        private String API_KEY = "vMxn7HtSEMFKZPHMYO+ULN3YfePDenhDoPA22uULpyU";
        private String[] SECTION = {"image"};

        @Override
        protected Bitmap doInBackground(Void... params) {
            String result = "";

            try {
                URL searchUrl = new URL(APILink);
                HttpsURLConnection conn = (HttpsURLConnection) searchUrl.openConnection();
                String auth = API_KEY + ":" + API_KEY;
                String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
                conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder resultBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    resultBuilder.append(line);
                }

                result = resultBuilder.toString();
            } catch (Exception ex) {
                Log.e("MainActivity", "error while retrieving data", ex);
            }

            //Extract link from JSON
            //String to Json
            JSONObject jsonObject = null;
            if (JSONValue.isValidJson(result)) {
                jsonObject = (JSONObject) JSONValue.parse(result);
            }
            ;

            jsonObject = (JSONObject) jsonObject.get("d");
            jsonObject = (JSONObject) ((JSONArray) jsonObject.get("results")).get(0);
            jsonObject = (JSONObject) jsonObject.get("Thumbnail");
            Log.e(". ", jsonObject.toString() + " . ");
            String url = (String) jsonObject.get("MediaUrl");
            Log.e(". ", url + " . ");

            Bitmap bitmap = null;
            try {
                bitmap = downloadBitmap(url);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return bitmap;
        }

        private Bitmap downloadBitmap(String url) throws IOException {
            HttpUriRequest request = new HttpGet(url.toString());
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                        bytes.length);
                return bitmap;
            } else {
                throw new IOException("Download failed, HTTP response code "
                        + statusCode + " - " + statusLine.getReasonPhrase());
            }


        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            MainActivity.this.resultImageView.setImageBitmap(bitmap);
        }
    }
}



