package com.revision_platform.questions.controller;

import com.revision_platform.questions.entity.UserQuestionHistory;
import com.revision_platform.questions.repository.UserQuestionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/track")
@RequiredArgsConstructor
public class TrackingController {

    private final UserQuestionHistoryRepository historyRepository;

    @GetMapping("/click")
    public ResponseEntity<Void> trackClick(@RequestParam Long historyId, @RequestParam(required = false, defaultValue = "leetcode") String dest) {
        Optional<UserQuestionHistory> historyOpt = historyRepository.findById(historyId);
        if (historyOpt.isPresent()) {
            UserQuestionHistory history = historyOpt.get();
            if (!Boolean.TRUE.equals(history.getIsClicked())) {
                history.setIsClicked(true);
                historyRepository.save(history);
            }
            
            String redirectUrl = history.getQuestion().getUrl();
            if ("neetcode".equalsIgnoreCase(dest) && history.getQuestion().getNeetcodeUrl() != null) {
                redirectUrl = history.getQuestion().getNeetcodeUrl();
            }

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();
        }
        // Fallback if not found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
