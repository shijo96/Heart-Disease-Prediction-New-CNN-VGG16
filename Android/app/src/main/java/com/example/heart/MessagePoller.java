package com.example.heart;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class MessagePoller {
    private User_chat activity;
    private Handler handler;
    private boolean isPolling = false;
    private static final int POLL_INTERVAL = 3000; // 3 seconds
    private String ulogid;
    private String dlogid;
    private int lastMessageId = 0;

    public MessagePoller(User_chat activity, String ulogid, String dlogid) {
        this.activity = activity;
        this.ulogid = ulogid;
        this.dlogid = dlogid;
        this.handler = new Handler(Looper.getMainLooper());
    }

    private Runnable pollRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isPolling) return;

            // Fetch new messages
            JsonReq JR = new JsonReq(activity);
            JR.json_response = activity;
            String q = "/User_view_new_chat?ulogid=" + ulogid +
                    "&dlogid=" + dlogid +
                    "&last_message_id=" + lastMessageId;
            JR.execute(q);

            // Schedule next poll
            handler.postDelayed(this, POLL_INTERVAL);
        }
    };

    public void startPolling() {
        if (!isPolling) {
            isPolling = true;
            handler.post(pollRunnable);
        }
    }

    public void stopPolling() {
        isPolling = false;
        handler.removeCallbacks(pollRunnable);
    }

    public void updateLastMessageId(int newLastMessageId) {
        this.lastMessageId = newLastMessageId;
    }
}