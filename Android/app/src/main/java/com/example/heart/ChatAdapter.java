package com.example.heart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessages;
    private String currentUserId;
    private String currentUserType;

    public ChatAdapter(Context context, List<ChatMessage> chatMessages,
                       String currentUserId, String currentUserType) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.currentUserId = currentUserId;
        this.currentUserType = currentUserType;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);

        // Check if the message is sent by the current user
        boolean isSentByCurrentUser = message.getSenderId().equals(currentUserId) &&
                message.getSenderType().equals(currentUserType);

        if (isSentByCurrentUser) {
            // Show sender (right-aligned) message
            holder.senderMessageLayout.setVisibility(View.VISIBLE);
            holder.receiverMessageLayout.setVisibility(View.GONE);

            holder.senderMessageText.setText(message.getMessage());
            holder.senderTimestamp.setText(message.getDateTime());
        } else {
            // Show receiver (left-aligned) message
            holder.senderMessageLayout.setVisibility(View.GONE);
            holder.receiverMessageLayout.setVisibility(View.VISIBLE);

            holder.receiverMessageText.setText(message.getMessage());
            holder.receiverTimestamp.setText(message.getDateTime());
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        notifyItemInserted(chatMessages.size() - 1);
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout senderMessageLayout;
        ConstraintLayout receiverMessageLayout;
        TextView senderMessageText;
        TextView senderTimestamp;
        TextView receiverMessageText;
        TextView receiverTimestamp;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageLayout = itemView.findViewById(R.id.senderMessageLayout);
            receiverMessageLayout = itemView.findViewById(R.id.receiverMessageLayout);
            senderMessageText = itemView.findViewById(R.id.senderMessageText);
            senderTimestamp = itemView.findViewById(R.id.senderTimestamp);
            receiverMessageText = itemView.findViewById(R.id.receiverMessageText);
            receiverTimestamp = itemView.findViewById(R.id.receiverTimestamp);
        }
    }
}