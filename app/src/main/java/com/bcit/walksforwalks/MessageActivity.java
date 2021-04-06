package com.bcit.walksforwalks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser currentUser;
    DatabaseReference mDatabaseRef;
    String string_name;
    String string_email;
    EditText editTextMessage;
    String string_key;
    private ChatAdapter chatAdapter;
    private RecyclerView mesRecyclerView;
    private List<ChatMessage> chatList;

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

        mDatabaseRef.child(currentUser.getUid()).child("conversations")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot x : snapshot.getChildren()) {
                    if (x.child("messageUser").getValue().toString().equals(string_email)) {
                        String email = x.child("messageUser").getValue().toString();
                        String time = x.child("messageTime").getValue().toString();
                        String message = x.child("messageText").getValue().toString();
                        ChatMessage chat = new ChatMessage(email, time, message);
//                        Log.d("debug", chat.toString());
                        chatList.add(chat);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chatAdapter = new ChatAdapter(MessageActivity.this, chatList);
        mesRecyclerView.setAdapter(chatAdapter);
    }

    @Override
    public void onClick(View v) {
        String message = editTextMessage.getText().toString();
        ChatMessage messageObject = new ChatMessage(message, currentUser.getEmail());
        String userId = currentUser.getUid();
        Log.d("Debug", message);
//        mDatabaseRef.child(userId + "/" + "conversations").push().setValue(messageObject);
        mDatabaseRef.child(string_key + "/" + "conversations").push().setValue(messageObject);
        editTextMessage.setText("");

//        Log.d("DEBUG", string_key);

    }
}
