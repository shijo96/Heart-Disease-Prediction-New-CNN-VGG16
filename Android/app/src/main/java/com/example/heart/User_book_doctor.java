package com.example.heart;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class User_book_doctor extends AppCompatActivity {

    private TextView selectedDateText,docname,day;
    private Calendar selectedDate;
    SharedPreferences sh;
    ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_doctor);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        docname = findViewById(R.id.docname);
        day = findViewById(R.id.day);

        docname.setText(sh.getString("first_names",""));
        day.setText(sh.getString("days","")+"\n"+sh.getString("start_times","")+" - "+sh.getString("end_times",""));

        im = findViewById(R.id.imgv);

        // Check gender and set the appropriate background
        if (sh.getString("genders","").equalsIgnoreCase("Male")) {
            im.setBackgroundResource(R.drawable.d1); // Set image for Male
        } else if (sh.getString("genders","").equalsIgnoreCase("Female")) {
            im.setBackgroundResource(R.drawable.d2); // Set image for Female
        }

        selectedDateText = findViewById(R.id.selectedDateText);
        Button datePickerButton = findViewById(R.id.datePickerButton);
        Button bookButton = findViewById(R.id.bookButton);

        // Set up DatePickerDialog to select date within the next 20 days
        datePickerButton.setOnClickListener(v -> openDatePickerDialog());

        // Show selected date in a toast on booking
        bookButton.setOnClickListener(v -> {
            if (selectedDate != null) {
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.getTime());
                Toast.makeText(User_book_doctor.this, "Selected date: " + date, Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor ed = sh.edit();
                ed.putString("date", date);
                ed.apply(); // Save the IP value asynchronously
                startActivity(new Intent(getApplicationContext(), User_payment.class));




            } else {
                Toast.makeText(User_book_doctor.this, "Please select a date first.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDatePickerDialog() {
        Calendar today = Calendar.getInstance();
        Calendar maxDate = (Calendar) today.clone();
        maxDate.add(Calendar.DAY_OF_YEAR, 20); // limit selection to 20 days from today

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                User_book_doctor.this,
                (view, year, month, dayOfMonth) -> {
                    // Update the selected date and display it
                    selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    String selectedDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.getTime());
                    selectedDateText.setText("Selected Date: " + selectedDateString);
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum and maximum selectable dates
        datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }
}
