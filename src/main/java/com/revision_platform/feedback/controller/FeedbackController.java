package com.revision_platform.feedback.controller;

import com.revision_platform.feedback.entity.Feedback;
import com.revision_platform.feedback.repository.FeedbackRepository;
import com.revision_platform.users.entity.User;
import com.revision_platform.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping
    public ResponseEntity<String> submitFeedback(
            @RequestParam("email") String email,
            @RequestParam("type") String type,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        User user = userService.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String filename = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, filename);
                Files.write(filePath, imageFile.getBytes());
                
                // Return path that WebMvcConfigurer can map
                imagePath = "/uploads/" + filename;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
            }
        }

        Feedback feedback = Feedback.builder()
                .user(user)
                .type(type)
                .description(description)
                .imagePath(imagePath)
                .build();

        feedbackRepository.save(feedback);

        return ResponseEntity.ok("Feedback submitted successfully");
    }
}
