package com.example.heart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class User_send_complaints extends AppCompatActivity implements JsonResponse {

    private RecyclerView complaintList;
    private EditText inputComplaint;
    private Button sendButton;
    private ComplaintAdapter adapter;
    private ArrayList<String> complaints, reply, date_time;
    SharedPreferences sh;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_complaints);

        // Initialize SharedPreferences
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = sh.getString("uid", "");

        // Initialize UI components
        complaintList = findViewById(R.id.complaintList);
        inputComplaint = findViewById(R.id.inputComplaint);
        sendButton = findViewById(R.id.sendButton);

        // Initialize lists
        complaints = new ArrayList<>();
        reply = new ArrayList<>();
        date_time = new ArrayList<>();

        // Initialize the adapter
        adapter = new ComplaintAdapter(complaints, reply, date_time);

        // Set up RecyclerView with a LinearLayoutManager
        complaintList.setLayoutManager(new LinearLayoutManager(this));
        complaintList.setAdapter(adapter);

        // Fetch complaints when the activity is created
        fetchComplaints();

        // Set send button click listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaint = inputComplaint.getText().toString().trim();
                if (!complaint.isEmpty()) {
                    sendComplaint(complaint);
                } else {
                    Toast.makeText(User_send_complaints.this, "Please enter a complaint", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchComplaints() {
        try {
            JsonReq JR = new JsonReq(User_send_complaints.this);
            JR.json_response = this;
            String query = "/User_view_complaints?user_id=" + userId;
            JR.execute(query);
        } catch (Exception e) {
            Toast.makeText(this, "Error fetching complaints: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendComplaint(String complaintText) {
        try {
            JsonReq JR = new JsonReq(User_send_complaints.this);
            JR.json_response = this;
            String query = "/User_send_complaint?user_id=" + userId + "&complaint=" + complaintText;
            JR.execute(query);
        } catch (Exception e) {
            Toast.makeText(this, "Error sending complaint: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            if (method.equalsIgnoreCase("User_view_complaints")) {
                handleFetchResponse(jo);
            } else if (method.equalsIgnoreCase("User_send_complaint")) {
                handleSendResponse(jo);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error processing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void handleFetchResponse(JSONObject jo) throws Exception {
        String status = jo.getString("status");
        if (status.equalsIgnoreCase("success")) {
            JSONArray data = jo.getJSONArray("data");
            complaints.clear();
            reply.clear();
            date_time.clear();
            for (int i = 0; i < data.length(); i++) {
                complaints.add(data.getJSONObject(i).getString("complaint"));
                reply.add(data.getJSONObject(i).getString("reply"));
                date_time.add(data.getJSONObject(i).getString("date_time"));
            }
            adapter.notifyDataSetChanged();

            // Scroll to the last item
            if (!complaints.isEmpty()) {
                complaintList.scrollToPosition(complaints.size() - 1);
            }
        } else {
            Toast.makeText(this, "No complaints found", Toast.LENGTH_SHORT).show();
        }
    }


    private void handleSendResponse(JSONObject jo) throws Exception {
        String status = jo.getString("status");
        if (status.equalsIgnoreCase("success")) {
            String complaintText = jo.getString("complaint");
            String currentDateTime = jo.getString("date_time"); // Assuming the server returns the date_time for the new complaint
            String replyText = jo.getString("reply"); // Assuming the server returns the date_time for the new complaint
//            String replyText = ""; // Assuming the initial reply is empty for new complaints

            // Add the new complaint to the lists
            complaints.add(complaintText);
            reply.add(replyText);
            date_time.add(currentDateTime);

            // Notify the adapter to update the RecyclerView
            adapter.notifyItemInserted(complaints.size() - 1);

            // Scroll to the newly added item
            complaintList.scrollToPosition(complaints.size() - 1);

            // Clear the input field
            inputComplaint.setText("");

            // Display a success message
            Toast.makeText(this, "Complaint sent successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to send complaint", Toast.LENGTH_SHORT).show();
        }
    }


//    private void handleSendResponse(JSONObject jo) throws Exception {
//        String status = jo.getString("status");
//        if (status.equalsIgnoreCase("success")) {
//            String complaintText = jo.getString("complaint");
//            complaints.add(complaintText);
//            adapter.notifyItemInserted(complaints.size() - 1);
//            complaintList.scrollToPosition(complaints.size() - 1);
//            inputComplaint.setText("");
//            Toast.makeText(this, "Complaint sent successfully", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Failed to send complaint", Toast.LENGTH_SHORT).show();
//        }
//    }
}
