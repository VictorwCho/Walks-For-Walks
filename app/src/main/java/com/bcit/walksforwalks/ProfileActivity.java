package com.bcit.walksforwalks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int SELECT_PICTURE = 1;

    private FirebaseUser currentUser;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseUsersRef;
    private StorageTask mUploadTask;
    private Uri imageUri;

    private ImageView profilePic;
    private TextView userName;
    private TextView petName;
    private Button editBtn;

    EditText etName;
    EditText etPhone;
    EditText etPetName;
    EditText etPetBreed;
    EditText etPostal;
    ImageView dialogProfilePic;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_profile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view_profile);
        navigationView.setNavigationItemSelectedListener(this);

        editBtn = findViewById(R.id.btn_profile_edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        userName = findViewById(R.id.tV_profile_name);
        petName = findViewById(R.id.tV_profile_pet_name);
        profilePic = findViewById(R.id.iV_profile_profilePic);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseUsersRef = FirebaseDatabase.getInstance().getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads");

    }

    void showDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this); // build view into alert
        LayoutInflater inflater = getLayoutInflater();
        View dialogView;
        dialogView = inflater.inflate(R.layout.edit_profile_dialog, null);

        // Get References
        etName = dialogView.findViewById(R.id.eT_dialog_name);
        etPhone = dialogView.findViewById(R.id.eT_dialog_phone);
        etPetName = dialogView.findViewById(R.id.eT_dialog_petName);
        etPetBreed = dialogView.findViewById(R.id.eT_dialog_pet_breed);
        etPostal = dialogView.findViewById(R.id.eT_dialog_postal);
        dialogProfilePic = dialogView.findViewById(R.id.iV_dialog);

        Button btnSave = dialogView.findViewById(R.id.btn_dialog_save);

        ImageView dialogProfilePic = dialogView.findViewById(R.id.iV_dialog);
        dialogProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
//                Picasso.with(dialogView.getContext())
//                        .load(imageUri)
//                        .into(dialogProfilePic);
            }
        });


        // Set values
        etName.setText(user.getFullName());
        etPhone.setText(user.getPhone());
        etPetName.setText(user.getPetName());
        etPetBreed.setText(user.getPetBreed());
        etPostal.setText(user.getPostalCode());
        Picasso.with(this)
                .load(user.getProfilePic())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(dialogProfilePic);
        dialogBuilder.setView(dialogView); // set the custom view with the AlertDialog Builder
        AlertDialog alertDialog = dialogBuilder.create(); // create alert
        alertDialog.show(); // the the alert

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
                alertDialog.dismiss();
            }
        });

    }


    void onSave() {
        String fullName = etName.getText().toString().trim();
        String petBreed = etPetBreed.getText().toString().trim();
        String petName = etPetName.getText().toString().trim();
        String postalCode = etPostal.getText().toString().trim().replaceAll(" ", "");
        String phoneNumber = etPhone.getText().toString().trim();
        EditText[] attributes = {etName, etPetBreed, etPetName, etPhone, etPhone};
        String[] attributeString = {"Full Name", "Pet Breed",
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
        user.updateUser(fullName,phoneNumber,  petName, petBreed, postalCode);
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Update was Successful!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this)
                    .load(imageUri)
                    .into(profilePic);
        }
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Toast.makeText(this, "image upload in progress", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fireReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            mUploadTask = fireReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            String uploadId = mDatabaseUsersRef.push().getKey();
                                            mDatabaseUsersRef.child(currentUser.getUid()).child("profilePic").setValue(imageUrl);
                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (Objects.equals(postSnapshot.getKey(), currentUser.getUid())) {
                        user = postSnapshot.getValue(User.class);
                        Picasso.with(ProfileActivity.this)
                                .load(user.getProfilePic())
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(profilePic);
                        userName.setText(user.getFullName());
                        petName.setText(user.getPetName());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this makes it so you can see the Options menu (...)
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // this makes it so you can see the Options menu (...)
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;


        if (id == R.id.nav_home) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_logout_profile) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            FirebaseAuth.getInstance().signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_profile);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}