package de.janl1.betteropelapp.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.janl1.betteropelapp.R;
import de.janl1.betteropelapp.retrofit.objects.Trip;

public class TripListArrayAdapter extends ArrayAdapter<Trip> {
    private final Context context;
    private final Trip[] values;

    public TripListArrayAdapter(Context context, Trip[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_trips, parent, false);
        TextView startDate = (TextView) rowView.findViewById(R.id.startDate);
        TextView stopDate = (TextView) rowView.findViewById(R.id.stopDate);
        TextView distance = (TextView) rowView.findViewById(R.id.distanceValue);
        TextView consumption = (TextView) rowView.findViewById(R.id.consumptionValue);
        TextView consumptionTotal = (TextView) rowView.findViewById(R.id.totalConsumptionValue);
        TextView temperature = (TextView) rowView.findViewById(R.id.temperatureValue);

        Trip trip = values[position];

        Date start = new Date((long)trip.startTime);
        Date end = new Date((long)trip.endTime);

        startDate.setText(dateParsed(start));
        stopDate.setText(dateParsed(end));
        distance.setText(String.format("%s km", String.valueOf(Math.round(trip.distance * 1.609))));
        consumption.setText(String.format("%s kWh", String.valueOf(round(trip.usedkWh / (trip.distance * 1.609) * 100, 2))));
        consumptionTotal.setText(String.format("%s kWh", String.valueOf(round(trip.usedkWh, 2))));
        temperature.setText(String.format("%s *C", String.valueOf(trip.avgTemp)));

        return rowView;
    }

    private String dateParsed(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyy\nH:m");
        String format = formatter.format(date);
        return format + " Uhr";
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
