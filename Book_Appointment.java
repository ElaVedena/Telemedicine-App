package com.telemedicine.app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Book_Appointment extends AppCompatActivity {

    private EditText doctorNameInput, appointmentDateInput, appointmentTimeInput, notesInput;
    private Button bookAppointmentButton;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        doctorNameInput = findViewById(R.id.doctorNameInput);
        appointmentDateInput = findViewById(R.id.appointmentDateInput);
        appointmentTimeInput = findViewById(R.id.appointmentTimeInput);
        notesInput = findViewById(R.id.notesInput);
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);

        // Set up date picker for appointment date
        appointmentDateInput.setOnClickListener(v -> showDatePicker());

        // Set up time picker for appointment time
        appointmentTimeInput.setOnClickListener(v -> showTimePicker());

        // Set up the booking button
        bookAppointmentButton.setOnClickListener(v -> bookAppointment());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            appointmentDateInput.setText(date);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
            appointmentTimeInput.setText(time);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void bookAppointment() {
        String doctorName = doctorNameInput.getText().toString().trim();
        String appointmentDate = appointmentDateInput.getText().toString().trim();
        String appointmentTime = appointmentTimeInput.getText().toString().trim();
        String notes = notesInput.getText().toString().trim();

        if (TextUtils.isEmpty(doctorName) || TextUtils.isEmpty(appointmentDate) || TextUtils.isEmpty(appointmentTime)) {
            Toast.makeText(this, "Please fill out all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> appointment = new HashMap<>();
        appointment.put("doctorName", doctorName);
        appointment.put("appointmentDate", appointmentDate);
        appointment.put("appointmentTime", appointmentTime);
        appointment.put("notes", notes);

        firestore.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to book appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
