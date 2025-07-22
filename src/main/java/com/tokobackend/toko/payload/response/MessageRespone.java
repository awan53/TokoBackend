package com.tokobackend.toko.payload.response;

public class MessageRespone {
    private String message;

    public MessageRespone(String message) {
        this.message = message;
    }

    // Getter dan Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
