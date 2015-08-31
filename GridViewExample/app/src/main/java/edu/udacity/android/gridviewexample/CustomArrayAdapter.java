package edu.udacity.android.gridviewexample;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<MovieInfo> {
  private final Context context;
  private final List<MovieInfo> items;

  public CustomArrayAdapter(Context context, List<MovieInfo> items) {
    super(context, R.layout.item_layout);
    this.context = context;
    this.items = items;
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public MovieInfo getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    MovieInfo item = items.get(position);

    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
    }

    ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
    TextView textView = (TextView) convertView.findViewById(R.id.text_view);
    Resources resources = context.getResources();

    imageView.setImageDrawable(resources.getDrawable(item.getImageId()));
    textView.setText(item.getName());
    return convertView;
  }
}
