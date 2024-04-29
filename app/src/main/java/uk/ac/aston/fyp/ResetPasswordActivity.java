package uk.ac.aston.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText newPass;
    EditText newPassTwo;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        newPass = findViewById(R.id.resetpasstext);
        newPassTwo = findViewById(R.id.resetpasstext2);
        mAuth = FirebaseAuth.getInstance();
    }

    public void backToSettings(View view) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void changePassword(View view) {
        if (!newPass.getText().toString().equals(newPassTwo.getText().toString())) {
            Log.i("Passwords", "Passwords must match");
        } else {
            FirebaseUser user = mAuth.getCurrentUser();

            user.updatePassword(newPass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("TAG", "User password updated.");
                            }
                        }
                    });
        }
        Intent i = new Intent(this, HomepageActivity.class);
        startActivity(i);
    }
}