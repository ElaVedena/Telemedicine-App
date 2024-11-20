package com.telemedicine.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView welcomeText;
    private EditText genderInput, ageInput, contactInfoInput, weightInput, healthConditionsInput;
    private Button saveProfileButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI components
        welcomeText = findViewById(R.id.welcomeText);
        genderInput = findViewById(R.id.genderInput);
        ageInput = findViewById(R.id.ageInput);
        contactInfoInput = findViewById(R.id.contactInfoInput);
        weightInput = findViewById(R.id.weightInput);
        healthConditionsInput = findViewById(R.id.healthConditionsInput);
        saveProfileButton = findViewById(R.id.saveProfileButton);

        // Populate user data if logged in
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.child(userId).get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    welcomeText.setText("Welcome, " + name + "!");
                    genderInput.setText(snapshot.child("gender").getValue(String.class));
                    ageInput.setText(snapshot.child("age").getValue(String.class));
                    contactInfoInput.setText(snapshot.child("contactInfo").getValue(String.class));
                    weightInput.setText(snapshot.child("weight").getValue(String.class));
                    healthConditionsInput.setText(snapshot.child("healthConditions").getValue(String.class));
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to retrieve profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }

        // Save profile data
        saveProfileButton.setOnClickListener(v -> saveProfileData());
    }

    private void saveProfileData() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            Map<String, Object> profileData = new HashMap<>();
            profileData.put("gender", genderInput.getText().toString());
            profileData.put("age", ageInput.getText().toString());
            profileData.put("contactInfo", contactInfoInput.getText().toString());
            profileData.put("weight", weightInput.getText().toString());
            profileData.put("healthConditions", healthConditionsInput.getText().toString());

            databaseReference.child(userId).updateChildren(profileData).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }
}
