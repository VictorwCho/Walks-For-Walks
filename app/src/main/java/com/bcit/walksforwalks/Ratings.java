package com.bcit.walksforwalks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class Ratings extends AppCompatActivity {
    Button submitBtn;
    RatingBar ratingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private List<Float> allRatings;
    private String badRating = "Oh that's awful, our support will take a look at this idiot right away";
    private String averageRating = "Alright alright, seems like a dog was walked at least";
    private String goodRating = "You may wanna buy this fella a beer,";
    private String ratingMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_layout);

        ratingBar = findViewById(R.id.rating_bar);
        submitBtn = findViewById(R.id.submit_rating);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Ratingssss");

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();


        submitBtn.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               float s = ratingBar.getRating();
               String stars = Float.toString(s);

               if (s <= 2.5){
                   ratingMessage = badRating;
               }
               else if (s == 5){
                   ratingMessage = goodRating;
               }
               else
                   ratingMessage = averageRating;


               Toast.makeText(getApplicationContext(), ratingMessage, Toast.LENGTH_LONG).show();
               Intent intent = new Intent(Ratings.this, MainActivity.class);
               startActivity(intent);
           }
       });
    }
}
