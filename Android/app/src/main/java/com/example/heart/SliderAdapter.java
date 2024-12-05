package com.example.heart;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private int[] layouts;
    private Context context;
    private ViewPager2 viewPager; // Add ViewPager2 reference

    public static String ip;
    SharedPreferences sh;
    EditText e1;


    // Constructor accepts Context, layout resource IDs, and ViewPager2
    public SliderAdapter(Context context, int[] layouts, ViewPager2 viewPager) {
        this.context = context;
        this.layouts = layouts;
        this.viewPager = viewPager; // Initialize ViewPager2
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout based on view type (position)
        View view = LayoutInflater.from(parent.getContext()).inflate(layouts[viewType], parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        // Handle binding based on slide position
//        if (position == 0) { // Home Slide
//            e1 = holder.itemView.findViewById(R.id.editText1);
//            sh = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//            e1.setText(sh.getString("ip", ""));
//
//            Button buttonNext = holder.itemView.findViewById(R.id.buttonNext);
//
//            buttonNext.setOnClickListener(v -> {
//                ip = e1.getText().toString();
//                SharedPreferences.Editor ed = sh.edit();
//                ed.putString("ip", ip);
//                ed.apply(); // Use apply() for async commit
//
//                // Navigate to the next slide (Login Slide)
//                viewPager.setCurrentItem(1, true);
//            });
//        }
//        else if (position == 1) { // Login Slide
//            EditText editTextUsername = holder.itemView.findViewById(R.id.editTextUsername);
//            EditText editTextPassword = holder.itemView.findViewById(R.id.editTextPassword);
//            Button buttonLogin = holder.itemView.findViewById(R.id.buttonLogin);
//
//            buttonLogin.setOnClickListener(v -> {
//                String username = editTextUsername.getText().toString().trim();
//                String password = editTextPassword.getText().toString().trim();
//
//                if (username.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(context, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
//                } else {
//                    String message = "Login:\nUsername: " + username + "\nPassword: " + password;
//                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//                    Toast.makeText(context, sh.getString("ip",""), Toast.LENGTH_LONG).show();
//                    sendLoginRequest(username, password); // Send login request
//                }
//            });
//        }

        if (position == 0) { // Home Slide
            e1 = holder.itemView.findViewById(R.id.editText1);
            sh = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

            // Load IP if it was previously saved
            e1.setText(sh.getString("ip", ""));

            Button buttonNext = holder.itemView.findViewById(R.id.buttonNext);
            buttonNext.setOnClickListener(v -> {
                saveIpToSharedPreferences();
                // Navigate to the next slide (Login Slide)
                viewPager.setCurrentItem(1, true);
            });

            // Add a listener to detect text changes in the IP EditText
            e1.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) { // Save IP when focus is lost (or when sliding to the next slide)
                    saveIpToSharedPreferences();
                }
            });
        }
        else if (position == 1) { // Login Slide
            EditText editTextUsername = holder.itemView.findViewById(R.id.editTextUsername);
            EditText editTextPassword = holder.itemView.findViewById(R.id.editTextPassword);
            Button buttonLogin = holder.itemView.findViewById(R.id.buttonLogin);

            buttonLogin.setOnClickListener(v -> {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                } else {
//                    String message = "Login:\nUsername: " + username + "\nPassword: " + password;
//                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//                    Toast.makeText(context, sh.getString("ip", ""), Toast.LENGTH_LONG).show();
                    sendLoginRequest(username, password); // Send login request
                }
            });
        }

        else if (position == 2) { // Registration Slide
            EditText editTextFname = holder.itemView.findViewById(R.id.editTextFname);
            EditText editTextLname = holder.itemView.findViewById(R.id.editTextLname);
            EditText editTextHname = holder.itemView.findViewById(R.id.editTextHname);
            EditText editTextPlace = holder.itemView.findViewById(R.id.editTextPlace);
            EditText editTextPhone = holder.itemView.findViewById(R.id.editTextPhone);
            EditText editTextRegEmail = holder.itemView.findViewById(R.id.editTextRegEmail);
            EditText editTextRegUsername = holder.itemView.findViewById(R.id.editTextRegUsername);
            EditText editTextRegPassword = holder.itemView.findViewById(R.id.editTextRegPassword);
            Button buttonRegister = holder.itemView.findViewById(R.id.buttonRegister);

            buttonRegister.setOnClickListener(v -> {
                String fname = editTextFname.getText().toString().trim();
                String lname = editTextLname.getText().toString().trim();
                String hname = editTextHname.getText().toString().trim();
                String place = editTextPlace.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String email = editTextRegEmail.getText().toString().trim();
                String username = editTextRegUsername.getText().toString().trim();
                String password = editTextRegPassword.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
//                    String message = "Register:\nUsername: " + username + "\nEmail: " + email + "\nPassword: " + password;
//                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    sendRegRequest(fname,lname,hname,place,phone,email,username, password); // Send login request
                }
            });
        }


    }





    @Override
    public int getItemCount() {
        return layouts.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    private void saveIpToSharedPreferences() {
        ip = e1.getText().toString();
        if (!ip.isEmpty()) {
            SharedPreferences.Editor ed = sh.edit();
            ed.putString("ip", ip);
            ed.apply(); // Save the IP value asynchronously
        }
    }

    // ViewHolder class
    static class SliderViewHolder extends RecyclerView.ViewHolder {
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    // Method to send login request to Flask backend
    @SuppressLint("StaticFieldLeak")
    private void sendLoginRequest(String username, String password) {
        // JSON format
        String json = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        // Create OkHttp client
        OkHttpClient client = new OkHttpClient();

        // Create request body with JSON data
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        // Create request
        Request request = new Request.Builder()
                .url("http://"+sh.getString("ip","")+"/api/login") // Replace with your Flask API URL
                .post(body)
                .build();

        // Execute the request in an AsyncTask (so it's not blocking the UI thread)
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Execute the request and get response
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string(); // Return the JSON response

                    } else {
                        return "Error: " + response.message();
                    }
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            }

//            @Override
//            protected void onPostExecute(String result) {
//                // Handle the response here
//                Log.d("Login Response", result);
//                Toast.makeText(context.getApplicationContext(), result,Toast.LENGTH_LONG).show();
//                // You can parse the result as JSON and show a toast or take action based on the response
//            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(result);

                    // Extract message and status
                    String message = jsonResponse.getString("message");
                    String status = jsonResponse.getString("status");
                    String logid = jsonResponse.getString("logid");
                    String uid = jsonResponse.getString("uid");

                    SharedPreferences.Editor ed = sh.edit();
                    ed.putString("logid", logid);
                    ed.putString("uid", uid);
                    ed.apply(); // Save the IP value asynchronously


                    // Show separate Toast messages for message and status
                    Toast.makeText(context.getApplicationContext(), "Message: " + message, Toast.LENGTH_LONG).show();
                    Toast.makeText(context.getApplicationContext(), "Status: " + status, Toast.LENGTH_LONG).show();

                    // Handle login success or error
                    if (status.equalsIgnoreCase("error")) {
                        // If status is "error", stay on the current slide (Login Slide)
                        Toast.makeText(context, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                        // You can navigate to another slide (if needed) based on the error, for example:
                        viewPager.setCurrentItem(1, true); // This keeps you on the login slide.
                    } else {
                        // If status is "success", navigate to the user home screen
//                        startActivity(new Intent(context, User_home.class));
                        Intent intent = new Intent(context, User_home.class);
                        context.startActivity(intent);

                        // Optionally, you can also finish the current activity to prevent the user from returning to the login screen
                        // finish();
                    }
                } catch (Exception e) {
                    // Handle any exceptions
                    Log.e("Login Response", "Error parsing response", e);
                    Toast.makeText(context.getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }



        }.execute();



    }



    // Method to send register request to Flask backend
    @SuppressLint("StaticFieldLeak")
    private void sendRegRequest(String fname, String lname, String hname, String place, String phone, String email, String username, String password) {
        // JSON format
//        String json = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        String json = "{"
                + "\"fname\": \"" + fname + "\", "
                + "\"lname\": \"" + lname + "\", "
                + "\"hname\": \"" + hname + "\", "
                + "\"place\": \"" + place + "\", "
                + "\"phone\": \"" + phone + "\", "
                + "\"email\": \"" + email + "\", "
                + "\"username\": \"" + username + "\", "
                + "\"password\": \"" + password + "\""
                + "}";


//        fname,lname,hname,place,phone,email,username, password
        // Create OkHttp client
        OkHttpClient client = new OkHttpClient();

        // Create request body with JSON data
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        // Create request
        Request request = new Request.Builder()
                .url("http://"+sh.getString("ip","")+"/api/register") // Replace with your Flask API URL
                .post(body)
                .build();

        // Execute the request in an AsyncTask (so it's not blocking the UI thread)
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Execute the request and get response
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string(); // Return the JSON response

                    } else {
                        return "Error: " + response.message();
                    }
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            }

