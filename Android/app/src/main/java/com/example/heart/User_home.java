package com.example.heart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class User_home extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.home));
        }


//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }


        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Enable edge-to-edge layout adjustments
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up navigation item selection listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Handle home click
                Intent intent = new Intent(User_home.this, User_home.class);
                startActivity(intent);
            } else if (id == R.id.nav_profile) {
                // Handle profile click
                Intent intent = new Intent(User_home.this, User_profile.class);
                startActivity(intent);

            } else if (id == R.id.nav_doctors) {
                // Handle profile click
                Intent intent = new Intent(User_home.this, User_view_doctors.class);
                startActivity(intent);
            } else if (id == R.id.nav_bookings) {
                // Handle profile click
                Intent intent = new Intent(User_home.this, User_view_bookings.class);
                startActivity(intent);
            } else if (id == R.id.nav_disease_prediction) {
                // Handle settings click
                Intent intent = new Intent(User_home.this, User_disease_prediction.class);
                startActivity(intent);
            }else if (id == R.id.nav_history) {
                // Handle settings click
                Intent intent = new Intent(User_home.this, User_view_history.class);
                startActivity(intent);
            }else if (id == R.id.nav_complaints) {
                // Handle settings click
                Intent intent = new Intent(User_home.this, User_send_complaints.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                // Handle logout click
                Intent intent = new Intent(User_home.this, MainActivity.class);
                startActivity(intent);
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}