package de.janl1.betteropelapp.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

import de.janl1.betteropelapp.R;
import de.janl1.betteropelapp.retrofit.ApiClient;
import de.janl1.betteropelapp.retrofit.TronityApi;
import de.janl1.betteropelapp.retrofit.objects.Battery;
import de.janl1.betteropelapp.retrofit.objects.Location;
import de.janl1.betteropelapp.retrofit.objects.Odometer;
import de.janl1.betteropelapp.retrofit.objects.Vehicle;
import de.janl1.betteropelapp.retrofit.objects.VehiclesResponseDTO;
import de.janl1.betteropelapp.utils.Vars;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_VEHICLE_OBJECT = "vehicle_object";
    private static final int PERMISSION_REQUEST_CODE = 1325586531;

    private PageViewModel pageViewModel;
    Vehicle vehicle;
    MapView mapView;
    GoogleMap googleMap;

    public static PlaceholderFragment newInstance(int index, Vehicle veh) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_VEHICLE_OBJECT, (Serializable) veh);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        Vehicle veh = null;
        if (getArguments() != null) {
            veh = (Vehicle) getArguments().getSerializable(ARG_VEHICLE_OBJECT);
        }
        pageViewModel.setVehicleIndex(veh);
        vehicle = veh;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vehicle, container, false);

        pageViewModel.getmVehicle().observe(getActivity(), new Observer<Vehicle>() {
            @Override
            public void onChanged(@Nullable Vehicle veh) {

                // Load vehicle specific data and update UI
                TronityApi apiInterface = ApiClient.getClient().create(TronityApi.class);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


                // Load Battery information
                Call<Battery> call1 = apiInterface.getBattery("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""), vehicle.id);
                call1.enqueue(new Callback<Battery>() {
                    @Override
                    public void onResponse(Call<Battery> call, Response<Battery> response) {

                        if (response.isSuccessful()) {

                            TextView tfBatteryValue = (TextView) root.findViewById(R.id.textAkkuValue);
                            TextView tfDistanceValue = (TextView) root.findViewById(R.id.textDistanceValue);

                            tfBatteryValue.setText(response.body().level + " %");
                            tfDistanceValue.setText(response.body().range + " km");

                        } else {
                            // TODO: handle error
                        }
                    }

                    @Override
                    public void onFailure(Call<Battery> call, Throwable t) {
                        // TODO: handle error
                    }
                });

                // Load Battery information
                Call<Odometer> call2 = apiInterface.getOdometer("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""), vehicle.id);
                call2.enqueue(new Callback<Odometer>() {
                    @Override
                    public void onResponse(Call<Odometer> call, Response<Odometer> response) {

                        if (response.isSuccessful()) {

                            TextView tfTotalDistanceValue = (TextView) root.findViewById(R.id.textTotalDistanceValue);

                            tfTotalDistanceValue.setText(response.body().odometer + " km");

                        } else {
                            // TODO: handle error
                        }
                    }

                    @Override
                    public void onFailure(Call<Odometer> call, Throwable t) {
                        // TODO: handle error
                    }
                });
            }
        });

        mapView = (MapView) root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return root;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }  else {
                }
                return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Display a helptext before requesting permission!
            getActivity().requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, PERMISSION_REQUEST_CODE);
        } else {
            googleMap.setMyLocationEnabled(true);
        }


        TronityApi apiInterface = ApiClient.getClient().create(TronityApi.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Call<Location> call1 = apiInterface.getLocation("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""), vehicle.id);
        call1.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {

                if (response.isSuccessful()) {

                    googleMap.addMarker(new MarkerOptions().position(new LatLng(response.body().latitude, response.body().longitude)).title("Auto"));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(response.body().latitude, response.body().longitude), 15);
                    googleMap.animateCamera(cameraUpdate);

                } else {
                    // TODO: handle error
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                System.out.println("HÖÖÖÖÖÖ");
            }
        });

    }
}