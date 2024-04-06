package uk.ac.aston.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
    }

    public void toHome(View view) {
        Intent i = new Intent(ContactsActivity.this, HomepageActivity.class);
        startActivity(i);
    }
}