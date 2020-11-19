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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Calendar;

import de.janl1.betteropelapp.R;
import de.janl1.betteropelapp.retrofit.ApiClient;
import de.janl1.betteropelapp.retrofit.TronityApi;
import de.janl1.betteropelapp.retrofit.objects.Bulk;
import de.janl1.betteropelapp.retrofit.objects.Consumption;
import de.janl1.betteropelapp.retrofit.objects.Location;
import de.janl1.betteropelapp.retrofit.objects.Trip;
import de.janl1.betteropelapp.retrofit.objects.Trips;
import de.janl1.betteropelapp.retrofit.objects.Vehicle;
import de.janl1.betteropelapp.utils.Dialog;
import de.janl1.betteropelapp.utils.Vars;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class VehicleFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_VEHICLE_OBJECT = "vehicle_object";
    private static final int PERMISSION_REQUEST_CODE = 1325586531;

    private PageViewModel pageViewModel;
    Vehicle vehicle;
    MapView mapView;
    GoogleMap googleMap;

    SwipeRefreshLayout swipeRefreshLayout;

    TextView tvBatteryValue;
    TextView tvDistanceValue;
    TextView tvTotalDistanceValue;
    TextView tvConsumption;
    CardView cardViewPercentage;

    TronityApi apiInterface;
    SharedPreferences prefs;

    boolean googleMapIsReady = false;

    public static VehicleFragment newInstance(Vehicle veh) {
        VehicleFragment fragment = new VehicleFragment();
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

        tvBatteryValue = root.findViewById(R.id.textAkkuValue);
        tvDistanceValue = root.findViewById(R.id.textDistanceValue);
        tvTotalDistanceValue = root.findViewById(R.id.textTotalDistanceValue);
        cardViewPercentage = root.findViewById(R.id.cardViewPercentage);
        swipeRefreshLayout = root.findViewById(R.id.swiperefresh);
        tvConsumption = root.findViewById(R.id.textConsumptionValue);

        apiInterface = ApiClient.getClient().create(TronityApi.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        pageViewModel.getmVehicle().observe(getActivity(), new Observer<Vehicle>() {
            @Override
            public void onChanged(@Nullable Vehicle veh) {
                loadBulkData(swipeRefreshLayout);
            }
        });

        mapView = (MapView) root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBulkData(swipeRefreshLayout);
            }
        });

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

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMapIsReady = true;

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Display a helptext before requesting permission!
            getActivity().requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, PERMISSION_REQUEST_CODE);
        } else {
            googleMap.setMyLocationEnabled(true);
        }

        this.googleMap = googleMap;

        loadMapData();
    }

    private void loadBulkData(SwipeRefreshLayout swipeRefreshLayout)
    {
        swipeRefreshLayout.setRefreshing(true);
        Call<Bulk> bulkCall = apiInterface.getBulkInformation("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""), vehicle.id);
        bulkCall.enqueue(new Callback<Bulk>() {
            @Override
            public void onResponse(Call<Bulk> call, Response<Bulk> response) {

                Bulk vehicleInformation = response.body();

                if (response.isSuccessful()) {
                    tvBatteryValue.setText(MessageFormat.format("{0} %", vehicleInformation.level));
                    tvDistanceValue.setText(MessageFormat.format("{0} km", vehicleInformation.range));
                    tvTotalDistanceValue.setText(MessageFormat.format("{0} km", vehicleInformation.odometer));

                    if (vehicleInformation.charging.equals("Charging"))  {
                        cardViewPercentage.setCardBackgroundColor(getResources().getColor(R.color.batteryStateCharging));
                    }

                    if (googleMapIsReady) {
                        try {
                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(vehicleInformation.latitude, vehicleInformation.longitude)).title("Auto"));
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(vehicleInformation.latitude, vehicleInformation.longitude), 15);
                            googleMap.animateCamera(cameraUpdate);

                        } catch (Exception e) {
                            e.printStackTrace();
                            // TODO:: handle error
                        }
                    }

                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    Dialog.showErrorMessage(getActivity().getApplicationContext(), "Laden der Fahrzeuginformation", response.code() + " " + response.message()).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<Bulk> call, Throwable t) {
                Dialog.showErrorMessage(getActivity().getApplicationContext(), "Laden der Fahrzeuginformation", t.getMessage()).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Call<Trips> tripsCall = apiInterface.getTrips("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""), "Metric", vehicle.id);
        tripsCall.enqueue(new Callback<Trips>() {
            @Override
            public void onResponse(Call<Trips> call, Response<Trips> response) {

                Trips consumption = response.body();

                double distance = 0;
                double kwUsed = 0;

                for (Trip trip: consumption.data) {
                    distance = distance + (trip.distance * 1.609);
                    kwUsed = kwUsed + trip.usedkWh;

                }

                if (response.isSuccessful()) {
                    tvConsumption.setText(MessageFormat.format("{0} kWh", Math.round(kwUsed / distance * 1000) / 10.0));
                } else {
                    Dialog.showErrorMessage(getActivity(), "Laden des Verbrauches", response.code() + " " + response.message()).show();
                }
            }

            @Override
            public void onFailure(Call<Trips> call, Throwable t) {
                Dialog.showErrorMessage(getActivity(), "Laden des Verbrauches", t.getMessage()).show();
            }
        });
    }

    private  void loadMapData()
    {
        Call<Location> call1 = apiInterface.getLocation("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""), vehicle.id);
        call1.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {

                if (response.isSuccessful()) {
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(response.body().latitude, response.body().longitude)).title("Auto"));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(response.body().latitude, response.body().longitude), 15);
                    googleMap.animateCamera(cameraUpdate);

                } else {
                    Dialog.showErrorMessage(getActivity().getApplicationContext(), "Laden der Fahrzeugposition", response.code() + " " + response.message()).show();
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Dialog.showErrorMessage(getActivity().getApplicationContext(), "Laden der Fahrzeugposition", t.getMessage()).show();
            }
        });
    }
}