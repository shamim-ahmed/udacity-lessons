package edu.udacity.android.picassoexample2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MovieDataDownloadTask extends AsyncTask<String, String, List<String>> {

    private final Context context;
    private final GridView gridView;

    public MovieDataDownloadTask(Context context, GridView gridView) {
        this.context = context;
        this.gridView = gridView;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        List<String> movieList = Arrays.asList("http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png");
        return movieList;
    }

    @Override
    protected void onPostExecute(List<String> movieList) {
        gridView.invalidateViews();

        MovieGridAdapter adapter = new MovieGridAdapter(context);
        gridView.setAdapter(adapter);
        adapter.addAll(movieList);
    }
}
