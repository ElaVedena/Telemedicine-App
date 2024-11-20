package com.telemedicine.app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class article_content extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);

        TextView articleTitle = findViewById(R.id.articleTitle);
        TextView articleContent = findViewById(R.id.articleContent);

        String articleType = getIntent().getStringExtra("articleType");
        if (articleType != null) {
            switch (articleType) {
                case "blood_pressure":
                    articleTitle.setText("Understanding Blood Pressure");
                    articleContent.setText(getBloodPressureContent());
                    break;
                case "heart_health":
                    articleTitle.setText("Maintaining Heart Health");
                    articleContent.setText(getHeartHealthContent());
                    break;
                case "blood_sugar":
                    articleTitle.setText("Managing Blood Sugar Levels");
                    articleContent.setText(getBloodSugarContent());
                    break;
                case "depression":
                    articleTitle.setText("Coping with Depression");
                    articleContent.setText(getDepressionContent());
                    break;
                case "healthy_diet":
                    articleTitle.setText("Healthy Diet & Recipes");
                    articleContent.setText(getHealthyDietContent());
                    break;
            }
        }
    }

    private String getBloodPressureContent() {
        return "Content for blood pressure articles. Topics include:\n" +
                "1. What is Blood Pressure?" +
                "Blood pressure is the force exerted by circulating blood against the walls of the arteries. It is measured in two numbers:\n" +
                "\n" +
                "    Systolic Pressure: The pressure when the heart pumps blood into the arteries.\n" +
                "    Diastolic Pressure: The pressure when the heart is at rest between beats.\n" +
                "\n" +
                "Normal blood pressure is usually around 120/80 mmHg. Consistently higher or lower values indicate potential health concerns.\n" +
                "\n" +
                "    High Blood Pressure (Hypertension): Persistent readings of 140/90 mmHg or higher.\n" +
                "    Low Blood Pressure (Hypotension): Readings lower than 90/60 mmHg.\n" +
                "2. Symptoms of High and Low Blood Pressure" +
                "High Blood Pressure (Hypertension):\n" +
                "Often called the \"silent killer,\" high blood pressure may show no symptoms. When it does, symptoms include:\n" +
                "\n" +
                "    Severe headaches.\n" +
                "    Shortness of breath.\n" +
                "    Nosebleeds.\n" +
                "    Fatigue or confusion.\n" +
                "    Vision problems.\n" +
                "\n" +
                "Low Blood Pressure (Hypotension):\n" +
                "Symptoms of low blood pressure may include:\n" +
                "\n" +
                "    Dizziness or lightheadedness.\n" +
                "    Fainting.\n" +
                "    Blurred vision.\n" +
                "    Nausea.\n" +
                "    Shock in extreme cases (e.g., cold, clammy skin).\n" +
                "\n" +
                "Both conditions, if unmanaged, can lead to severe complications, including heart disease, stroke, and organ failure.\n" +
                "3. Tips for Maintaining Healthy Blood Pressure Levels\n" +
                "Maintaining a healthy blood pressure requires lifestyle adjustments and regular monitoring. Here are actionable tips:\n" +
                "a. Healthy Diet\n" +
                "\n" +
                "    Consume less salt (sodium): Limit intake to less than 2,300 mg per day (about one teaspoon).\n" +
                "    Adopt the DASH Diet: Focus on vegetables, fruits, whole grains, and low-fat dairy. Reduce saturated fats and cholesterol.\n" +
                "    Increase potassium intake: Found in bananas, oranges, spinach, and potatoes, potassium helps balance sodium levels.\n" +
                "\n" +
                "b. Regular Physical Activity\n" +
                "\n" +
                "    Engage in 150 minutes of moderate aerobic exercise weekly, like brisk walking, swimming, or cycling.\n" +
                "    Incorporate strength training at least twice a week to improve overall cardiovascular health.\n" +
                "\n" +
                "c. Stress Management\n" +
                "\n" +
                "    Practice relaxation techniques such as deep breathing, meditation, or yoga.\n" +
                "    Avoid overworking; schedule breaks and prioritize rest.\n" +
                "    Engage in hobbies or activities that bring joy and relaxation.\n" +
                "\n" +
                "d. Avoid Harmful Habits\n" +
                "\n" +
                "    Quit smoking, as it narrows arteries and raises blood pressure.\n" +
                "    Limit alcohol intake to one drink per day for women and two for men.\n" +
                "    Reduce caffeine consumption, as it may temporarily spike blood pressure.\n" +
                "\n" +
                "e. Regular Monitoring\n" +
                "\n" +
                "    Use a home blood pressure monitor to track readings.\n" +
                "    Visit a doctor for regular check-ups, especially if you have a family history of hypertension.\n" +
                "\n" +
                "f. Medications (if necessary)\n" +
                "\n" +
                "    If prescribed, adhere to medication routines without skipping doses. Common medications include ACE inhibitors, beta blockers, and diuretics.";
    }

    private String getHeartHealthContent() {
        return "Content for heart health articles. Topics include:\n" +
                "1. Understanding Heart Diseases\n" +
                "2. The Role of Diet and Exercise in Heart Health\n" +
                "3. Monitoring Heart Health\n" +
                "Content spans 400+ words with subtopics and actionable advice.";
    }

    private String getBloodSugarContent() {
        return "Content for managing blood sugar levels. Topics include:\n" +
                "1. Understanding Diabetes and Blood Sugar\n" +
                "2. Signs of High and Low Blood Sugar\n" +
                "3. Tips for Balancing Blood Sugar\n" +
                "Content spans 400+ words with subtopics and actionable advice.";
    }

    private String getDepressionContent() {
        return "Content for coping with depression. Topics include:\n" +
                "1. Understanding Depression\n" +
                "2. Common Symptoms of Depression\n" +
                "3. Strategies for Coping and Seeking Help\n" +
                "Content spans 400+ words with subtopics and actionable advice.";
    }

    private String getHealthyDietContent() {
        return "Content for healthy diets and recipes. Topics include:\n" +
                "1. The Role of a Balanced Diet\n" +
                "2. Easy and Nutritious Recipes\n" +
                "3. Foods for Specific Health Benefits\n" +
                "Content spans 400+ words with subtopics and actionable advice.";
    }
}
