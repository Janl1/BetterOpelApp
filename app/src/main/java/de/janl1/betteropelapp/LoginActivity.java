package de.janl1.betteropelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import de.janl1.betteropelapp.utils.Cryptography;
import de.janl1.betteropelapp.utils.Vars;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ALIAS = "BETTEROPELAPP";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    EditText clientId;
    EditText clientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = prefs.edit();

        clientId = (EditText) findViewById(R.id.clientId);
        clientSecret = (EditText) findViewById(R.id.clientSecret);

        boolean setupComplete = prefs.getBoolean(Vars.PREF_SETUP_COMPLETE, false);
        if (setupComplete) {
            loadExistingData();
        }
    }

    /**
     * Loads the existing encrypted login information and decrypts it
     */
    private void loadExistingData() {

        String encryptedClientId = prefs.getString(Vars.PREF_CLIENT_ID, "");
        String encryptedClientSecret = prefs.getString(Vars.PREF_CLIENT_SECRET, "");

        if (encryptedClientId.equals("") || encryptedClientSecret.equals("")) {
            // TODO: handle errors
            return;
        }

        try {

            Cryptography c = new Cryptography(Vars.CRYPT_KEYNAME_AUTH);

            clientId.setText(c.decrypt(encryptedClientId));
            clientSecret.setText(c.decrypt(encryptedClientSecret));

        } catch (Exception e) {
            // TODO: handle errors
            e.printStackTrace();
        }
    }

    /**
     * Opens the tronity.io website in browser
     * @param view internal view instance
     */
    public void openTronity(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tronity.io/home"));
        startActivity(browserIntent);
    }

    /**
     * Opens the tronity.io platform website in browser
     * @param view internal view instance
     */
    public void openTronityPlatform(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.platform.tronity.io/"));
        startActivity(browserIntent);
    }

    /**
     * Validates, encrypts and saves the form data
     * @param view internal view instance
     */
    public void saveData(View view)
    {
        String clientIdText = clientId.getText().toString();
        String clientSecretText = clientSecret.getText().toString();

        if (clientIdText.equals("") || clientSecretText.equals("")) {
            Toast.makeText(getBaseContext(), "Bitte beide Felder ausfüllen!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Cryptography c = new Cryptography(Vars.CRYPT_KEYNAME_AUTH);
            String clientIdTextCrypt = c.encrypt(clientIdText);
            String clientSecretTextCrypt = c.encrypt(clientSecretText);

            editor.putString(Vars.PREF_CLIENT_ID, clientIdTextCrypt);
            editor.putString(Vars.PREF_CLIENT_SECRET, clientSecretTextCrypt);
            editor.putBoolean(Vars.PREF_SETUP_COMPLETE, true);

            editor.commit();

            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();

        } catch (Exception e) {
            // TODO: handle errors
            e.printStackTrace();
        }
    }
}