package uk.ac.aston.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    Button lightmodeButton;
    Button darkmodeButton;
    boolean nightmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        lightmodeButton = findViewById(R.id.lightmodebutton);
        darkmodeButton = findViewById(R.id.darkmodebutton);

        sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        nightmode = sharedPreferences.getBoolean("night", false);
    }

    public void lightMode(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        editor = sharedPreferences.edit();
        editor.putBoolean("night", false);
        editor.apply();
    }

    public void darkMode(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        editor = sharedPreferences.edit();
        editor.putBoolean("night", true);
        editor.apply();
    }
}