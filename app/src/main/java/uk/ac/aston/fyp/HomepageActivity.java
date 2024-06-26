package uk.ac.aston.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

//HOMEPAGE ACTIVITY - INCLUDES HOMEPAGE OPTIONS AND USER'S LATEST ADDED FILES

public class HomepageActivity extends AppCompatActivity {

    TextView filesList;
    ListView fileList;
    ArrayList<String> files;
    ArrayAdapter<String> filesAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        filesList = findViewById(R.id.loggedinuser);
        fileList = findViewById(R.id.fileslistview);
        files = new ArrayList<String>();
        filesAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, files);
        fileList.setAdapter(filesAdapter);
        db = FirebaseFirestore.getInstance();
        filesList.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        mAuth = FirebaseAuth.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            filesAdapter.add(item.getName());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });

    }

    public void logOut(View view) {
        mAuth.signOut();
        Log.i("Signed out", "Signed out");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void toContacts(View view) {
        Intent i = new Intent(HomepageActivity.this, ContactsActivity.class);
        startActivity(i);
    }

    public void toSendFile(View view) {
        Intent i = new Intent(HomepageActivity.this, SendFileActivity.class);
        startActivity(i);
    }

    public void toSettings(View view) {
        Intent i = new Intent(HomepageActivity.this, SettingsActivity.class);
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
