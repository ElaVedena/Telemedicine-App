package com.telemedicine.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.database.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.telemedicine.app.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private ProgressBar progressBar;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private FirebaseDatabase db;  // FirebaseDatabase instance
    private DatabaseReference chatRef;  // Reference to the chats node

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize UI components
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        progressBar = findViewById(R.id.progressBar);

        // Set up RecyclerView
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);

        // Initialize Firebase database reference
      db = FirebaseDatabase.getInstance();
        chatRef = db.getReference("chats");  // Connect to the "chats" node in the database

        // Show loading indicator when starting to load messages
        progressBar.setVisibility(View.VISIBLE);

        // Load chat messages from Firebase
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();  // Clear previous messages

                // Loop through the snapshot to get each message
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);  // Get message from snapshot
                    if (message != null) {
                        messageList.add(message);  // Add message to the list
                    }
                }

                // Notify adapter to update RecyclerView
                messageAdapter.notifyDataSetChanged();

                // Scroll to the latest message
                recyclerViewMessages.scrollToPosition(messageList.size() - 1);

                // Hide the loading indicator
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors, such as database connectivity issues
                Toast.makeText(Chat.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        // Set up the send button click listener
        buttonSend.setOnClickListener(view -> {
            String content = editTextMessage.getText().toString().trim();  // Get message content
            if (!content.isEmpty()) {
                // Create a new message object with sender as "patient"
                Message message = new Message("patient", content, System.currentTimeMillis());

                // Push message to Firebase database
                chatRef.push().setValue(message);

                // Clear the input field after sending the message
                editTextMessage.setText("");
            }
        });
    }
}


