package com.bcit.walksforwalks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static RecyclerView mRecyclerView;
    private UserAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private static List<User> userList;
    private FirebaseUser currentUser;
    public String postalCode;
    private ProgressBar mProgressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = findViewById(R.id.rV_main_users);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        userList = new ArrayList<>();

        mProgressCircle = findViewById(R.id.progress_circle);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        String current_user_uid = mDatabaseRef.child(currentUser.getUid()).getKey();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postalCode = snapshot.child("postalCode").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference currentUserDb = mDatabaseRef.child(current_user_uid);
        currentUserDb.addListenerForSingleValueEvent(eventListener);
    }

    private boolean matchProvinceByPostalCode(String postalCode1, String postalCode2) {
        return (postalCode1.charAt(0) == postalCode2.charAt(0));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (!Objects.equals(currentUser.getEmail(), user.getEmail())) {
                        if (matchProvinceByPostalCode(postalCode, user.getPostalCode())) {
                            userList.add(user);
                        }
                    }
                }
                mAdapter = new UserAdapter(MainActivity.this, userList);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getDetails(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

//    private void userLogin() {
//        String email = userEmail.getText().toString().trim();
//        String password = userPassword.getText().toString().trim();
//
//        if (email.isEmpty()) {
//            userEmail.setError("Email is required!");
//            userEmail.requestFocus();
//            return;
//        }
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            userEmail.setError("Please enter a valid email!");
//            userEmail.requestFocus();
//            return;
//        }
//
//        if (password.isEmpty()) {
//            userPassword.setError("Please enter a valid password!");
//            userPassword.requestFocus();
//            return;
//        }
//
//        if (password.length() < 6) {
//            userPassword.setError("Min password length is 6 characters!");
//            userPassword.requestFocus();
//            return;
//        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this makes it so you can see the Options menu (...)
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
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
            intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_logout) {

            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            FirebaseAuth.getInstance().signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}