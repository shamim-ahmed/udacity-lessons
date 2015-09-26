package edu.udacity.android.gridviewexample2;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String[] values =
            {"A", "B", "C", "D", "E",
                    "F", "G", "H", "I", "J",
                    "K", "L", "M", "N", "O",
                    "P", "Q", "R", "S", "T",
                    "U", "V", "W", "X", "Y", "Z"};

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        List<MovieInfo> infoList = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {
            int imageId;

            switch (i % 4) {
                case 0:
                    imageId = R.drawable.google_android0;
                    break;
                case 1:
                    imageId = R.drawable.google_android1;
                    break;
                case 2:
                    imageId = R.drawable.google_android2;
                    break;
                case 3:
                    imageId = R.drawable.google_android3;
                    break;
                default:
                    imageId = R.drawable.google_android0;
                    break;
            }

            MovieInfo info = new MovieInfo(values[i], imageId);
            infoList.add(info);
        }

        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), infoList);
        gridView.setAdapter(adapter);

        return view;
    }
}
