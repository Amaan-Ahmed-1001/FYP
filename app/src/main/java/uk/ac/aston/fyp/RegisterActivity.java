package uk.ac.aston.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//REGISTER ACTIVITY - PAGE TO REGISTER USER IF THEY DO NOT ALREADY HAVE AN ACCOUNT

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    EditText registerEmail;
    EditText registerPassword;
    EditText reenterPass;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        registerEmail = findViewById(R.id.registerusernametext);
        registerPassword = findViewById(R.id.registerpasswordtext);
        reenterPass = findViewById(R.id.registerreenterpass);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    public void registerUser(View view) {
        email = registerEmail.getText().toString();
        password = registerPassword.getText().toString();
        String secondPass = reenterPass.getText().toString();

        if (!password.equals(secondPass)) {
            Log.i("Passwords not the same", "Passwords do not match");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete( Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.i("Register update", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                createDocument();
                                Intent i = new Intent(RegisterActivity.this, HomepageActivity.class);
                                startActivity(i);
                            } else {
                                Log.i("createUserWithEmail:failure", task.getException().toString());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void createDocument() {
        mAuth = FirebaseAuth.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("user", mAuth.getCurrentUser().getEmail());
        user.put("uid", mAuth.getCurrentUser().getUid());

        db.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TAG", "Error adding document");
                    }
                });

    }
}