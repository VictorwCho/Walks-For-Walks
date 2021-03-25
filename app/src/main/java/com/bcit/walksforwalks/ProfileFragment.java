package com.bcit.walksforwalks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FirebaseUser currentUser;
    private DatabaseReference userData;


    public ProfileFragment() {
    }

    public ProfileFragment(int contentLayoutId) {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        userData = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        userData.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

//                    // this is where we will check the postal code and only add users to
//                    // the recycler who are in the users vicinity
//
//                    // only add users who are not the current user to the recyclerview
//                    User user = userSnapshot.getValue(User.class);
//
//                }
//
//                UserAdapter adapter = new UserAdapter(userList);  // get a user adapter
//                rvUsers.setAdapter(adapter); // set the user adapter
//                rvUsers.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
