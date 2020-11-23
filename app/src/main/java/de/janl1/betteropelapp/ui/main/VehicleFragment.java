package de.janl1.betteropelapp.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Calendar;

import de.janl1.betteropelapp.R;
import de.janl1.betteropelapp.listadapters.TripListArrayAdapter;
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

    FloatingActionButton refreshFloatingButton;
    ProgressBar progressBar;

    TextView tvBatteryValue;
    TextView tvBatteryText;
    TextView tvDistanceValue;
    TextView tvTotalDistanceValue;
    TextView tvConsumption;
    CardView cardViewPercentage;

    ListView tripsList;

    TronityApi apiInterface;
    SharedPreferences prefs;

    boolean googleMapIsReady = false;

    public static VehicleFragment newInstance(Vehicle veh) {
        VehicleFragment fragment = new VehicleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_VEHICLE_OBJECT, veh);
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
        //swipeRefreshLayout = root.findViewById(R.id.swiperefresh);
        tvConsumption = root.findViewById(R.id.textConsumptionValue);
        tvBatteryText = root.findViewById(R.id.textAkku);
        tripsList = root.findViewById(R.id.tripList);
        refreshFloatingButton = root.findViewById(R.id.refreshFloatingButton);
        progressBar = root.findViewById(R.id.progressBar);

        apiInterface = ApiClient.getClient().create(TronityApi.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        pageViewModel.getmVehicle().observe(getActivity(), veh -> loadBulkData());

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        refreshFloatingButton.setOnClickListener(v -> VehicleFragment.this.loadBulkData());
        // swipeRefreshLayout.setOnRefreshListener(() -> loadBulkData(swipeRefreshLayout));

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

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Display a helptext before requesting permission!
            getActivity().requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, PERMISSION_REQUEST_CODE);
        } else {
            googleMap.setMyLocationEnabled(true);
        }

        this.googleMap = googleMap;

        loadMapData();
    }

    private void loadBulkData()
    {
        showLoadingSpinner();
        Call<Bulk> bulkCall = apiInterface.getBulkInformation("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""), vehicle.id);
        bulkCall.enqueue(new Callback<Bulk>() {
            @Override
            public void onResponse(@NotNull Call<Bulk> call, @NotNull Response<Bulk> response) {

                Bulk vehicleInformation = response.body();

                if (vehicleInformation == null) {
                    Dialog.showErrorMessage(getActivity(), "Laden der Fahrzeuginformation", "Die von der Schnittstelle zurückgegebenen Daten sind leer!").show();
                    hideLoadingSpinner();
                    return;
                }

                if (response.isSuccessful()) {
                    tvBatteryValue.setText(MessageFormat.format("{0} %", vehicleInformation.level));
                    tvDistanceValue.setText(MessageFormat.format("{0} km", vehicleInformation.range));
                    tvTotalDistanceValue.setText(MessageFormat.format("{0} km", vehicleInformation.odometer));

                    if (vehicleInformation.charging.equals("Charging"))  {
                        cardViewPercentage.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.batteryStateCharging));
                        tvBatteryValue.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), android.R.color.background_light));
                        tvBatteryText.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), android.R.color.background_light));
                    } else {
                        cardViewPercentage.setCardBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.background_light));
                        tvBatteryValue.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), android.R.color.secondary_text_light));
                        tvBatteryText.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), android.R.color.secondary_text_light));
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

                    hideLoadingSpinner();

                } else {
                    Dialog.showErrorMessage(getActivity(), "Laden der Fahrzeuginformation", response.code() + " " + response.message()).show();
                    hideLoadingSpinner();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Bulk> call, @NotNull Throwable t) {
                Dialog.showErrorMessage(getActivity(), "Laden der Fahrzeuginformation", t.getMessage()).show();
                hideLoadingSpinner();
            }
        });

        Call<Trips> tripsCall = apiInterface.getTrips("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""), "metric", vehicle.id);
        tripsCall.enqueue(new Callback<Trips>() {
            @Override
            public void onResponse(@NotNull Call<Trips> call, @NotNull Response<Trips> response) {

                Trips consumption = response.body();

                double distance = 0;
                double kwUsed = 0;

                for (Trip trip: consumption.data) {
                    distance = distance + (trip.distance * 1.609);
                    kwUsed = kwUsed + trip.usedkWh;

                }

                Trip[] itemsArray = new Trip[consumption.data.size()];
                itemsArray = consumption.data.toArray(itemsArray);

                TripListArrayAdapter adapter = new TripListArrayAdapter(getActivity(), itemsArray);
                tripsList.setAdapter(adapter);

                if (response.isSuccessful()) {
                    tvConsumption.setText(MessageFormat.format("{0} kWh", Math.round(kwUsed / distance * 1000) / 10.0));
                } else {
                    Dialog.showErrorMessage(getActivity(), "Laden des Verbrauches", response.code() + " " + response.message()).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Trips> call, @NotNull Throwable t) {
                Dialog.showErrorMessage(getActivity(), "Laden des Verbrauches", t.getMessage()).show();
            }
        });
    }

    private  void loadMapData()
    {
        Call<Location> call1 = apiInterface.getLocation("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""), vehicle.id);
        call1.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(@NotNull Call<Location> call, @NotNull Response<Location> response) {

                if (response.isSuccessful()) {
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(response.body().latitude, response.body().longitude)).title("Auto"));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(response.body().latitude, response.body().longitude), 15);
                    googleMap.animateCamera(cameraUpdate);

                } else {
                    Dialog.showErrorMessage(getActivity(), "Laden der Fahrzeugposition", response.code() + " " + response.message()).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Location> call, @NotNull Throwable t) {
                Dialog.showErrorMessage(getActivity(), "Laden der Fahrzeugposition", t.getMessage()).show();
            }
        });
    }

    private void showLoadingSpinner()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingSpinner()
    {
        progressBar.setVisibility(View.INVISIBLE);
    }
}