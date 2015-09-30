package edu.udacity.android.experiments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Forecast> {

    public CustomAdapter(Context context) {
        super(context, R.layout.list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Forecast forecast = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView dayView = (TextView) convertView.findViewById(R.id.forecast_day);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.forecast_description);
        TextView maxTempView = (TextView) convertView.findViewById(R.id.forecast_max_temp);
        TextView minTempView = (TextView) convertView.findViewById(R.id.forecast_min_temp);

        dayView.setText(forecast.getDay());
        descriptionView.setText(forecast.getDescription());
        maxTempView.setText(Double.toString(forecast.getMaximumTemparature()));
        minTempView.setText(Double.toString(forecast.getMinimumTemparature()));

        return convertView;
    }
}
