package edu.udacity.android.picassoexample2;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class MovieDataDownloadTask extends AsyncTask<String, String, List<String>> {

    private final Context context;
    private final ListView listView;

    public MovieDataDownloadTask(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        List<String> movieList = Arrays.asList("http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png");
        return movieList;
    }

    @Override
    protected void onPostExecute(List<String> movieList) {
        listView.invalidateViews();

        MovieGridAdapter adapter = new MovieGridAdapter(context);
        adapter.addAll(movieList);
        listView.setAdapter(adapter);
    }
}
