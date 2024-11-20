package com.telemedicine.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class bloodsugar extends AppCompatActivity {

    private GraphView bloodSugarGraph;
    private SeekBar bloodSugarScale;
    private TextView scaleValueText, recommendedArticleText;
    private Button addRecordButton, viewAllRecordsButton, setReminderButton;

    private String userEmail = "user@example.com"; // Replace with dynamically fetched user email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodsugar);

//        Validate user email
    if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "User email is not set. Please log in again.", Toast.LENGTH_SHORT).show();
          finish();
          return;
      }

        // Initialize Views
         initializeViews();

        // Set up UI interactions
       setupUIInteractions();

        // Initialize Graph
      setupGraph();
    }

    private void initializeViews() {
        bloodSugarGraph = findViewById(R.id.bloodSugarGraph);
        bloodSugarScale = findViewById(R.id.bloodSugarScale);
        scaleValueText = findViewById(R.id.scaleValueText);
        recommendedArticleText = findViewById(R.id.recommendedArticleText);
        addRecordButton = findViewById(R.id.addRecordButton);
        viewAllRecordsButton = findViewById(R.id.viewAllRecordsButton);
        setReminderButton = findViewById(R.id.setReminderButton);

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        ImageView clockReminder = findViewById(R.id.clockReminder);
        clockReminder.setOnClickListener(v ->
                Toast.makeText(this, "Set a daily reminder to record blood sugar.", Toast.LENGTH_SHORT).show()
        );
    }

    private void setupUIInteractions() {
        bloodSugarScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                runOnUiThread(() -> scaleValueText.setText("Value: " + progress + " mg/dL"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        addRecordButton.setOnClickListener(v -> saveBloodSugarRecord(bloodSugarScale.getProgress()));
        viewAllRecordsButton.setOnClickListener(v -> startActivity(new Intent(this, RecordHistory.class)));
        setReminderButton.setOnClickListener(v -> setDailyReminder());
    }

    private void saveBloodSugarRecord(int value) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Map<String, Object> recordData = new HashMap<>();
        recordData.put("value", value);
        recordData.put("timestamp", timestamp);

        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        CollectionReference recordsCollection = firebase.collection("records");

        recordsCollection.document(userEmail.replace(".", "_"))
                .collection("entries")
                .add(recordData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Record saved successfully!", Toast.LENGTH_SHORT).show();
                    updateGraph(value, timestamp);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save record: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error saving record", e);
                });

        updateRecommendedArticle(value);
    }

    private void updateGraph(int value, String timestamp) {
        try {
            String[] timeParts = timestamp.split(" ")[1].split(":");
            float xValue = Float.parseFloat(timeParts[0]) + Float.parseFloat(timeParts[1]) / 60.0f;

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(xValue, value)
            });

            int color = value < 100 ? ContextCompat.getColor(this, R.color.blue)
                    : (value <= 125 ? ContextCompat.getColor(this, R.color.orange) : ContextCompat.getColor(this, R.color.red));
            series.setColor(color);

            bloodSugarGraph.addSeries(series);
            bloodSugarGraph.invalidate();
        } catch (Exception e) {
            Log.e("GraphUpdate", "Error updating graph with timestamp: " + timestamp, e);
        }
    }

    private void setupGraph() {
        bloodSugarGraph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        bloodSugarGraph.getGridLabelRenderer().setVerticalAxisTitle("Blood Sugar (mg/dL)");
        bloodSugarGraph.getViewport().setScrollable(true);
        bloodSugarGraph.getViewport().setScalable(true);
    }

    private void updateRecommendedArticle(int value) {
        if (value < 100) {
            recommendedArticleText.setText("Maintain Healthy Sugar Levels");
        } else if (value <= 125) {
            recommendedArticleText.setText("Learn About Managing Pre-Diabetes");
        } else {
            recommendedArticleText.setText("Articles on Managing Diabetes");
        }
    }

    private void setDailyReminder() {
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            long triggerTime = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            Toast.makeText(this, "Daily reminder set!", Toast.LENGTH_SHORT).show();
        }
    }

    public static class ReminderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Time to record your blood sugar!", Toast.LENGTH_SHORT).show();
        }
    }
}
