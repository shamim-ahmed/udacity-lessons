package edu.udacity.android.picassoexample2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieGridAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> movieList;

    public MovieGridAdapter(Context context, List<String> movieList) {
        super(context, R.layout.grid_item);
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public String getItem(int index) {
        return movieList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("shamim", "position " + position + " list " + movieList);

        if (position >= movieList.size()) {
            return null;
        }

        String movie = movieList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
        Log.i("shamim", "Am I here ??");
        Picasso.with(context).load(movie).into(imageView);

        return convertView;
    }
}
