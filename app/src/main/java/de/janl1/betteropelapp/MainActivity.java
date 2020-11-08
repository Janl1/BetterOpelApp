package de.janl1.betteropelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

        checkTokenExistanceAndExpiry();

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

    private void checkTokenExistanceAndExpiry()
    {
        String accessToken = prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, "");
        long expiresTimestamp = prefs.getLong(Vars.PREF_AUTH_EXPIRES, 0);

        if (accessToken.equals("") || expiresTimestamp == 0) {
            Toast.makeText(getBaseContext(), "Kein Token gefunden! Fordere neuen Token an!", Toast.LENGTH_SHORT).show();
            requestToken();
        }

        long currentTimestamp = new Date().getTime() / 1000L;
        if (currentTimestamp > expiresTimestamp) {
            Toast.makeText(getBaseContext(), "Token abgelaufen! Fordere neuen Token an!", Toast.LENGTH_SHORT).show();
            requestToken();
        } else {
            Toast.makeText(getBaseContext(), "Token gültig!", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestToken()
    {
        String encryptedClientId = prefs.getString(Vars.PREF_CLIENT_ID, "");
        String encryptedClientSecret = prefs.getString(Vars.PREF_CLIENT_SECRET, "");

        try {
            Cryptography c = new Cryptography(Vars.CRYPT_KEYNAME_AUTH);

            TokenRequestDTO tokenRequestDTO = new TokenRequestDTO(c.decrypt(encryptedClientId), c.decrypt(encryptedClientSecret), "app");
            Call<Token> call1 = apiInterface.auth(tokenRequestDTO);
            call1.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {

                    System.out.println("Request ist gelaufen!");

                    if (response.isSuccessful()) {
                        try {
                            Token token = response.body();

                            editor = prefs.edit();
                            editor.putString(Vars.PREF_AUTH_ACCESSTOKEN, token.access_token);
                            editor.putLong(Vars.PREF_AUTH_EXPIRES, new JSONObject(new String(Base64.getDecoder().decode(token.access_token.split(Pattern.quote("."))[1]))).getLong("exp"));
                            editor.apply();
                        } catch (JSONException e) {
                            // TODO: handle error
                            e.printStackTrace();
                        }

                    } else {
                        // TODO: handle error
                        switch (response.code())
                        {
                            case 500:
                                // The server experienced an unexpected error.
                                break;
                            case 401:
                                // The provided client_id or client_secret were incorrect.
                                break;
                            default:
                                // Something else
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    // TODO: handle error
                }
            });

        } catch (Exception e) {
            // TODO: handle errors
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}