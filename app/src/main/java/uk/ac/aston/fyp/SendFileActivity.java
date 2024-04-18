package uk.ac.aston.fyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SendFileActivity extends AppCompatActivity {

    EditText chooseContact;
    ArrayList<String> contacts;
    String myDocId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> contactsAdapter;
    Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);
        chooseContact = findViewById(R.id.choosecontact);

        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        db.collection("Users")
                .whereEqualTo("user", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("MyUser", document.getId() + " => " + document.getData());
                                myDocId = document.getId();
                                contacts = (ArrayList<String>) document.get("contacts");
                            }
                        } else {
                            Log.i("Error getting documents: ", task.getException().toString());
                        }
                    }
                });
        contactsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, contacts);
    }

    public void goBack(View view) {
        Intent i = new Intent(SendFileActivity.this, HomepageActivity.class);
        startActivity(i);
    }

    public void sendFile(View view) {
        if (chooseContact.getText().toString().equals(null)) {
            Toast.makeText(SendFileActivity.this, "emptytext", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);

            // We will be redirected to choose pdf
            i.setType("application/pdf");
            startActivityForResult(i, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            imageuri = data.getData();
            final String timestamp = "" + System.currentTimeMillis();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final String messagePushID = timestamp;
            Toast.makeText(SendFileActivity.this, imageuri.toString(), Toast.LENGTH_SHORT).show();

            // Here we are uploading the pdf in firebase storage with the name of current time
            final StorageReference filepath = storageReference.child(chooseContact.getText().toString() + "/" + messagePushID + "." + "pdf");
            Toast.makeText(SendFileActivity.this, filepath.getName(), Toast.LENGTH_SHORT).show();
            filepath.putFile(imageuri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        Toast.makeText(SendFileActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SendFileActivity.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}