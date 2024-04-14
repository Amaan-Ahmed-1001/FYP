package uk.ac.aston.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    Button lightmodeButton;
    Button darkmodeButton;
    Button deleteAccountButton;
    boolean nightmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        lightmodeButton = findViewById(R.id.lightmodebutton);
        darkmodeButton = findViewById(R.id.darkmodebutton);
        deleteAccountButton = findViewById(R.id.deleteaccountbutton);

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

    public void deleteAccount(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("deleting account", "User account deleted.");
                        }
                    }
                });

        Intent i = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(i);
    }
}