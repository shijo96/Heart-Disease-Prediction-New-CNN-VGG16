package com.example.heart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User_chat extends AppCompatActivity implements JsonResponse {

    private SharedPreferences sh;
    public static String dlogid, ulogid;

    private EditText inputField;
    private ImageButton sendButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private MessagePoller messagePoller;

    private String input_chat;
    private int lastMessageId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_chat);

        // Initialize SharedPreferences
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dlogid = sh.getString("login_ids", "");
        ulogid = sh.getString("logid", "");

        // Initialize views
        inputField = findViewById(R.id.inputField);
        sendButton = findViewById(R.id.sendButton);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        // Initialize chat messages list and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages, ulogid, "User");

        // Set up RecyclerView
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Initialize message poller
        messagePoller = new MessagePoller(this, ulogid, dlogid);

        // Fetch existing chat messages
        fetchChatMessages();

        // Send button click listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_chat = inputField.getText().toString();
                if (!input_chat.isEmpty()) {
                    sendMessage();
                }
            }
        });

        // Set window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchChatMessages() {
        JsonReq JR = new JsonReq(User_chat.this);
        JR.json_response = this;
        String q = "/User_view_chat?ulogid=" + ulogid + "&dlogid=" + dlogid;
        JR.execute(q);
    }

    private void sendMessage() {
        JsonReq JR = new JsonReq(User_chat.this);
        JR.json_response = this;
        String encodedMessage = input_chat.replace(" ", "%20");
        String q = "/User_send_chat?ulogid=" + ulogid + "&dlogid=" + dlogid + "&input_chat=" + encodedMessage;
        JR.execute(q);

        // Clear input field
        inputField.setText("");
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");

            if (method.equalsIgnoreCase("User_view_chat")) {
                handleViewChatResponse(jo);
            }

            if (method.equalsIgnoreCase("User_send_chat")) {
                handleSendChatResponse(jo);
            }

            if (method.equalsIgnoreCase("User_view_new_chat")) {
                handleNewChatMessages(jo);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleViewChatResponse(JSONObject jo) throws Exception {
        String status = jo.getString("status");
        if (status.equalsIgnoreCase("success")) {
            JSONArray ja1 = jo.getJSONArray("data");

            // Clear existing messages
            chatMessages.clear();

            // Populate chat messages
            for (int i = 0; i < ja1.length(); i++) {
                JSONObject messageObj = ja1.getJSONObject(i);
                ChatMessage chatMessage = new ChatMessage(
                        messageObj.getString("sender_id"),
                        messageObj.getString("sender_type"),
                        messageObj.getString("receiver_id"),
                        messageObj.getString("receiver_type"),
                        messageObj.getString("message"),
                        messageObj.getString("date_time")
                );
                chatMessages.add(chatMessage);
            }

            // Update last message ID if messages exist
            if (!chatMessages.isEmpty()) {
                // Assuming the last message has the highest ID
                lastMessageId = Integer.parseInt(
                        ja1.getJSONObject(ja1.length() - 1).getString("chat_id")
                );
                messagePoller.updateLastMessageId(lastMessageId);
            }

            // Update UI
            runOnUiThread(() -> {
                chatAdapter.notifyDataSetChanged();
                // Scroll to the bottom of the chat
                if (!chatMessages.isEmpty()) {
                    chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                }

                // Start polling for new messages
                messagePoller.startPolling();
            });
        } else {
            Toast.makeText(getApplicationContext(), "No data!!", Toast.LENGTH_LONG).show();
        }
    }

    private void handleSendChatResponse(JSONObject jo) throws Exception {
        String status = jo.getString("status");
        if (status.equalsIgnoreCase("success")) {
            // Refresh chat messages after sending
            fetchChatMessages();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to send message!!", Toast.LENGTH_LONG).show();
        }
    }

    private void handleNewChatMessages(JSONObject jo) throws Exception {
        String status = jo.getString("status");
        if (status.equalsIgnoreCase("success")) {
            JSONArray ja1 = jo.getJSONArray("data");

            // Add new messages
            for (int i = 0; i < ja1.length(); i++) {
                JSONObject messageObj = ja1.getJSONObject(i);
                ChatMessage chatMessage = new ChatMessage(
                        messageObj.getString("sender_id"),
                        messageObj.getString("sender_type"),
                        messageObj.getString("receiver_id"),
                        messageObj.getString("receiver_type"),
                        messageObj.getString("message"),
                        messageObj.getString("date_time")
                );
                chatMessages.add(chatMessage);

                // Update last message ID
                lastMessageId = Integer.parseInt(messageObj.getString("chat_id"));
            }

            // Update UI
            runOnUiThread(() -> {
                chatAdapter.notifyItemRangeInserted(chatMessages.size() - ja1.length(), ja1.length());
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                messagePoller.updateLastMessageId(lastMessageId);
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop polling when activity is destroyed
        messagePoller.stopPolling();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Not used in this implementation
    }
}

