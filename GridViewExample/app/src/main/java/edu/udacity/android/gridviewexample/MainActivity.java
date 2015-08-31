package edu.udacity.android.gridviewexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private static final String[] values =
      {"A", "B", "C", "D", "E",
          "F", "G", "H", "I", "J",
          "K", "L", "M", "N", "O",
          "P", "Q", "R", "S", "T",
          "U", "V", "W", "X", "Y", "Z"};


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    GridView gridView = (GridView) findViewById(R.id.grid_view);
    List<MovieInfo> itemList = new ArrayList<>();

    for (int i = 0; i < values.length; i++) {
      String s = values[i];
      MovieInfo item = new MovieInfo();
      item.setName(s);
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

      item.setImageId(imageId);
      itemList.add(item);
    }

    ArrayAdapter<MovieInfo> arrayAdapter = new CustomArrayAdapter(this, itemList);
    gridView.setAdapter(arrayAdapter);
  }
}
