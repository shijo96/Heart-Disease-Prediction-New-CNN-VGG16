package com.example.heart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {

    private ArrayList<String> complaints;
    private ArrayList<String> reply;
    private ArrayList<String> date_time;

    public ComplaintAdapter(ArrayList<String> complaints, ArrayList<String> reply, ArrayList<String> date_time) {
        this.complaints = complaints;
        this.reply = reply;
        this.date_time = date_time;
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_complaint, parent, false);
        return new ComplaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {
        if (position < complaints.size() && position < reply.size() && position < date_time.size()) {
            holder.complaintTextView.setText("Complaint : "+complaints.get(position));
            holder.replyTextView.setText("Reply : "+reply.get(position));
            holder.dateTimeTextView.setText(date_time.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(complaints.size(), Math.min(reply.size(), date_time.size()));
    }

    public static class ComplaintViewHolder extends RecyclerView.ViewHolder {
        TextView complaintTextView, replyTextView, dateTimeTextView;

        public ComplaintViewHolder(@NonNull View itemView) {
            super(itemView);
            complaintTextView = itemView.findViewById(R.id.tvComplaint);
            replyTextView = itemView.findViewById(R.id.tvReply);
            dateTimeTextView = itemView.findViewById(R.id.tvDateTime);
        }
    }
}

