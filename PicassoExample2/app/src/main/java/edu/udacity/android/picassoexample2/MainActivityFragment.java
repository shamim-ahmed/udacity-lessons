package edu.udacity.android.picassoexample2;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.movie_grid);

        List<String> movieList = Arrays.asList("http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png");
        gridView.setAdapter(new MovieGridAdapter(getActivity().getApplicationContext(), movieList));

        return view;
    }
}
