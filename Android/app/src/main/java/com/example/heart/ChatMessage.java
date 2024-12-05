package com.example.heart;

public class ChatMessage {
    private String senderId;
    private String senderType;
    private String receiverId;
    private String receiverType;
    private String message;
    private String dateTime;

    public ChatMessage(String senderId, String senderType, String receiverId,
                       String receiverType, String message, String dateTime) {
        this.senderId = senderId;
        this.senderType = senderType;
        this.receiverId = receiverId;
        this.receiverType = receiverType;
        this.message = message;
        this.dateTime = dateTime;
    }

    // Getters and setters
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getReceiverType() { return receiverType; }
    public void setReceiverType(String receiverType) { this.receiverType = receiverType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
}