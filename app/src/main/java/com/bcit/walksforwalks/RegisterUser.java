package com.bcit.walksforwalks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private String defaultPetName = "Spot";
    private String defaultPetBreed = "mutt";
    private String defaultPhone = "000-000-0000";

    private FirebaseAuth mAuth;
    private EditText editFullName;
    private EditText editEmail;
    private EditText editPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        editFullName = findViewById(R.id.editText_register_name);
        editEmail = findViewById(R.id.editText_register_email);
        editPassword = findViewById(R.id.editText_register_password);
        Button registerUser = findViewById(R.id.button_register_user);
        progressBar = findViewById(R.id.progressBar_register_user);

        registerUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_register_user) {
            onRegister();
        }
    }

    private void onRegister() {
        String email = editEmail.getText().toString().trim();
        String fullName = editFullName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            editFullName.setError("Full name is required!");
            editFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editEmail.setError("Email is required!");
            editEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please provide valid email!");
            editEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editPassword.setError("In password length needs to be greather than 6.");
            editPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(fullName, email);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                            } else {
                                Toast.makeText(RegisterUser.this, "User has not been successfully registered", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Toast.makeText(RegisterUser.this, "User has not been successfully registered", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}