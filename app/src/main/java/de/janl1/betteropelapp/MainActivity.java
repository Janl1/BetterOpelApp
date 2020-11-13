package de.janl1.betteropelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

import de.janl1.betteropelapp.retrofit.ApiClient;
import de.janl1.betteropelapp.retrofit.TronityApi;
import de.janl1.betteropelapp.retrofit.objects.Token;
import de.janl1.betteropelapp.retrofit.objects.TokenRequestDTO;
import de.janl1.betteropelapp.retrofit.objects.Vehicle;
import de.janl1.betteropelapp.retrofit.objects.VehiclesResponseDTO;
import de.janl1.betteropelapp.utils.Cryptography;
import de.janl1.betteropelapp.utils.Vars;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    TronityApi apiInterface;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean setupComplete = prefs.getBoolean(Vars.PREF_SETUP_COMPLETE, false);
        if (!setupComplete) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }

        setContentView(R.layout.activity_main);

        apiInterface = ApiClient.getClient().create(TronityApi.class);

        // checkTokenExistanceAndExpiry();

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
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

    public void editCredentials(View view)
    {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        this.finish();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        Call<VehiclesResponseDTO> call1 = apiInterface.getVehicles("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""));
        call1.enqueue(new Callback<VehiclesResponseDTO>() {
            @Override
            public void onResponse(Call<VehiclesResponseDTO> call, Response<VehiclesResponseDTO> response) {

                if (response.isSuccessful()) {

                        VehiclesResponseDTO vehList = response.body();
                        Vehicle vehicle = vehList.data.get(0);
                        setTitle(vehicle.manufacture + " " + vehicle.model + " (" + vehicle.year + ")");




                } else {
                    // TODO: handle error
                }
            }

            @Override
            public void onFailure(Call<VehiclesResponseDTO> call, Throwable t) {
                System.out.println("HÖÖÖÖÖÖ");
            }
        });


        googleMap.addMarker(new MarkerOptions().position(new LatLng(51.468327, 7.0810843)).title("Auto"));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(51.468327, 7.0810843), 15);
        googleMap.animateCamera(cameraUpdate);
    }
}