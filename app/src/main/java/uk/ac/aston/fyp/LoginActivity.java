package uk.ac.aston.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//LOGIN ACTIVITY - ACTIVITY TO LOGIN USER IF THEY HAVE AN ACCOUNT

public class LoginActivity extends AppCompatActivity {

    private String username;
    private String password;
    EditText user;
    EditText pass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = findViewById(R.id.loginusernametext);
        pass = findViewById(R.id.loginpasswordtext);
        mAuth = FirebaseAuth.getInstance();
    }

    public void toSplash(View view) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void submit(View view) {
        username = user.getText().toString();
        password = pass.getText().toString();

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("loginupdate", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(LoginActivity.this, HomepageActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("signInWithEmail:failure", task.getException().toString());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}