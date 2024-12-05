//package com.example.heart;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.Base64;
//
//public class User_disease_prediction extends AppCompatActivity {
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private ImageButton imageButton;
//    private ImageView imageView;
//    private Button uploadButton;
//    private TextView resultText;
//    private Uri imageUri;
//    private View cardView;
//
//    SharedPreferences sh;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_user_disease_prediction);
//
//        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//
//        imageButton = findViewById(R.id.imageButton);
//        imageView = findViewById(R.id.imageView);
//        uploadButton = findViewById(R.id.button);
//        resultText = findViewById(R.id.textView3);
//        cardView = findViewById(R.id.cardView);  // Assuming cardView has id cardView
//
//        // Hide result and cardView initially
//        resultText.setVisibility(View.GONE);
//        cardView.setVisibility(View.GONE);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // Set up ImageButton to open gallery
//        imageButton.setOnClickListener(view -> openGallery());
//
//        // Set up Upload button
//        uploadButton.setOnClickListener(view -> {
//            if (imageUri != null) {
//                uploadImageToServer();
//            } else {
//                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // Method to open the gallery
//    private void openGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                imageButton.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Method to upload image to server
//    private void uploadImageToServer() {
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] imageBytes = baos.toByteArray();
//            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
//
//            // Create JSON payload
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("image", encodedImage);
//
//            // Flask server URL
//            String url = "http://+"+sh.getString("ip","")+"/api/upload_file";
//
//
//            // Send JSON request
//            RequestQueue queue = Volley.newRequestQueue(this);
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            handleServerResponse(response);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(User_disease_prediction.this, "Upload failed", Toast.LENGTH_SHORT).show();
//                    displayFailure();
//                }
//            });
//
//            queue.add(jsonObjectRequest);
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Handle server response
//    private void handleServerResponse(JSONObject response) {
//        try {
//            String status = response.getString("status");
//            String result = response.getString("result");
//
//            if ("success".equals(status)) {
//                resultText.setText(result);
//                resultText.setVisibility(View.VISIBLE);
//                cardView.setVisibility(View.VISIBLE);
//
//                // Display the uploaded image in imageView
//                imageView.setImageURI(imageUri);
//            } else {
//                displayFailure();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            displayFailure();
//        }
//    }
//
//    // Display failure message and image
//    private void displayFailure() {
//        resultText.setText("No result found");
//        resultText.setVisibility(View.VISIBLE);
//        cardView.setVisibility(View.GONE);
//
//        // Display the selected image in imageView
//        imageView.setImageURI(imageUri);
//    }
//}




package com.example.heart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class User_disease_prediction extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton imageButton;
    private ImageView imageView;
    private Button uploadButton;
    private TextView resultText;
    private Uri imageUri;
    private View cardView;

    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_disease_prediction);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        imageButton = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.imageView);
        uploadButton = findViewById(R.id.button);
        resultText = findViewById(R.id.textView3);
        cardView = findViewById(R.id.cardView);  // Assuming cardView has id cardView

        // Hide result and cardView initially
        resultText.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up ImageButton to open gallery
        imageButton.setOnClickListener(view -> openGallery());

        // Set up Upload button
        uploadButton.setOnClickListener(view -> {
            if (imageUri != null) {
                uploadImageToServer();
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to open the gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageButton.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to upload image to server
    private void uploadImageToServer() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

            // Create JSON payload
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("image", encodedImage);
            jsonObject.put("uid", sh.getString("uid",""));


            // Flask server URL
            String url = "http://" + sh.getString("ip", "") + "/api/upload_file";

            // Send JSON request
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            handleServerResponse(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(User_disease_prediction.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    displayFailure();
                }
            });

            queue.add(jsonObjectRequest);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    // Handle server response
    private void handleServerResponse(JSONObject response) {
        try {
            String status = response.getString("status");
            String result = response.getString("result");

            if ("success".equals(status)) {
                resultText.setText(result);
                resultText.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);

                // Display the uploaded image in imageView
                imageView.setImageURI(imageUri);
            } else {
                displayFailure();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            displayFailure();
        }
    }

    // Display failure message and image
    private void displayFailure() {
        resultText.setText("No result found");
        resultText.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);

        // Display the selected image in imageView
        imageView.setImageURI(imageUri);
    }
}
