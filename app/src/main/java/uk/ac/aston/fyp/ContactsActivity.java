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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ContactsActivity extends AppCompatActivity {

    private EditText addContactText;
    private ListView contactList;
    private ArrayList<String> usersList;
    private ArrayAdapter<String> usersListAdapter;
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
    }

    public void toHome(View view) {
        Intent i = new Intent(ContactsActivity.this, HomepageActivity.class);
        startActivity(i);
    }

    public void addContact(View view) {
        Log.i("contact add", addContactText.getText().toString());

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
                            usersListAdapter.add(addContactText.getText().toString());
                        } else {
                            Log.i( "Error getting documents: ", task.getException().toString());
                        }
                    }
                });
    }
}