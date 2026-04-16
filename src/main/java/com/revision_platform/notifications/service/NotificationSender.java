package com.revision_platform.notifications.service;

public interface NotificationSender {
    void sendNotification(String to, String subject, String body);
}
