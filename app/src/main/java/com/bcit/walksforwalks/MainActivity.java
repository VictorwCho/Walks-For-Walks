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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText userEmail;
    private EditText userPassword;
    private Button logIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.textView_main_register);
        register.setOnClickListener(this);

        logIn = findViewById(R.id.button_main_login);
        logIn.setOnClickListener(this);

        userEmail = findViewById(R.id.editText_main_username);
        userPassword = findViewById(R.id.editText_main_password);

        progressBar = findViewById(R.id.progressBar_main_login);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textView_main_register) {
            startActivity(new Intent(this, RegisterUser.class));
        }

        if (v.getId() == R.id.button_main_login) {
            userLogin();
        }
    }

    private void userLogin() {
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (email.isEmpty()) {
            userEmail.setError("Email is required!");
            userEmail.requestFocus();
            return;
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Please enter a valid email!");
            userEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            userPassword.setError("Please enter a valid password!");
            userPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            userPassword.setError("Min password length is 6 characters!");
            userPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // redict to user profile
                } else {
                    Toast.makeText(MainActivity.this, "Failed to login! Please" +
                            "check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}