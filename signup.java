package com.telemedicine.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.database.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.telemedicine.app.databinding.ActivityMainBinding;
import com.telemedicine.app.databinding.ActivitySignupBinding;

public class signup extends AppCompatActivity {
    ActivitySignupBinding binding;
    String name, email, username, password;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = binding.name.getText().toString();
                password = binding.password.getText().toString();
                username = binding.username.getText().toString();
                email = binding.email.getText().toString();
                ProgressBar progressBar = findViewById(R.id.progressBar);
                Button button = findViewById(R.id.button7);

                if (!name.isEmpty() && !username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
                    User user = new User(name,email, username, password);
                    progressBar.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                    FirebaseFirestore firebase = FirebaseFirestore.getInstance();

                    // Create a new user document
                    firebase.collection("users").document(username).set(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Clear input fields
                                        binding.name.setText("");
                                        binding.username.setText("");
                                        binding.email.setText("");
                                        binding.password.setText("");
                                        Toast.makeText(signup.this, "Account created", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(signup.this, "Error creating account", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    progressBar.setVisibility(View.GONE);
                    button.setEnabled(false);
                }else {
                    Toast.makeText(signup.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
