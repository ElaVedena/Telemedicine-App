package com.database;

public class Message {
    private String sender;
    private String content;
    private Object timestamp;  // Use Object for the timestamp to handle ServerValue.TIMESTAMP

    // Default constructor for Firebase
    public Message() {}

    public Message(String sender, String content, Object timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}

