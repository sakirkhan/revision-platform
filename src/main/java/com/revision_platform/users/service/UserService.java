package com.revision_platform.users.service;

import com.revision_platform.users.dto.UserRegistrationRequest;
import com.revision_platform.users.entity.User;
import com.revision_platform.users.entity.UserConfig;
import com.revision_platform.users.repository.UserConfigRepository;
import com.revision_platform.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConfigRepository userConfigRepository;

    @Transactional
    @SuppressWarnings("null")
    public User registerUser(UserRegistrationRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (normalizedEmail == null) {
            throw new RuntimeException("Email is required");
        }

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new RuntimeException("User already exists with email: " + request.getEmail());
        }

        User user = User.builder()
                .email(normalizedEmail)
                .name(request.getName())
                .build();

        User savedUser = Objects.requireNonNull(userRepository.save(user));

        UserConfig config = UserConfig.builder()
                .user(savedUser)
                .notificationTime(request.getNotificationTime())
                .preferredTopics(
                        request.getPreferredTopics() != null ? String.join(",", request.getPreferredTopics()) : null)
                .preferredDifficulties(request.getPreferredDifficulties() != null
                        ? String.join(",", request.getPreferredDifficulties())
                        : null)
                .questionCountPerDay(request.getQuestionCountPerDay())
                .preferredList(request.getPreferredList())
                .revisionDurationDays(request.getRevisionDurationDays())
                .build();

        userConfigRepository.save(Objects.requireNonNull(config));

        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null) {
            return Optional.empty();
        }
        return userRepository.findByEmail(normalizedEmail);
    }

    @Transactional
    public UserConfig updatePreferences(String email, UserRegistrationRequest request) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null) {
            throw new RuntimeException("Email is required");
        }

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        UserConfig config = user.getUserConfig();
        if (config == null) {
            config = UserConfig.builder().user(user).build();
        }

        if (request.getNotificationTime() != null) config.setNotificationTime(request.getNotificationTime());
        if (request.getPreferredTopics() != null) config.setPreferredTopics(String.join(",", request.getPreferredTopics()));
        if (request.getPreferredDifficulties() != null) config.setPreferredDifficulties(String.join(",", request.getPreferredDifficulties()));
        if (request.getQuestionCountPerDay() != null) config.setQuestionCountPerDay(request.getQuestionCountPerDay());
        if (request.getPreferredList() != null) config.setPreferredList(request.getPreferredList());
        if (request.getRevisionDurationDays() != null) config.setRevisionDurationDays(request.getRevisionDurationDays());
        // Any preference update should reactivate previously unsubscribed users.
        config.setIsActive(true);
        config.setUnsubscribeReason(null);

        return userConfigRepository.save(config);
    }

    @Transactional
    public void deactivateUser(String email, String reason) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null) {
            throw new RuntimeException("Email is required");
        }

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
                
        UserConfig config = user.getUserConfig();
        if (config != null) {
            config.setIsActive(false);
            if (reason != null && !reason.trim().isEmpty()) {
                config.setUnsubscribeReason(reason);
            }
            userConfigRepository.save(config);
        }
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        String normalized = email.trim().toLowerCase();
        return normalized.isEmpty() ? null : normalized;
    }
}
