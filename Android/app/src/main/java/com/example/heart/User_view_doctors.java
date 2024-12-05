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

public class User_view_doctors extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    Spinner filterSpinner;
    String[] login_id, doctor_id, first_name, house_name, gender, place, landmark, qualification, phone, email, statuss;
    public static String login_ids, doctor_ids, genders, first_names, qualifications;
    FusedLocationProviderClient fusedLocationProviderClient;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_user_view_doctors);

        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.home));
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        l1 = findViewById(R.id.vdoctors);
        l1.setOnItemClickListener(this);

        filterSpinner = findViewById(R.id.filterSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.filter_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        filterSpinner.setSelection(0, false);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = parentView.getItemAtPosition(position).toString();
                if (!selectedOption.equals("Choose Filter")) {
                    getCurrentLocation(selectedOption);
                } else if (selectedOption.equals("Choose Filter")) {
                    JsonReq JR = new JsonReq(User_view_doctors.this);
                    JR.json_response = (JsonResponse) User_view_doctors.this;
                    String q = "/User_view_doctors";
                    JR.execute(q);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        JsonReq JR = new JsonReq(User_view_doctors.this);
        JR.json_response = (JsonResponse) User_view_doctors.this;
        String q = "/User_view_doctors";
        JR.execute(q);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getCurrentLocation(String selectedOption) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(User_view_doctors.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (!addresses.isEmpty()) {
                                String currentLocationName = addresses.get(0).getLocality();
                                JsonReq JR = new JsonReq(User_view_doctors.this);
                                JR.json_response = User_view_doctors.this;
                                String q = "/User_filter_doctors?filter=" + selectedOption + "&location=" + currentLocationName;
                                JR.execute(q);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(User_view_doctors.this, "Unable to fetch location!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            if (method.equalsIgnoreCase("User_view_doctors")) {
                String status = jo.getString("status");

                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = jo.getJSONArray("data");


                    if (ja1.length() == 0) {
//                        // No doctors available

                        // Show a no data message in the ListView
                        String[] placeholder = {"Sorry.. No doctors are available in your area."};
                        // Set a placeholder layout to display the message
                        ArrayAdapter<String> noDataAdapter = new ArrayAdapter<>(this, R.layout.no_data_layout, R.id.no_data_message, placeholder);
                        l1.setAdapter(noDataAdapter);

                    } else {
                        login_id = new String[ja1.length()];
                        doctor_id = new String[ja1.length()];
                        first_name = new String[ja1.length()];
                        house_name = new String[ja1.length()];
                        gender = new String[ja1.length()];
                        place = new String[ja1.length()];
                        landmark = new String[ja1.length()];
                        qualification = new String[ja1.length()];
                        phone = new String[ja1.length()];
                        email = new String[ja1.length()];
                        statuss = new String[ja1.length()];

                        for (int i = 0; i < ja1.length(); i++) {
                            login_id[i] = ja1.getJSONObject(i).getString("login_id");
                            doctor_id[i] = ja1.getJSONObject(i).getString("doctor_id");
                            first_name[i] = ja1.getJSONObject(i).getString("first_name") + " " + ja1.getJSONObject(i).getString("last_name");
                            house_name[i] = ja1.getJSONObject(i).getString("house_name");
                            gender[i] = ja1.getJSONObject(i).getString("gender");
                            place[i] = ja1.getJSONObject(i).getString("place");
                            landmark[i] = ja1.getJSONObject(i).getString("landmark");
                            qualification[i] = ja1.getJSONObject(i).getString("qualification");
                            phone[i] = ja1.getJSONObject(i).getString("phone");
                            email[i] = ja1.getJSONObject(i).getString("email");
                            statuss[i] = ja1.getJSONObject(i).getString("status");
                        }

                        Custom_doctors ar = new Custom_doctors(User_view_doctors.this, first_name, gender, qualification, email, statuss);
                        l1.setAdapter(ar);
                    }
                } else {


                    // Show a no data message in the ListView
                    String[] placeholder = {"Sorry.. No doctors are available in your area."};
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
        login_ids = login_id[arg2];
        doctor_ids = doctor_id[arg2];
        genders = gender[arg2];
        first_names = first_name[arg2];
        qualifications = qualification[arg2];

        SharedPreferences.Editor ed = sh.edit();
        ed.putString("login_ids", login_ids);
        ed.putString("doctor_ids", doctor_ids);
        ed.putString("genders", genders);
        ed.putString("first_names", first_names);
        ed.putString("qualifications", qualifications);
        ed.apply(); // Save the IP value asynchronously

        final CharSequence[] items = {"View Schedule", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(User_view_doctors.this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("View Schedule")) {
                startActivity(new Intent(getApplicationContext(), User_view_doctor_schedule.class));
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