//            @Override
//            protected void onPostExecute(String result) {
//                // Handle the response here
//                Log.d("Login Response", result);
//                Toast.makeText(context.getApplicationContext(), result,Toast.LENGTH_LONG).show();
//                // You can parse the result as JSON and show a toast or take action based on the response
//            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(result);

//                    // Extract message and status
                    String message = jsonResponse.getString("message");
                    String status = jsonResponse.getString("status");
//                    String logid = jsonResponse.getString("logid");
//
//                    SharedPreferences.Editor ed = sh.edit();
//                    ed.putString("logid", logid);
//                    ed.apply(); // Save the IP value asynchronously


                    // Show separate Toast messages for message and status
                    Toast.makeText(context.getApplicationContext(), "Message: " + message, Toast.LENGTH_LONG).show();
                    Toast.makeText(context.getApplicationContext(), "Status: " + status, Toast.LENGTH_LONG).show();

                    // Handle login success or error
                    if (status.equalsIgnoreCase("error")) {
                        // If status is "error", stay on the current slide (Login Slide)
                        Toast.makeText(context, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        // You can navigate to another slide (if needed) based on the error, for example:
                        viewPager.setCurrentItem(2, true); // This keeps you on the login slide.
                    } else {
                        // If status is "success", navigate to the user home screen
//                        startActivity(new Intent(context, User_home.class));
//                        Intent intent = new Intent(context, SliderAdapter.class);
//                        context.startActivity(intent);

                        // Navigate to the next slide (Login Slide)
                        viewPager.setCurrentItem(1, true);

                        // Optionally, you can also finish the current activity to prevent the user from returning to the login screen
                        // finish();
                    }
                } catch (Exception e) {
                    // Handle any exceptions
                    Log.e("Registration Response", "Error parsing response", e);
                    Toast.makeText(context.getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }



        }.execute();



    }



}
