package com.example.heart;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class User_view_history extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;

    String[] history_id, user_id, file_path, result, date_time;

    SharedPreferences sh;
    public static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_user_view_history);

        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        uid = sh.getString("uid","");

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.home));
        }


        l1 = findViewById(R.id.vdoctors);
        l1.setOnItemClickListener(this);

        JsonReq JR = new JsonReq(User_view_history.this);
        JR.json_response = (JsonResponse) User_view_history.this;
        String q = "/User_view_history?uid="+uid;
        JR.execute(q);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            if (method.equalsIgnoreCase("User_view_history")) {
                String status = jo.getString("status");

                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = jo.getJSONArray("data");


                    if (ja1.length() == 0) {
//                        // No doctors available

                        // Show a no data message in the ListView
                        String[] placeholder = {"Sorry.. No history available."};
                        // Set a placeholder layout to display the message
                        ArrayAdapter<String> noDataAdapter = new ArrayAdapter<>(this, R.layout.no_data_layout, R.id.no_data_message, placeholder);
                        l1.setAdapter(noDataAdapter);

                    } else {
                        history_id = new String[ja1.length()];
                        user_id = new String[ja1.length()];
                        file_path = new String[ja1.length()];
                        result = new String[ja1.length()];
                        date_time = new String[ja1.length()];



                        for (int i = 0; i < ja1.length(); i++) {
                            history_id[i] = ja1.getJSONObject(i).getString("history_id");
                            user_id[i] = ja1.getJSONObject(i).getString("user_id");
                            file_path[i] = ja1.getJSONObject(i).getString("file_path");
                            result[i] = ja1.getJSONObject(i).getString("result");
                            date_time[i] = ja1.getJSONObject(i).getString("date_time");

                        }

                        Custom_history ar = new Custom_history(User_view_history.this, file_path, result, date_time);
                        l1.setAdapter(ar);
                    }
                } else {


                    // Show a no data message in the ListView
                    String[] placeholder = {"Sorry.. No history available."};
                    // Set a placeholder layout to display the message
                    ArrayAdapter<String> noDataAdapter = new ArrayAdapter<>(this, R.layout.no_data_layout, R.id.no_data_message, placeholder);
                    l1.setAdapter(noDataAdapter);
                }


            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }
}
