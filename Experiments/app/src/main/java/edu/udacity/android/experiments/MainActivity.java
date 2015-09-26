package edu.udacity.android.experiments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_forecast);

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.linear_layout);

        ListView listView = (ListView) rootLayout.findViewById(R.id.forecast_listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, R.id.forecast_textView, new ArrayList<String>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> arp = (ArrayAdapter<String>) ((ListView) parent).getAdapter();
                String data = arp.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("FORECAST_DATA", data);
                startActivity(intent);
                //Toast.makeText(parent.getContext(), data, Toast.LENGTH_LONG).show();
            }
        });
        fetchForecastData(adapter);
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
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.refresh_settings) {
            ListView listView = (ListView) findViewById(R.id.forecast_listView);
            fetchForecastData((ArrayAdapter<String>) listView.getAdapter());
            return true;
        } else if (id == R.id.open_map_settings) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String location = preferences.getString("loction", "2122, au");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + location));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchForecastData(ArrayAdapter<String> adapter) {
        ForecastDownloadTask task = new ForecastDownloadTask(adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String[] values = location.split(",");

        if (values.length < 2) {
            task.execute(values[0], "au");
        } else {
            task.execute(values[0], values[1]);
        }
    }
}
