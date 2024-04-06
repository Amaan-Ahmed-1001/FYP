package uk.ac.aston.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class ContactsActivity extends AppCompatActivity {

    private EditText addContactText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        addContactText = findViewById(R.id.addcontacttext);
    }

    public void toHome(View view) {
        Intent i = new Intent(ContactsActivity.this, HomepageActivity.class);
        startActivity(i);
    }

    public void addContact(View view) {
        Log.i("contact add", addContactText.getText().toString());
    }
}