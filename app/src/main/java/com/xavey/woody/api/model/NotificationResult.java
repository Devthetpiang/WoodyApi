package com.xavey.woody.api.model;

public class NotificationResult {

    private String message;
    private Notifications notification;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Notifications getNotification() {
        return notification;
    }

    public void setNotification(Notifications notification) {
        this.notification = notification;
    }
}