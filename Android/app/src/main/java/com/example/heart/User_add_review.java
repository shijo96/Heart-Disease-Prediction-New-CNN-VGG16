package com.example.heart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class User_add_review extends AppCompatActivity implements JsonResponse {

    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button submitButton;
    SharedPreferences sh;
    public static String doc_id,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_add_review);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Initialize the UI components
        ratingBar = findViewById(R.id.ratingBar);
        reviewEditText = findViewById(R.id.reviewEditText);
        submitButton = findViewById(R.id.submitButton);

         doc_id = sh.getString("doctor_ids","");
         uid = sh.getString("uid","");
         Toast.makeText(getApplicationContext(),"doc_id : "+doc_id+"\nuid : "+uid,Toast.LENGTH_LONG).show();


        JsonReq JR= new JsonReq(User_add_review.this);
        JR.json_response=(JsonResponse)User_add_review.this;
        String q="/User_view_review?uid=" + uid+"&doc_id="+doc_id;
        JR.execute(q);


        // Set onClick listener for the Submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the rating value
                float rating = ratingBar.getRating();

                // Get the review text
                String review = reviewEditText.getText().toString().trim();

                // Validate inputs
                if (rating == 0) {
                    Toast.makeText(User_add_review.this, "Please provide a rating.", Toast.LENGTH_SHORT).show();
                } else if (review.isEmpty()) {
                    Toast.makeText(User_add_review.this, "Please write a review.", Toast.LENGTH_SHORT).show();
                } else {

                    String rting = String.valueOf(rating);
                    // Process the rating and review (e.g., save to database or send to server)
                    submitReview(rting, review,doc_id,uid);

//                    // Provide feedback to the user
//                    Toast.makeText(User_add_review.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
//
//                    // Optionally, clear the inputs
//                    ratingBar.setRating(0);
//                    reviewEditText.setText("");
                }
            }
        });
    }

    // Method to handle the rating and review submission

    @SuppressLint("StaticFieldLeak")
    private void submitReview(String rting, String review, String doc_id, String uid) {
        // JSON format
        String json = "{" +
                "\"rting\": \"" + rting + "\", " +
                "\"review\": \"" + review + "\", " +
                "\"doc_id\": \"" + doc_id + "\", " +
                "\"uid\": \"" + uid + "\"" +
                "}";



        // Create OkHttp client
        OkHttpClient client = new OkHttpClient();

        // Create request body with JSON data
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        // Create request
        Request request = new Request.Builder()
                .url("http://"+sh.getString("ip","")+"/api/User_add_review") // Replace with your Flask API URL
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
//            @Override
            protected void onPostExecute(String result) {
                try {
                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(result);

                    String message = jsonResponse.getString("message");
                    String status = jsonResponse.getString("status");

                    Toast.makeText(User_add_review.this, message, Toast.LENGTH_LONG).show();

                    if (status.equalsIgnoreCase("success")) {
                        // Clear the rating and review input
                        ratingBar.setRating(0);
                        reviewEditText.setText("");

                        // Navigate to the desired activity if necessary
                        startActivity(new Intent(User_add_review.this, User_home.class));
                        finish(); // Prevent returning to this activity
                    }
                } catch (Exception e) {
                    Log.e("Response", "Error parsing response", e);
                    Toast.makeText(User_add_review.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


//            protected void onPostExecute(String result) {
//                try {
//                    // Parse the JSON response
//                    JSONObject jsonResponse = new JSONObject(result);
//
//                    // Extract message and status
//                    String message = jsonResponse.getString("message");
//                    String status = jsonResponse.getString("status");
//
//
//                    // Show separate Toast messages for message and status
//                    Toast.makeText(getApplicationContext().getApplicationContext(), "Message: " + message, Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext().getApplicationContext(), "Status: " + status, Toast.LENGTH_LONG).show();
//
//                    // Handle login success or error
//                    if (status.equalsIgnoreCase("error")) {
//                        // If status is "error", stay on the current slide (Login Slide)
//                        Toast.makeText(getApplicationContext(), "Failed. Please try again.", Toast.LENGTH_SHORT).show();
//                        // You can navigate to another slide (if needed) based on the error, for example:
////                        viewPager.setCurrentItem(1, true); // This keeps you on the login slide.
//                        Intent intent = new Intent(getApplicationContext(), User_add_review.class);
//                        getApplicationContext().startActivity(intent);
//                    } else {
//                        // If status is "success", navigate to the user home screen
////                        startActivity(new Intent(context, User_home.class));
//                        Intent intent = new Intent(getApplicationContext(), User_add_review.class);
//                        getApplicationContext().startActivity(intent);
//
//                        // Optionally, you can also finish the current activity to prevent the user from returning to the login screen
//                        // finish();
//                    }
//                } catch (Exception e) {
//                    // Handle any exceptions
//                    Log.e("Response", "Error parsing response", e);
//                    Toast.makeText(getApplicationContext().getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
//                }
//            }



        }.execute();



    }

    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {
            String status = jo.getString("status");
            String method = jo.getString("method");
            Log.d("result", status);
            if (method.equalsIgnoreCase("User_view_review")) {

                if (status.equalsIgnoreCase("success")) {

                    JSONArray ja = (JSONArray) jo.getJSONArray("data");

                    // Get the rating value from the JSON object
                    float ratingValue = (float) ja.getJSONObject(0).getDouble("rate");

                    // Set the rating on the RatingBar
                    ratingBar.setRating(ratingValue);

                    reviewEditText.setText(ja.getJSONObject(0).getString("review"));

                } else {
//                     Optionally, clear the inputs
                    ratingBar.setRating(0);
                    reviewEditText.setText("");
                    Toast.makeText(getApplicationContext(), "Sorry. No Data", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}




