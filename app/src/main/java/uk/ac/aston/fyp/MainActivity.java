package uk.ac.aston.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

//MAIN ACTIVITY - FEATURES LOGIN AND REGISTER BUTTONS

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        Log.i("basic", "Confirming");
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void register(View view) {
        Log.i("register", "registering");
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }
}