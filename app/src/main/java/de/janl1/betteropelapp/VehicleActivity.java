package de.janl1.betteropelapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
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
import de.janl1.betteropelapp.ui.main.SectionsPagerAdapter;
import de.janl1.betteropelapp.utils.Cryptography;
import de.janl1.betteropelapp.utils.Dialog;
import de.janl1.betteropelapp.utils.Vars;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleActivity extends AppCompatActivity {

    TronityApi apiInterface;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        apiInterface = ApiClient.getClient().create(TronityApi.class);

        if (!isTokenStillValid()) {
            requestToken();
            return;
        }

        Call<VehiclesResponseDTO> call1 = apiInterface.getVehicles("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""));
        call1.enqueue(new Callback<VehiclesResponseDTO>() {
            @Override
            public void onResponse(@NotNull Call<VehiclesResponseDTO> call, @NotNull Response<VehiclesResponseDTO> response) {

                if (response.isSuccessful()) {

                    VehiclesResponseDTO vehList = response.body();

                    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), vehList);
                    ViewPager viewPager = findViewById(R.id.view_pager);
                    viewPager.setAdapter(sectionsPagerAdapter);
                    TabLayout tabs = findViewById(R.id.tabs);
                    tabs.setupWithViewPager(viewPager);
                } else {
                    Dialog.showErrorMessage(VehicleActivity.this, "Laden der Fahrzeugliste", response.code() + " " + response.message()).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<VehiclesResponseDTO> call, @NotNull Throwable t) {
                Dialog.showErrorMessage(VehicleActivity.this, "Laden der Fahrzeugliste", t.getMessage()).show();
            }
        });
    }

    private boolean isTokenStillValid()
    {
        String accessToken = prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, "");
        long expiresTimestamp = prefs.getLong(Vars.PREF_AUTH_EXPIRES, 0);
        boolean appReady = prefs.getBoolean(Vars.PREF_SETUP_COMPLETE, false);

        if (!appReady) {
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }

        if (accessToken.equals("") || expiresTimestamp == 0) {
            Toast.makeText(getBaseContext(), "Kein Token gefunden! Fordere neuen Token an!", Toast.LENGTH_SHORT).show();
            return false;
        }

        long currentTimestamp = new Date().getTime() / 1000L;
        return currentTimestamp <= expiresTimestamp;
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
                public void onResponse(@NotNull Call<Token> call, @NotNull Response<Token> response) {

                    if (response.isSuccessful()) {
                        try {
                            Token token = response.body();

                            if (token == null) {
                                Dialog.showErrorMessage(VehicleActivity.this, "Parsen des Tokens", "Die von der Schnittstelle zurückgegebenen Daten sind leer!").show();
                                return;
                            }

                            editor = prefs.edit();
                            editor.putString(Vars.PREF_AUTH_ACCESSTOKEN, token.access_token);
                            editor.putLong(Vars.PREF_AUTH_EXPIRES, new JSONObject(new String(Base64.getDecoder().decode(token.access_token.split(Pattern.quote("."))[1]))).getLong("exp"));
                            editor.apply();

                            restartActivity();

                        } catch (JSONException e) {
                            Dialog.showErrorMessage(VehicleActivity.this, "Parsen des Tokens", e.getMessage()).show();
                            e.printStackTrace();
                        }

                    } else {
                        // TODO: handle error
                        switch (response.code())
                        {
                            case 500:
                                // The server experienced an unexpected error.
                                Dialog.showErrorMessage(VehicleActivity.this, "Authentifizierung", "Der Server hat einen Fehler zurückgemeldet.").show();
                                break;
                            case 401:
                                // The provided client_id or client_secret were incorrect.
                                Dialog.showErrorMessage(VehicleActivity.this, "Authentifizierung", "Die Client-ID und/oder das Client-Secret sind nicht korrekt!").show();
                                break;
                            default:
                                // Something else
                                Dialog.showErrorMessage(VehicleActivity.this, "Authentifizierung", "Der Server hat einen unbekannten Fehler zurückgemeldet.").show();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Token> call, @NotNull Throwable t) {
                    Dialog.showErrorMessage(VehicleActivity.this, "Authentifizierung", t.getMessage()).show();
                }
            });

        } catch (Exception e) {
            // TODO: handle errors
            Dialog.showErrorMessage(VehicleActivity.this, "Allgemeiner Anwendungsfehler", e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void restartActivity()
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}