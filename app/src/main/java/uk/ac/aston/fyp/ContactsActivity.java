package uk.ac.aston.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ContactsActivity extends AppCompatActivity {

    private EditText addContactText;
    private ListView contactListView;
    private ArrayList<String> usersList;
    private ArrayAdapter<String> usersListAdapter;
    private String myDocId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        addContactText = findViewById(R.id.addcontacttext);
        contactListView = findViewById(R.id.contactslistview);
        usersList = new ArrayList<String>();
        usersListAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, usersList);
        contactListView.setAdapter(usersListAdapter);

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
                                usersList = (ArrayList<String>) document.get("contacts");
                                for (int i = 0; i <= usersList.size() - 1; i++) {
                                    usersListAdapter.add(usersList.get(i));
                                }
                            }
                            Log.i("MyID", myDocId);
                        } else {
                            Log.i("Error getting documents: ", task.getException().toString());
                        }
                    }
                });

    }

    public void toHome(View view) {
        Intent i = new Intent(ContactsActivity.this, HomepageActivity.class);
        startActivity(i);
    }

    public void addContact(View view) {

        //first get, if user exists
        if (addContactText.getText().toString().equals(null)) {
            Toast.makeText(this, "Please enter a contact", Toast.LENGTH_SHORT).show();
        } else {
            db.collection("Users")
                    .whereEqualTo("user", addContactText.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.i("TAG", document.getId() + " => " + document.getData());
                                }
                                getDocId();
                                usersListAdapter.add(addContactText.getText().toString());
                            } else {
                                Log.i("Error getting documents: ", task.getException().toString());
                            }
                        }
                    });
        }
    }

    public void getDocId() {
        //second get, current user's firestore doc id
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
                                addToDatabase(myDocId);
                            }
                            Log.i("MyID", myDocId);
                        } else {
                            Log.i("Error getting documents: ", task.getException().toString());
                        }
                    }
                });
    }

    public void addToDatabase(String documentId) {
        Map<String, Object> docData = new HashMap<>();
        usersList.add(addContactText.getText().toString());
        docData.put("contacts", usersList);

        db.collection("Users").document(documentId)
                .set(docData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i( "Error writing document", e.toString());
                    }
                });
    }
    
}