package com.telemedicine.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import coil.Coil;
import coil.request.ImageRequest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profilePicture;
    private TextView welcomeText;
    private EditText ageInput, contactInfoInput, weightInput, healthConditionsInput;
    private RadioGroup genderGroup;
    private Button saveProfileButton, uploadPictureButton, logoutButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    private Uri profileImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures");

        // Initialize UI components
        profilePicture = view.findViewById(R.id.profilepicture);
        welcomeText = view.findViewById(R.id.welcomeText);
        ageInput = view.findViewById(R.id.ageInput);
        contactInfoInput = view.findViewById(R.id.contactInfoInput);
        weightInput = view.findViewById(R.id.weightInput);
        healthConditionsInput = view.findViewById(R.id.healthConditionsInput);
        genderGroup = view.findViewById(R.id.genderGroup);
        saveProfileButton = view.findViewById(R.id.saveProfileButton);
        uploadPictureButton = view.findViewById(R.id.uploadPictureButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Populate existing user data
        populateUserData();

        // Set up listeners
        saveProfileButton.setOnClickListener(v -> saveProfileData());
        uploadPictureButton.setOnClickListener(v -> selectProfilePicture());
        logoutButton.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void populateUserData() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String username = currentUser.getUid();

            firestore.collection("users").document(username).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String age = documentSnapshot.getString("age");
                    String contactInfo = documentSnapshot.getString("contactInfo");
                    String weight = documentSnapshot.getString("weight");
                    String healthConditions = documentSnapshot.getString("healthConditions");
                    String gender = documentSnapshot.getString("gender");
                    String profileImageUrl = documentSnapshot.getString("profilePictureUrl");

                    welcomeText.setText("Welcome, " + (name != null ? name : "User") + "!");
                    ageInput.setText(age);
                    contactInfoInput.setText(contactInfo);
                    weightInput.setText(weight);
                    healthConditionsInput.setText(healthConditions);

                    if (gender != null) {
                        if (gender.equalsIgnoreCase("Male")) {
                            genderGroup.check(R.id.maleRadio);
                        } else if (gender.equalsIgnoreCase("Female")) {
                            genderGroup.check(R.id.femaleRadio);
                        } else {
                            genderGroup.check(R.id.otherRadio);
                        }
                    }

                    // Load profile picture using Coil
                    if (profileImageUrl != null) {
                        ImageRequest request = new ImageRequest.Builder(requireContext())
                                .data(profileImageUrl) // The URL of the image
                                .target(profilePicture) // The ImageView to load into
                                .build();
                        Coil.imageLoader(requireContext()).enqueue(request);
                    }
                }
            }).addOnFailureListener(e ->
                    Toast.makeText(requireContext(), "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void saveProfileData() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String username = currentUser.getUid();

            // Validate input
            if (TextUtils.isEmpty(ageInput.getText().toString())) {
                Toast.makeText(requireContext(), "Please enter your age.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(contactInfoInput.getText().toString())) {
                Toast.makeText(requireContext(), "Please enter your contact info.", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedGender = ((RadioButton) requireView().findViewById(genderGroup.getCheckedRadioButtonId())).getText().toString();

            Map<String, Object> user = new HashMap<>();
            user.put("age", ageInput.getText().toString());
            user.put("contactInfo", contactInfoInput.getText().toString());
            user.put("weight", weightInput.getText().toString());
            user.put("healthConditions", healthConditionsInput.getText().toString());
            user.put("gender", selectedGender);

            // Save profile picture URL if uploaded
            if (profileImageUri != null) {
                storageReference.child(username).putFile(profileImageUri).addOnSuccessListener(taskSnapshot -> {
                    storageReference.child(username).getDownloadUrl().addOnSuccessListener(uri -> {
                        user.put("profilePictureUrl", uri.toString());
                        saveToFirestore(username, user);
                    });
                }).addOnFailureListener(e ->
                        Toast.makeText(requireContext(), "Failed to upload picture: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            } else {
                saveToFirestore(username, user);
            }
        }
    }

    private void saveToFirestore(String username, Map<String, Object> user) {
        firestore.collection("users").document(username).set(user).addOnSuccessListener(aVoid ->
                Toast.makeText(requireContext(), "Profile saved successfully!", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(e ->
                Toast.makeText(requireContext(), "Failed to save profile: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void selectProfilePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            profilePicture.setImageURI(profileImageUri);
        }
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        Intent intent = new Intent(requireContext(), login.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
