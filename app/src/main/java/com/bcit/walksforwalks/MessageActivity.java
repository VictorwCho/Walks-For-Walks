package com.bcit.walksforwalks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser currentUser;
    private DatabaseReference mDatabaseRef;
    private String string_name;
    private String string_email;
    private EditText editTextMessage;
    private String string_key;
    private ChatAdapter chatAdapter;
    private RecyclerView mesRecyclerView;
    private List<ChatMessage> chatList;
    public ImageView return_home;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent = getIntent();

        mesRecyclerView = findViewById(R.id.rv_messages);
        mesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();

        TextView textViewName = findViewById(R.id.textView1_message_name);
        TextView textViewPhone = findViewById(R.id.textView2_message_phone);
        TextView textViewEmail = findViewById(R.id.textView3_message_email);
        ImageView imageView = findViewById(R.id.imageView_message_picture);
        editTextMessage = findViewById(R.id.editText_message_message);
        Button sendMessage = findViewById(R.id.button_message_send);
        sendMessage.setOnClickListener(this);

        string_name = intent.getStringExtra("name");
        String string_phone = intent.getStringExtra("phone");
        string_email = intent.getStringExtra("email");
        String string_pic = intent.getStringExtra("photo");
        Picasso.with(this)
                .load(string_pic)
                .fit()
                .centerCrop()
                .into(imageView);

        textViewName.setText(string_name);
        textViewPhone.setText(string_phone);
        textViewEmail.setText(string_email);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseRef.orderByChild("email").equalTo(string_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot x: snapshot.getChildren()) {
                    string_key = x.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toast.makeText(this, "Click on the profile pic to return home", Toast.LENGTH_SHORT).show();

        /// Return home after page
        return_home = findViewById(R.id.imageView_message_picture);
        return_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mDatabaseRef.child(currentUser.getUid()).child("conversations")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot x : snapshot.getChildren()) {
                    if (x.child("messageUser").getValue().toString().equals(string_email)) {
                        String email = x.child("messageUser").getValue().toString();
                        Long long_time = (Long) x.child("messageTime").getValue();
                        Format format = new SimpleDateFormat("EEE, dd MMM yyyy h:mm a");
                        String time = format.format(long_time);
                        String message = x.child("messageText").getValue().toString();
                        ChatMessage chat = new ChatMessage(email, time, message);
                        chatList.add(chat);
                    }
                    chatAdapter = new ChatAdapter(MessageActivity.this, chatList);
                    mesRecyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        String message = editTextMessage.getText().toString();
        ChatMessage messageObject = new ChatMessage(message, currentUser.getEmail());
        mDatabaseRef.child(string_key + "/" + "conversations").push().setValue(messageObject);
        editTextMessage.setText("");
    }
}
