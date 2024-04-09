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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ContactsActivity extends AppCompatActivity {

    private EditText addContactText;
    private ListView contactList;
    private ArrayList<String> usersList;
    private ArrayAdapter<String> usersListAdapter;
    private String myDocId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        addContactText = findViewById(R.id.addcontacttext);
        contactList = findViewById(R.id.contactslist);
        usersList = new ArrayList<String>();
        usersListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        contactList.setAdapter(usersListAdapter);
        myDocId = "";
    }

    public void toHome(View view) {
        Intent i = new Intent(ContactsActivity.this, HomepageActivity.class);
        startActivity(i);
    }

    public void addContact(View view) {

        //first get, if user exists
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
        docData.put("listExample", Arrays.asList().add(addContactText.getText().toString()));

        db.collection("Users").document(documentId)
                .set(docData)
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