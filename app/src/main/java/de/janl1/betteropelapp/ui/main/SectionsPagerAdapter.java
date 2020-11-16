package de.janl1.betteropelapp.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import de.janl1.betteropelapp.retrofit.objects.Vehicle;
import de.janl1.betteropelapp.retrofit.objects.VehiclesResponseDTO;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    VehiclesResponseDTO vehicles;

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm, VehiclesResponseDTO vehicles) {
        super(fm);
        mContext = context;
        this.vehicles = vehicles;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return VehicleFragment.newInstance(position + 1, vehicles.data.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Vehicle veh = vehicles.data.get(position);
        return veh.manufacture + " " + veh.model + " (" + veh.year + ")";
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return vehicles.count;
    }
}