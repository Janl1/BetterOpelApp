package de.janl1.betteropelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setupComplete = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(Vars.PREF_SETUP_COMPLETE, false);
        if (!setupComplete) {
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }

        setContentView(R.layout.activity_main);
    }
}