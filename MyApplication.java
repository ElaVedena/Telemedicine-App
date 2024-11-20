package com.telemedicine.app;

import static com.google.firebase.database.logging.Logger.Level.DEBUG;

import android.annotation.SuppressLint;
import android.app.Application;

import com.google.firebase.FirebaseApp;


public class MyApplication extends Application {
    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);



    }
}
