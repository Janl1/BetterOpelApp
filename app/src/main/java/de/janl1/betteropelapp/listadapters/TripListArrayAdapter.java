package de.janl1.betteropelapp.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_trips, (ViewGroup) parent.getTag());
            holder = new ViewHolder();

            holder.startDate = convertView.findViewById(R.id.startDate);
            holder.stopDate = convertView.findViewById(R.id.stopDate);
            holder.distance = convertView.findViewById(R.id.timeValue);
            holder.consumption = convertView.findViewById(R.id.distanceValue);
            holder.consumptionTotal = convertView.findViewById(R.id.maxValue);
            holder.temperature = convertView.findViewById(R.id.energyValue);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Trip trip = values[position];

        Date start = new Date((long)trip.startTime);
        Date end = new Date((long)trip.endTime);

        double consumptionValue = round(trip.usedkWh / (trip.distance * 1.609) * 100, 2);
        if (consumptionValue < 0) {
            consumptionValue = 0;
        }

        holder.startDate.setText(dateParsed(start));
        holder.stopDate.setText(dateParsed(end));
        holder.distance.setText(String.format("%s km", Math.round(trip.distance * 1.609)));
        holder.consumption.setText(String.format("%s kWh", consumptionValue));
        holder.consumptionTotal.setText(String.format("%s kWh", round(trip.usedkWh, 2)));
        holder.temperature.setText(String.format("%s *C", trip.avgTemp));

        return convertView;
    }

    private String dateParsed(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyy\nHH:mm", Locale.GERMANY);
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

    static class ViewHolder {
        private TextView startDate;
        private TextView stopDate;
        private TextView distance;
        private TextView consumption;
        private TextView consumptionTotal;
        private TextView temperature;
    }
}
