package com.telemedicine.app;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class activitybloodsugar extends AppCompatActivity {

    private TextView subpageContent;
    private ProgressBar progressBar;
    private Button previousButton, nextButton;

    private final String[] subpages = {
            "Heart Health Advice 1: Stay Active!",
            "Heart Health Advice 2: Eat Healthy!",
            "Heart Health Advice 3: Regular Checkups!",
            "Heart Health Advice 4: Manage Stress!"
    };

    private int currentPage = 0;
    private final int subpageDuration = 45000; // 45 seconds
    private boolean isRunning = true;
    private Handler handler = new Handler();
    private Runnable pageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitybloodsugar);

        subpageContent = findViewById(R.id.subpageContent1);
        progressBar = findViewById(R.id.progressBar1);
        previousButton = findViewById(R.id.previousButton1);
        nextButton = findViewById(R.id.nextButton1);

        updateContent();

        previousButton.setOnClickListener(v -> {
            isRunning = false;
            moveToPreviousPage();
        });

        nextButton.setOnClickListener(v -> {
            isRunning = false;
            moveToNextPage();
        });

        startAutoCycle();
    }

    private void startAutoCycle() {
        pageSwitcher = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    moveToNextPage();
                }
                handler.postDelayed(this, subpageDuration);
            }
        };
        handler.post(pageSwitcher);
    }

    private void updateContent() {
        subpageContent.setText(subpages[currentPage]);
        progressBar.setProgress((currentPage + 1) * 100 / subpages.length);
    }

    private void moveToNextPage() {
        currentPage = (currentPage + 1) % subpages.length;
        updateContent();
        isRunning = true;
    }

    private void moveToPreviousPage() {
        currentPage = (currentPage - 1 + subpages.length) % subpages.length;
        updateContent();
        isRunning = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(pageSwitcher);
    }
}
