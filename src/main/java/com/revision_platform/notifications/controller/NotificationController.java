package com.revision_platform.notifications.controller;

import com.revision_platform.scheduler.DailyNotificationScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final DailyNotificationScheduler dailyNotificationScheduler;

    @PostMapping("/trigger")
    public ResponseEntity<String> triggerNotifications(@RequestParam(required = false) String email) {
        dailyNotificationScheduler.triggerNotifications(email, true);
        if (email != null && !email.trim().isEmpty()) {
            return ResponseEntity.ok("Daily notifications triggered successfully for user: " + email);
        }
        return ResponseEntity.ok("Daily notifications triggered successfully for all users.");
    }

    @PostMapping("/trigger-reminder")
    public ResponseEntity<String> triggerReminder(@RequestParam(required = false) String email) {
        dailyNotificationScheduler.triggerInactivityReminders(email);
        if (email != null && !email.trim().isEmpty()) {
            return ResponseEntity.ok("Inactivity reminder triggered successfully for user: " + email);
        }
        return ResponseEntity.ok("Inactivity reminders triggered successfully for all eligible users.");
    }
}
