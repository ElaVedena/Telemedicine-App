package com.firebase.com;

import com.database.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDB {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firestore;

    public FirebaseDB() {
        // Initialize Firebase Realtime Database
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = this.database.getReference("users");

        // Initialize Firestore
        this.firestore = FirebaseFirestore.getInstance();
    }

    // Retrieve Realtime Database reference for "users" node
    public DatabaseReference getUsersReferences() {
        return this.databaseReference;
    }

    // Add user to Firestore
    public void createUserInFirestore(User user) {
        firestore.collection("users").add(user)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("User added to Firestore with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error adding user to Firestore: " + e.getMessage());
                });
    }

    // Add user to Realtime Database
    public void createUserInRealtimeDatabase(String userId, User user) {
        databaseReference.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("User added to Realtime Database with ID: " + userId);
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error adding user to Realtime Database: " + e.getMessage());
                });
    }

    public void createUser(User user) {
    }

}

