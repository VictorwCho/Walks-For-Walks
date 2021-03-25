package com.bcit.walksforwalks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    private static RecyclerView rvUsers;
    private static List<User> userList;
    private FirebaseUser currentUser;
    private DatabaseReference dbUsers;
    public String postalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get hamburger Button
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        // add the toggle to our hamburger
        drawer.addDrawerListener(toggle);
        // so the toggle knows when the drawer is open or closed
        toggle.syncState();

        // Set the navView onclick listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rvUsers = findViewById(R.id.rV_main_users); // get rV reference
        userList = new ArrayList<>(); // initialize the userList

        currentUser = FirebaseAuth.getInstance().getCurrentUser(); // Get currentUser reference
        dbUsers = FirebaseDatabase.getInstance().getReference("Users"); // Get database reference

        String current_user_uid = dbUsers.child(currentUser.getUid()).getKey();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postalCode = snapshot.child("postalCode").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference currentUserDb = dbUsers.child(current_user_uid);
        currentUserDb.addListenerForSingleValueEvent(eventListener);
    }

    private boolean matchProvinceByPostalCode(String postalCode1, String postalCode2)
    {
        return(postalCode1.charAt(0) == postalCode2.charAt(0));
    }

    @Override
    protected void onStart() {
        super.onStart();




        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {

                   User user = userSnapshot.getValue(User.class);
                    if (!Objects.equals(currentUser.getEmail(), user.getEmail()))
                    {
                        if(matchProvinceByPostalCode(postalCode,user.postalCode)) {

                            if (matchProvinceByPostalCode(postalCode, user.postalCode)) {
                                userList.add(user);
                            }
                        }
                    }





//if (matchProvinceByPostalCode(postalCode, user.postalCode)) {
//                            userList.add(user);
//                        }

                    UserAdapter adapter = new UserAdapter(userList);  // get a user adapter
                    rvUsers.setAdapter(adapter); // set the user adapter
                    rvUsers.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

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
        Fragment fragment = null;
        Intent intent = null;

//
//        switch (id) {
//            case R.id.nav_drafts:
//                fragment = draftsFragment;
//                break;
//            case R.id.nav_sent:
//                fragment = sentItemsFragment;
//                break;
//            case R.id.nav_trash:
//                fragment = trashFragment;
//                break;
//            case R.id.nav_help:
//                intent = new Intent(this, HelpActivity.class);
//                break;
//            case R.id.nav_feedback:
//                intent = new Intent(this, FeedbackActivity.class);
//                break;
//            default:
//                fragment = inboxFragment;
//
//        }
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_view, fragment);
//            transaction.commit();
        } else {
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}