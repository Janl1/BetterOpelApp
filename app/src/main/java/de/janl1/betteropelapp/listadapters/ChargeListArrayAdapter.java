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
import java.util.concurrent.TimeUnit;

import de.janl1.betteropelapp.R;
import de.janl1.betteropelapp.retrofit.objects.Charge;

public class ChargeListArrayAdapter extends ArrayAdapter<Charge> {
    private final Context context;
    private final Charge[] values;

    public ChargeListArrayAdapter(Context context, Charge[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_charges, (ViewGroup) parent.getTag());
            holder = new ViewHolder();

            holder.startDate = convertView.findViewById(R.id.startDate);
            holder.stopDate = convertView.findViewById(R.id.stopDate);
            holder.distance = convertView.findViewById(R.id.distanceValue);
            holder.energy = convertView.findViewById(R.id.energyValue);
            holder.max = convertView.findViewById(R.id.maxValue);
            holder.time = convertView.findViewById(R.id.timeValue);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Charge charge = values[position];

        Date start = new Date((long)charge.startTime);
        Date end = new Date((long)charge.endTime);

        long time = (long)charge.endTime - (long)charge.startTime;
        time = time / 1000;


        String timeCharging = String.format(Locale.GERMAN, "%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60);

        holder.startDate.setText(dateParsed(start));
        holder.stopDate.setText(dateParsed(end));
        holder.distance.setText(String.format("%s km", round(charge.range * 1.609, 2)));
        holder.energy.setText(String.format("%s kWh", charge.kWh));
        holder.max.setText(String.format("%s kW", round(charge.max, 2)));
        holder.time.setText(timeCharging);

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
        private TextView energy;
        private TextView max;
        private TextView time;
    }
}
