package com.telemedicine.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Weight extends AppCompatActivity {

    private EditText weightInput, heightInput;
    private Button saveRecordButton, viewRecordsButton;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);
        saveRecordButton = findViewById(R.id.saveRecordButton);
        viewRecordsButton = findViewById(R.id.viewRecordsButton);

        // Set up button listeners
        saveRecordButton.setOnClickListener(v -> saveRecord());
        viewRecordsButton.setOnClickListener(v -> {
            // Navigate to a record viewing activity
            startActivity(new Intent(this, Weight.class));
        });
    }

    private void saveRecord() {
        String weightStr = weightInput.getText().toString().trim();
        String heightStr = heightInput.getText().toString().trim();

        if (TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(heightStr)) {
            Toast.makeText(this, "Please enter both weight and height.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double height = Double.parseDouble(heightStr);

            if (weight <= 0 || height <= 0) {
                Toast.makeText(this, "Invalid values for weight or height.", Toast.LENGTH_SHORT).show();
                return;
            }

            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            Map<String, Object> recordData = new HashMap<>();
            recordData.put("weight", weight);
            recordData.put("height", height);
            recordData.put("timestamp", timestamp);

            // Replace "user@example.com" with the actual logged-in user email or ID
            String userId = "user@example.com".replace(".", "_");

            firestore.collection("weight_height_records")
                    .document(userId)
                    .collection("records")
                    .add(recordData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Record saved successfully!", Toast.LENGTH_SHORT).show();
                        weightInput.setText("");
                        heightInput.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to save record: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format. Please enter valid weight and height.", Toast.LENGTH_SHORT).show();
        }
    }
}
