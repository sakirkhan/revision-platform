package com.revision_platform.questions.controller;

import com.revision_platform.questions.entity.Question;
import com.revision_platform.questions.service.QuestionService;
import com.revision_platform.users.entity.User;
import com.revision_platform.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/today")
    public ResponseEntity<List<Question>> getTodayQuestions(@RequestParam String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        try {
            List<Question> questions = questionService.getOrGenerateTodayQuestions(user);
            return ResponseEntity.ok(questions);
        } catch (com.revision_platform.questions.exception.ConfigExhaustedException ex) {
            return ResponseEntity.ok(java.util.Collections.emptyList());
        }
    }

    @GetMapping("/preview")
    public ResponseEntity<List<Question>> getPreviewQuestions(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) List<String> topics,
            @RequestParam(defaultValue = "3") int limit) {
        return ResponseEntity.ok(questionService.getPreviewQuestions(difficulty, limit, topics));
    }

    @GetMapping("/history")
    public ResponseEntity<org.springframework.data.domain.Page<com.revision_platform.questions.entity.UserQuestionHistory>> getHistory(
            @RequestParam String email, 
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "5") int size) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return ResponseEntity.ok(questionService.getUserHistory(user, page, size));
    }
}
