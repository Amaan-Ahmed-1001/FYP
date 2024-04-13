package uk.ac.aston.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

//HOMEPAGE ACTIVITY - INCLUDES HOMEPAGE OPTIONS AND USER'S LATEST ADDED FILES

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void toContacts(View view) {
        Intent i = new Intent(HomepageActivity.this, ContactsActivity.class);
        startActivity(i);
    }

    public void toSendFile(View view) {
        Intent i = new Intent(HomepageActivity.this, SendFileActivity.class);
        startActivity(i);
    }

    public void createFile(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
            intent.setType("*/*");
            this.startActivity(intent);
        }
    }
}