package com.telemedicine.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.database.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.telemedicine.app.databinding.ActivityLgBinding;

public class lg extends AppCompatActivity {  // Renamed class to "lg"
    ActivityLgBinding binding;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set up View Binding
        binding = ActivityLgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Database reference
        reference = FirebaseDatabase.getInstance().getReference("users");

        // Handle Login button click
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.username1.getText().toString();
                String password = binding.password1.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    loginUser(username, password);
                } else {
                    Toast.makeText(lg.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void loginUser(String username, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(username).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Hide the loader once the task is complete
//                        progressBar.setVisibility(View.GONE);
//                        loginButton.setEnabled(true); // Re-enable the button

                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                            User user = task.getResult().toObject(User.class);
                            if (user != null && user.getPassword().equals(password)) {
                                Toast.makeText(lg.this, "Login successful", Toast.LENGTH_SHORT).show();
                                // Navigate to the next activity
                               Intent intent = new Intent(lg.this, home.class);
                                startActivity(intent) ;
                            } else {
                                Toast.makeText(lg.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(lg.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
