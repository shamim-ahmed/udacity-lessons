package edu.udacity.android.example;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageDownloadApplication application = (ImageDownloadApplication) getApplication();
        Resources resources = getResources();

        try (InputStream inputStream = resources.openRawResource(R.raw.config)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            String baseUrl = properties.getProperty("image.base.url");
            String imagePath = "/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
            String imageLocation = baseUrl + imagePath;

            ImageView imageView = (ImageView) findViewById(R.id.image_view);
            ImageDownloadTask task = new ImageDownloadTask(imageView, application);
            task.execute(imageLocation);
        } catch (IOException ex) {
            Log.e(TAG, "Error while rendering image", ex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
}
