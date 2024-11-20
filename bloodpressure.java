package com.telemedicine.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.Camera;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class bloodpressure extends AppCompatActivity {

    private TextView bloodPressureTextView;
    private ImageView cameraImageView;
    private View waveAnimationView;
    private Button startMeasurementButton;

    private Camera camera;
    private SurfaceView surfaceView;
    private boolean isMeasuring = false;
    private final List<Integer> bloodPressureValues = new ArrayList<>();
    private int measureCount = 0;
    private Handler handler;
    private Runnable measurementRunnable;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodpressure);

        bloodPressureTextView = findViewById(R.id.blood_pressure_value);
        cameraImageView = findViewById(R.id.camera_image);
        waveAnimationView = findViewById(R.id.wave_animation);
        startMeasurementButton = findViewById(R.id.start_measurement);
        surfaceView = findViewById(R.id.camera_surface);

        handler = new Handler();
        measurementRunnable = createMeasurementRunnable();

        startMeasurementButton.setOnClickListener(v -> startMeasurement());
    }

    private Runnable createMeasurementRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if (measureCount < 9) { // 45 seconds (9 x 5-second intervals)
                    takePicture();
                    measureCount++;
                    handler.postDelayed(this, 5000); // 5-second interval
                } else {
                    endMeasurement();
                }
            }
        };
    }

    private void startMeasurement() {
        if (isMeasuring) return;

        isMeasuring = true;
        measureCount = 0;
        bloodPressureValues.clear();

        // Start Camera and display wave animation
        openCamera();
        waveAnimationView.setVisibility(View.VISIBLE);

        handler.postDelayed(measurementRunnable, 5000);
    }

    private void openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }

        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH); // Keep flash on
        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(surfaceView.getHolder());
            camera.startPreview();
        } catch (Exception e) {
            Log.e("Camera Error", "Error setting up preview", e);
        }
    }

    private void takePicture() {
        camera.takePicture(null, null, (data, camera) -> {
            // Process image data for blood pressure calculation
            int simulatedValue = calculateBloodPressure(data);
            bloodPressureValues.add(simulatedValue);
            bloodPressureTextView.setText(String.valueOf(simulatedValue));

            // Restart the preview after taking picture
            camera.startPreview();
        });
    }

    private int calculateBloodPressure(byte[] data) {
        // Placeholder: Use image data for real blood pressure calculation
        return new Random().nextInt(40) + 80; // Sample value between 80-120
    }

    private void endMeasurement() {
        isMeasuring = false;
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        // Calculate and display the average blood pressure
        int average = (int) bloodPressureValues.stream().mapToInt(Integer::intValue).average().orElse(0);
        bloodPressureTextView.setText(String.valueOf(average));
        waveAnimationView.setVisibility(View.GONE);

        // Store the result in Firebase
        saveToFirebase(average);
    }

    private void saveToFirebase(int average) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("BloodPressure");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String username = currentUser.getDisplayName(); // Assuming username is stored in displayName field


            DatabaseReference userRef = database.child(username);
            userRef.child("username").setValue(username);
            userRef.child("average").setValue(average)

                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(bloodpressure.this, "Blood Pressure Saved", Toast.LENGTH_SHORT).show()
                    ).addOnFailureListener(e ->
                            Toast.makeText(bloodpressure.this, "Failed to Save", Toast.LENGTH_SHORT).show()
                    );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
