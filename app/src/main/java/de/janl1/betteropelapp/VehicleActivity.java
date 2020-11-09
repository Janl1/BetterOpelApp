package de.janl1.betteropelapp;

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

import de.janl1.betteropelapp.retrofit.ApiClient;
import de.janl1.betteropelapp.retrofit.TronityApi;
import de.janl1.betteropelapp.retrofit.objects.Vehicle;
import de.janl1.betteropelapp.retrofit.objects.VehiclesResponseDTO;
import de.janl1.betteropelapp.ui.main.SectionsPagerAdapter;
import de.janl1.betteropelapp.utils.Vars;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleActivity extends AppCompatActivity {

    TronityApi apiInterface;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        apiInterface = ApiClient.getClient().create(TronityApi.class);

        Call<VehiclesResponseDTO> call1 = apiInterface.getVehicles("Bearer " + prefs.getString(Vars.PREF_AUTH_ACCESSTOKEN, ""));
        call1.enqueue(new Callback<VehiclesResponseDTO>() {
            @Override
            public void onResponse(Call<VehiclesResponseDTO> call, Response<VehiclesResponseDTO> response) {

                if (response.isSuccessful()) {

                    VehiclesResponseDTO vehList = response.body();

                    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), vehList);
                    ViewPager viewPager = findViewById(R.id.view_pager);
                    viewPager.setAdapter(sectionsPagerAdapter);
                    TabLayout tabs = findViewById(R.id.tabs);
                    tabs.setupWithViewPager(viewPager);


                } else {
                    // TODO: handle error
                }
            }

            @Override
            public void onFailure(Call<VehiclesResponseDTO> call, Throwable t) {
                System.out.println("HÖÖÖÖÖÖ");
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}