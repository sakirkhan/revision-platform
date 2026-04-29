package com.revision_platform.scheduler;

import com.revision_platform.questions.entity.Question;
import com.revision_platform.questions.service.QuestionService;
import com.revision_platform.users.entity.User;
import com.revision_platform.users.repository.UserRepository;
import com.revision_platform.notifications.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class DailyNotificationScheduler {

    private final UserRepository userRepository;
    private final QuestionService questionService;
    private final NotificationSender notificationSender;
    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendBaseUrl;

    // Run every minute to check who needs to be notified
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata")
    public void scheduleDailyNotifications() {
        triggerNotifications(null, false);
    }

    public void triggerNotifications(String email, boolean force) {
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Kolkata")).withSecond(0).withNano(0);
        log.info("Running scheduler at IST: {}", now);

        List<User> users;
        if (email != null && !email.trim().isEmpty()) {
            users = userRepository.findAll().stream()
                    .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                    .toList();
            if (users.isEmpty()) {
                log.warn("User with email {} not found for targeted trigger.", email);
            }
        } else {
            users = userRepository.findAll();
        }

        for (User user : users) {
             if (user.getUserConfig() == null || (user.getUserConfig().getIsActive() != null && !user.getUserConfig().getIsActive())) {
                 continue; // Skip inactive users or users lacking configs
             }

            // Default to 10:00 AM if not set
            LocalTime notificationTime = LocalTime.of(10, 0);
            if (user.getUserConfig().getNotificationTime() != null) {
                notificationTime = user.getUserConfig().getNotificationTime();
            }

            if (force || notificationTime.equals(now)) {
                log.info("Processing user: {}", user.getEmail());
                processUserWithRetries(user, 3);
            }
        }
    }

    private void processUserWithRetries(User user, int retries) {
        try {
            List<Question> questions = questionService.getQuestionsForUser(user);
            if (!questions.isEmpty()) {
                List<com.revision_platform.questions.entity.UserQuestionHistory> historyRecords = questionService.recordQuestionsSent(user, questions);
                
                String body = formatEmailBody(historyRecords, false);
                notificationSender.sendNotification(user.getEmail(), "Your Daily Revision Questions", body);
            }
        } catch (com.revision_platform.questions.exception.ConfigExhaustedException ce) {
            log.warn("User {} config exhausted. Sending warning email.", user.getEmail());
            String warningHtml = "<!DOCTYPE html><html><body><h2>Your Configured Study Plan is Complete!</h2><p>Hello! You have successfully finished all available questions for your chosen topics and difficulty levels.</p><p>We have paused your daily questions. If you wish to continue receiving questions, please log in and <b>update your preferences</b>, or we'll begin sending random fallback questions soon!</p><p><a href='" + frontendBaseUrl + "'>Update Preferences Now</a></p></body></html>";
            notificationSender.sendNotification(user.getEmail(), "Action Required: Your DSA Study Plan is Exhausted", warningHtml);
        } catch (Exception e) {
            log.error("Failed to process user: {}", user.getEmail(), e);
            if (retries > 0) {
                processUserWithRetries(user, retries - 1);
            }
        }
    }

    private String formatEmailBody(List<com.revision_platform.questions.entity.UserQuestionHistory> records, boolean isReminder) {
        String userEmail = records.isEmpty() ? "" : records.get(0).getUser().getEmail();
        String appUrl = frontendBaseUrl;
        String updatePreferencesUrl = appUrl;
        String feedbackUrl = userEmail.isBlank() ? appUrl + "/feedback" : appUrl + "/feedback?email=" + userEmail;
        String unsubscribeUrl = userEmail.isBlank() ? appUrl + "/unsubscribe" : appUrl + "/unsubscribe?email=" + userEmail;
        String title = isReminder ? "Don't break your streak!" : "Your Daily Revision";
        String subtitle = isReminder ? "You still have unattempted questions locally waiting for you today. Log in and crush them!" : "Here are your curated revision questions for today to keep your skills sharp!";
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><style>")
            .append("body{font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;background-color:#f4f7f6;color:#333;line-height:1.6;margin:0;padding:20px;}")
            .append(".container{max-width:600px;margin:0 auto;background-color:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 10px 15px -3px rgba(0,0,0,0.1);}")
            .append(".header{background-color:#4f46e5;color:#ffffff;padding:25px;text-align:center;border-bottom:5px solid #4338ca;}")
            .append(".header h1{margin:0;font-size:26px;letter-spacing:1px;}")
            .append(".content{padding:35px;}")
            .append(".greeting{font-size:18px;margin-top:0;color:#1e293b;}")
            .append(".question-card{background-color:#f8fafc;border:1px solid #e2e8f0;border-left:5px solid #4f46e5;padding:20px;margin-bottom:20px;border-radius:6px;transition:transform 0.2s ease;}")
            .append(".question-title{font-size:19px;font-weight:700;margin-top:0;margin-bottom:12px;color:#0f172a;}")
            .append(".badge{display:inline-block;padding:5px 10px;border-radius:9999px;font-size:11px;font-weight:800;text-transform:uppercase;margin-right:8px;letter-spacing:0.5px;}")
            .append(".badge-easy{background-color:#dcfce7;color:#166534;}")
            .append(".badge-medium{background-color:#fef08a;color:#854d0e;}")
            .append(".badge-hard{background-color:#fee2e2;color:#991b1b;}")
            .append(".badge-topic{background-color:#e0e7ff;color:#3730a3;}")
            .append(".action-button{display:inline-block;padding:12px 24px;background-color:#4f46e5;color:#ffffff !important;text-decoration:none;border-radius:8px;font-weight:700;margin-top:18px;transition:background-color 0.2s ease;}")
            .append(".footer{background-color:#f1f5f9;padding:20px;text-align:center;font-size:13px;color:#64748b;border-top:1px solid #e2e8f0;}")
            .append("</style></head><body>")
            .append("<div class='container'>")
            .append("<div class='header'><h1>").append(title).append("</h1></div>")
            .append("<div class='content'>")
            .append("<p class='greeting'>Hello there,</p>")
            .append("<p>").append(subtitle).append("</p>")
            .append("<br>");

        for (com.revision_platform.questions.entity.UserQuestionHistory record : records) {
            Question q = record.getQuestion();
            String trackingUrl = appUrl + "/api/track/click?historyId=" + record.getId();
            
            String difficultyClass = "badge-" + q.getDifficulty().name().toLowerCase();
            String topicName = q.getTopic() != null ? q.getTopic().getName() : "General";
            html.append("<div class='question-card'>")
                .append("<h3 class='question-title'>").append(q.getTitle()).append("</h3>");
                
            if (q.getDescription() != null && !q.getDescription().isEmpty()) {
                html.append("<p style='font-size: 14px; color: #475569; margin-top: 0; margin-bottom: 15px;'>")
                    .append(q.getDescription())
                    .append("</p>");
            }
                
            html.append("<div style='margin-bottom: 15px;'>")
                .append("<span class='badge ").append(difficultyClass).append("'>").append(q.getDifficulty().name()).append("</span>")
                .append("</div>")
                .append("<div>")
                .append("<a href='").append(trackingUrl).append("&dest=leetcode' class='action-button' style='background-color: #4f46e5; margin-right: 12px;' target='_blank'>Solve on LeetCode</a>");
            
            if (q.getNeetcodeUrl() != null && !q.getNeetcodeUrl().isEmpty()) {
                html.append("<a href='").append(trackingUrl).append("&dest=neetcode' class='action-button' style='background-color: #10b981;' target='_blank'>Solve on NeetCode</a>")
                    .append("<p style='font-size: 12px; color: #64748b; font-style: italic; margin-top: 15px; margin-bottom: 0;'>* Note: If this LeetCode question requires premium, you can solve it on NeetCode instead.</p>");
            }
            html.append("</div></div>");
        }

        // --- Fetch and inject meme ---
        String[][] hindiMemes = {
            {"Arey bhai, abhi coding nahi karega to kab karega?", "https://media.giphy.com/media/LmNwrBhejkK9EFP504/giphy.gif"},
            {"Jo daily karta rahega kaam, wo kabhi nahi hoga fail! 🚀", "https://media.giphy.com/media/13HgwGsXF0aiGY/giphy.gif"},
            {"Bug fix karte karte subah ho gayi bro... 😭", "https://media.giphy.com/media/xT9IgzoKnwFNmISR8I/giphy.gif"},
            {"Jab production code first time mein chal jaye! 🕺", "https://media.giphy.com/media/blSTtZehjAZ8I/giphy.gif"},
            {"Bhai bas ek chota sa feature hai, 5 minute lagenge... 😅", "https://media.giphy.com/media/3o6wrebnKWmvx4ZBio/giphy.gif"},
            {"Production me fat gaya toh mera naam mat lena! 🏃", "https://media.giphy.com/media/jUwpcgza92QC8/giphy.gif"},
            {"Weekend plan: Neetcode 150... Reality: 😴", "https://media.giphy.com/media/mguPrVJAnEHIY/giphy.gif"},
            {"Me explaining my code to the senior developer 🤯", "https://media.giphy.com/media/l0IylOPIQiGW9ODlK/giphy.gif"},
            {"Bina StackOverflow ke ek line code bhi likh du toh... 😂", "https://media.giphy.com/media/YQitE4YNQxWuOhmDfb/giphy.gif"}
        };
        
        int randomIndex = new Random().nextInt(hindiMemes.length);
        String memeTitle = hindiMemes[randomIndex][0];
        String memeUrl = hindiMemes[randomIndex][1];

        html.append("<div style='margin-top: 35px; text-align: center; background: #fff; padding: 20px; border-radius: 12px; border: 1px solid #e2e8f0;'>")
            .append("<h4 style='margin-top: 0; color: #4f46e5; font-size: 16px;'>Daily Brain Break ☕</h4>")
            .append("<p style='font-size: 14px; color: #475569;'>").append(memeTitle).append("</p>")
            .append("<img src='").append(memeUrl).append("' alt='Tech Meme' style='max-width: 100%; height: auto; border-radius: 8px;' />")
            .append("</div>");

        html.append("<p style='margin-top: 35px;'>Happy coding and keep grinding!</p>")
            .append("</div>")
            .append("<div style='background-color: #eef2f6; padding: 30px 20px; text-align: center; border-top: 2px dashed #cbd5e1;'>")
            .append("<h4 style='margin-top: 0; color: #475569; font-size: 16px; margin-bottom: 20px;'>Manage Your Daily Prep</h4>")
            .append("<a href='").append(updatePreferencesUrl).append("' style='display: inline-block; padding: 10px 15px; background-color: #ffffff; color: #4f46e5; text-decoration: none; border-radius: 6px; font-weight: bold; border: 1px solid #4f46e5; margin: 5px;'>⚙️ Update Plan</a>")
            .append("<a href='").append(unsubscribeUrl).append("' style='display: inline-block; padding: 10px 15px; background-color: #fee2e2; color: #dc2626; text-decoration: none; border-radius: 6px; font-weight: bold; border: 1px solid #ef4444; margin: 5px;'>⏸️ Pause Emails</a>")
            .append("<a href='").append(feedbackUrl).append("' style='display: inline-block; padding: 10px 15px; background-color: #ffffff; color: #059669; text-decoration: none; border-radius: 6px; font-weight: bold; border: 1px solid #059669; margin: 5px;'>💡 Feedback</a>")
            .append("</div>")
            .append("<div class='footer'>")
            .append("&copy; 2026 Revision Platform. All rights reserved.")
            .append("</div>")
            .append("</div></body></html>");
        
        return html.toString();
    }

    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Kolkata") // 9 PM IST Everyday
    public void scheduleInactivityReminders() {
        triggerInactivityReminders(null);
    }

    public void triggerInactivityReminders(String email) {
        log.info("Running 9 PM IST Inactivity Check...");
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata")); 
        
        List<User> users;
        if (email != null && !email.trim().isEmpty()) {
            users = userRepository.findAll().stream()
                    .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                    .toList();
        } else {
            users = userRepository.findAll();
        }
        
        for (User user : users) {
             if (user.getUserConfig() == null || (user.getUserConfig().getIsActive() != null && !user.getUserConfig().getIsActive())) {
                 continue;
             }

             // Check if they had questions today
             List<com.revision_platform.questions.entity.UserQuestionHistory> todayHistory = 
                 questionService.getUserHistory(user, 0, 100).getContent()
                 .stream().filter(h -> today.equals(h.getSentDate())).toList();
                 
             if (!todayHistory.isEmpty()) {
                 boolean clickedAny = todayHistory.stream().anyMatch(h -> 
                     h.getIsClicked() != null && h.getIsClicked()
                 );
                 
                 if (!clickedAny) {
                     log.info("Sending reminder email to user: {}", user.getEmail());
                     String body = formatEmailBody(todayHistory, true);
                     notificationSender.sendNotification(user.getEmail(), "⚠️ Don't lose your streak! Your daily DSA challenges are waiting", body);
                 }
             }
        }
    }
}
