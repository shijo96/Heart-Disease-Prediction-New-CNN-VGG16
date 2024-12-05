package com.example.heart;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Get the ViewPager2 reference
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Array of layout resource IDs for the slides
        int[] layouts = {R.layout.slide_home, R.layout.slide_login, R.layout.slide_registration};

        // Set the adapter and pass ViewPager2 instance as the third parameter
        SliderAdapter sliderAdapter = new SliderAdapter(this, layouts, viewPager);
        viewPager.setAdapter(sliderAdapter);
    }
}
