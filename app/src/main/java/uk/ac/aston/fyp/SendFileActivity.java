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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.io.File;
import java.util.ArrayList;

public class SendFileActivity extends AppCompatActivity {

    //PAGE FOR SENDING FILES
    AutoCompleteTextView auto;
    ArrayList<String> contacts;
    String myDocId;
    String item;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> contactsAdapter;
    Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);
        auto = findViewById(R.id.autoCompleteTextView);
        contacts = new ArrayList<String>();

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
                                for (int i = 0; i <= contacts.size() - 1; i++) {
                                    contactsAdapter.add(contacts.get(i));
                                }
                            }
                        } else {
                            Log.i("Error getting documents: ", task.getException().toString());
                        }
                    }
                });
        contactsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, contacts);
        auto.setAdapter(contactsAdapter);

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                Log.i("item", item);
            }
        });

    }

    public void goBack(View view) {
        Intent i = new Intent(SendFileActivity.this, HomepageActivity.class);
        startActivity(i);
    }
    public void sendFile(View view) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("application/pdf");
            startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            imageuri = data.getData();
            File file = new File(imageuri.getPath());
            String filename = file.getName();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            Toast.makeText(SendFileActivity.this, "File Sent", Toast.LENGTH_SHORT).show();

            final StorageReference filepath = storageReference.child(item + "/" + filename + "." + "pdf");
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