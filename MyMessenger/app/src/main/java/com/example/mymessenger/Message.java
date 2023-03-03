package com.example.mymessenger;

public class Message {

    private String senderId;
    private String receiverId;
    private String textMessage;

    public Message(String senderId, String receiverId, String textMessage) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.textMessage = textMessage;
    }

    public Message() {
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getTextMessage() {
        return textMessage;
    }
}
