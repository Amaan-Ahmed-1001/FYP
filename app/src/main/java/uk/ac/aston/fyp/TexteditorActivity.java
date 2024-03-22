package uk.ac.aston.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.provider.MediaStore;
import android.view.View;

public class TexteditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texteditor);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    public void createNewFile(View view) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
        intent.setType("*.txt");
        this.startActivity(intent);
    }
}