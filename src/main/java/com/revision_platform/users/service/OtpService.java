package com.revision_platform.users.service;

import com.revision_platform.notifications.service.NotificationSender;
import com.revision_platform.users.entity.OtpRecord;
import com.revision_platform.users.repository.OtpRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final NotificationSender notificationSender;
    private final OtpRecordRepository otpRecordRepository;
    
    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendBaseUrl;
    
    private final SecureRandom secureRandom = new SecureRandom();
    
    // 5 minutes
    private static final long EXPIRE_DURATION_MILLIS = 5 * 60 * 1000;

    public void generateAndSendOtp(String email) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null) {
            log.warn("Skipping OTP generation due to invalid email input");
            return;
        }

        // Generate a 4-digit code
        int code = 1000 + secureRandom.nextInt(9000);
        String otp = String.valueOf(code);
        
        OtpRecord record = otpRecordRepository.findByEmail(normalizedEmail).orElse(new OtpRecord());
        record.setEmail(normalizedEmail);
        record.setOtp(otp);
        record.setExpirationTimeMillis(System.currentTimeMillis() + EXPIRE_DURATION_MILLIS);
        otpRecordRepository.save(record);

        String subject = "Your Verification Code - Premium DSA";
        String appUrl = frontendBaseUrl;
        String feedbackUrl = frontendBaseUrl + "/feedback?email=" + normalizedEmail;
        String unsubscribeUrl = appUrl + "/unsubscribe?email=" + normalizedEmail;
        String body = "<div style='font-family: sans-serif; max-width: 600px; padding: 20px;'>" +
                "<h1 style='color: #8b5cf6;'>Verification Required</h1>" +
                "<p>Your 4-digit code to enable daily DSA revision missions is:</p>" +
                "<h2 style='font-size: 32px; letter-spacing: 4px;'>" + otp + "</h2>" +
                "<p>This code will expire in 5 minutes. If you did not request this, please ignore it.</p>" +
                "<p style='margin-top: 18px;'>Have suggestions or hit an issue? <a href='" + feedbackUrl + "'>Share feedback</a>.</p>" +
                "<p style='margin-top: 8px;'>Do not want daily emails? <a href='" + unsubscribeUrl + "'>Pause / unsubscribe</a>.</p>" +
                "</div>";

        notificationSender.sendNotification(normalizedEmail, subject, body);
        log.info("Dispatched OTP to: {}", normalizedEmail);
    }

    public boolean verifyOtp(String email, String userInputCode) {
        String normalizedEmail = normalizeEmail(email);
        String normalizedOtp = normalizeOtp(userInputCode);
        if (normalizedEmail == null || normalizedOtp == null) {
            return false;
        }

        OtpRecord data = otpRecordRepository.findByEmail(normalizedEmail).orElse(null);
        if (data == null) {
            return false;
        }
        
        if (System.currentTimeMillis() > data.getExpirationTimeMillis()) {
            otpRecordRepository.delete(data);
            return false; // Expired
        }
        
        if (data.getOtp().equals(normalizedOtp)) {
            otpRecordRepository.delete(data); // Successful verification consumes the OTP
            return true;
        }
        
        return false;
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        String normalized = email.trim().toLowerCase();
        return normalized.isEmpty() ? null : normalized;
    }

    private String normalizeOtp(String otp) {
        if (otp == null) return null;
        String normalized = otp.replaceAll("\\s+", "");
        return normalized.isEmpty() ? null : normalized;
    }
}
