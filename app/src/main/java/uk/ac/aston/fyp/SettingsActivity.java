package uk.ac.aston.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingsActivity extends AppCompatActivity {

    Button deleteAccountButton;
    FirebaseFirestore db;
    FirebaseStorage storage;
    String myDocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        deleteAccountButton = findViewById(R.id.deleteaccountbutton);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

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
                            }
                            Log.i("MyID", myDocId);
                        } else {
                            Log.i("Error getting documents: ", task.getException().toString());
                        }
                    }
                });
    }


    public void deleteAccount(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("Users").document(myDocId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TAG", "Error deleting document");
                    }
                });

        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child(user.getEmail());

        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("folder deleted", "user files deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("ERROR", "Check error");
            }
        });

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