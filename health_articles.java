package com.telemedicine.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class health_articles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles);

        Button bloodPressureArticlesButton = findViewById(R.id.bloodPressureArticlesButton);
        Button heartHealthArticlesButton = findViewById(R.id.heartHealthArticlesButton);
        Button bloodSugarArticlesButton = findViewById(R.id.bloodSugarArticlesButton);
        Button depressionArticlesButton = findViewById(R.id.depressionArticlesButton);
        Button healthyDietArticlesButton = findViewById(R.id.healthyDietArticlesButton);

        bloodPressureArticlesButton.setOnClickListener(v -> openArticleContent("blood_pressure"));
        heartHealthArticlesButton.setOnClickListener(v -> openArticleContent("heart_health"));
        bloodSugarArticlesButton.setOnClickListener(v -> openArticleContent("blood_sugar"));
        depressionArticlesButton.setOnClickListener(v -> openArticleContent("depression"));
        healthyDietArticlesButton.setOnClickListener(v -> openArticleContent("healthy_diet"));
    }

    private void openArticleContent(String articleType) {
        Intent intent = new Intent(this, article_content.class);
        intent.putExtra("articleType", articleType);
        startActivity(intent);
    }
}
