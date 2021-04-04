package com.bcit.walksforwalks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editFullName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPetBreed;
    private EditText editPetName;
    private EditText editPostalCode;
    private EditText editPhoneNumber;
    private Button btnRegisterUser;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();
        editFullName = findViewById(R.id.editText_register_name);
        editEmail = findViewById(R.id.editText_register_email);
        editPassword = findViewById(R.id.editText_register_password);
        editPetBreed = findViewById(R.id.editText_register_pet_breed);
        editPetName = findViewById(R.id.editText_register_pet_name);
        editPostalCode = findViewById(R.id.editText_postal_code);
        editPhoneNumber = findViewById(R.id.editText_phone_number);
        btnRegisterUser = findViewById(R.id.button_register_user);
        progressBar = findViewById(R.id.progressBar_register_user);
        btnRegisterUser.setOnClickListener(this);
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
        String petBreed = editPetBreed.getText().toString().trim();
        String petName = editPetName.getText().toString().trim();
        String postalCode = editPostalCode.getText().toString().trim().replaceAll(" ", "");
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        EditText[] attributes = {editFullName, editEmail, editPassword, editPetBreed,
                editPetName, editPostalCode, editPhoneNumber};
        String[] attributeString = {"Full Name", "Email", "Password", "Pet Breed",
                "Pet Name", "Postal Code", "Phone Number"};
        HashMap<String, EditText> userInfo = new HashMap<String, EditText>();
        for (int i = 0; i < attributes.length; i++) {
            userInfo.put(attributeString[i], attributes[i]);
        }

        for (Map.Entry<String, EditText> attribute : userInfo.entrySet()) {
            String attributeName = attribute.getKey();
            EditText editText = attribute.getValue();
            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError(attributeName + " is required");
                editText.requestFocus();
                return;
            }
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please provide valid email!");
            editEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editPassword.setError("In password length needs to be greater than 6.");
            editPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(fullName, email, phoneNumber, petName, petBreed, postalCode, null);
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