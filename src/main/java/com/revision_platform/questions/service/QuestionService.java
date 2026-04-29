package com.revision_platform.questions.service;

import com.revision_platform.questions.entity.Question;
import com.revision_platform.questions.entity.UserQuestionHistory;
import com.revision_platform.questions.repository.QuestionRepository;
import com.revision_platform.questions.repository.UserQuestionHistoryRepository;
import com.revision_platform.users.entity.User;
import com.revision_platform.users.entity.UserConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.revision_platform.common.constants.DefaultConfigConstants;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserQuestionHistoryRepository historyRepository;

    public org.springframework.data.domain.Page<UserQuestionHistory> getUserHistory(User user, int page, int size) {
        return historyRepository.findAllByUserIdOrderBySentDateDesc(user.getId(), org.springframework.data.domain.PageRequest.of(page, size));
    }

    public List<Question> getOrGenerateTodayQuestions(User user) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        List<UserQuestionHistory> todayHistory = historyRepository.findByUserIdAndSentDate(user.getId(), today);
        
        if (!todayHistory.isEmpty()) {
            List<Question> cached = todayHistory.stream()
                    .map(UserQuestionHistory::getQuestion)
                    .collect(Collectors.toList());
            
            int limit = getQuestionLimitForToday(user.getUserConfig());
            
            if (cached.size() > limit) {
                return cached.subList(0, limit);
            }
            return cached;
        }
        
        List<Question> newQuestions = getQuestionsForUser(user);
        
        return newQuestions;
    }

    public List<Question> getQuestionsForUser(User user) {
        UserConfig config = user.getUserConfig();

        // Defaults
        String preferredList = (config != null && config.getPreferredList() != null) ? config.getPreferredList() : DefaultConfigConstants.DEFAULT_QUESTION_SOURCE_LIST;
        int limit = getQuestionLimitForToday(config);
        int cutoffDays = (config != null && config.getRevisionDurationDays() != null) ? config.getRevisionDurationDays() : DefaultConfigConstants.DEFAULT_REVISION_DURATION_DAYS;

        // Baseline un-sent candidates pool
        List<Question> candidates = questionRepository.findBySourceList(preferredList);
        LocalDate cutoffDate = LocalDate.now(ZoneId.of("Asia/Kolkata")).minusDays(cutoffDays);
        List<UserQuestionHistory> history = historyRepository.findByUserIdAndSentDateAfter(user.getId(), cutoffDate);
        Set<Long> sentQuestionIds = history.stream().map(h -> h.getQuestion().getId()).collect(Collectors.toSet());
        candidates = candidates.stream().filter(q -> !sentQuestionIds.contains(q.getId())).collect(Collectors.toList());

        // Parse configurations
        Set<String> preferredTopics = (config != null && config.getPreferredTopics() != null && !config.getPreferredTopics().isEmpty()) ? Set.of(config.getPreferredTopics().split(",")) : Collections.emptySet();
        Set<String> preferredDifficulties = (config != null && config.getPreferredDifficulties() != null && !config.getPreferredDifficulties().isEmpty()) ? Set.of(config.getPreferredDifficulties().toUpperCase().split(",")) : Collections.emptySet();

        return buildLayeredSelection(candidates, preferredTopics, preferredDifficulties, limit);
    }

    public List<Question> getPreviewQuestions(String difficulty, int limit, List<String> topics) {
        String preferredList = DefaultConfigConstants.DEFAULT_QUESTION_SOURCE_LIST;
        List<Question> candidates = questionRepository.findBySourceList(preferredList);

        Set<String> preferredTopics = (topics != null && !topics.isEmpty()) ? Set.copyOf(topics) : Collections.emptySet();
        Set<String> preferredDifficulties = (difficulty != null && !difficulty.equalsIgnoreCase("Random") && !difficulty.isEmpty()) ? Set.of(difficulty.toUpperCase()) : Collections.emptySet();

        return buildLayeredSelection(candidates, preferredTopics, preferredDifficulties, limit);
    }

    private List<Question> buildLayeredSelection(List<Question> candidates, Set<String> preferredTopics, Set<String> preferredDifficulties, int limit) {
        List<Question> finalSelection = new java.util.ArrayList<>();
        
        // 1. Try to strictly match both Topics AND Difficulty
        List<Question> tier1 = candidates.stream()
                .filter(q -> (preferredTopics.isEmpty() || preferredTopics.contains(q.getTopic().getName())))
                .filter(q -> (preferredDifficulties.isEmpty() || preferredDifficulties.contains(q.getDifficulty().name().toUpperCase())))
                .collect(Collectors.toList());
        Collections.shuffle(tier1);
        for(Question q : tier1) {
            if (finalSelection.size() < limit) finalSelection.add(q);
        }

        // 2. If short on limit, try to match JUST Topics (ignore difficulty logic)
        if (finalSelection.size() < limit && !preferredTopics.isEmpty()) {
            List<Question> tier2 = candidates.stream()
                    .filter(q -> preferredTopics.contains(q.getTopic().getName()))
                    .filter(q -> !finalSelection.contains(q))
                    .collect(Collectors.toList());
            Collections.shuffle(tier2);
            for(Question q : tier2) {
                if (finalSelection.size() < limit) finalSelection.add(q);
            }
        }

        // 3. If short on limit, try to match JUST Difficulty (ignore topic logic)
        if (finalSelection.size() < limit && !preferredDifficulties.isEmpty()) {
            List<Question> tier3 = candidates.stream()
                    .filter(q -> preferredDifficulties.contains(q.getDifficulty().name().toUpperCase()))
                    .filter(q -> !finalSelection.contains(q))
                    .collect(Collectors.toList());
            Collections.shuffle(tier3);
            for(Question q : tier3) {
                if (finalSelection.size() < limit) finalSelection.add(q);
            }
        }

        // 4. Ultimate fallback: Completely random to ensure user gets daily quota
        if (finalSelection.size() < limit) {
            boolean hasSpecificConfigs = !preferredTopics.isEmpty() || !preferredDifficulties.isEmpty();
            if (hasSpecificConfigs && finalSelection.isEmpty()) {
                throw new com.revision_platform.questions.exception.ConfigExhaustedException("Your configured topics/difficulty are exhausted.");
            }

            List<Question> tier4 = candidates.stream()
                    .filter(q -> !finalSelection.contains(q))
                    .collect(Collectors.toList());
            Collections.shuffle(tier4);
            for(Question q : tier4) {
                if (finalSelection.size() < limit) finalSelection.add(q);
            }
        }

        return finalSelection;
    }

    public List<UserQuestionHistory> recordQuestionsSent(User user, List<Question> questions) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        List<UserQuestionHistory> histories = questions.stream()
                .map(q -> UserQuestionHistory.builder()
                        .user(user)
                        .question(q)
                        .sentDate(today)
                        .isClicked(false)
                        .build())
                .collect(Collectors.toList());
        return historyRepository.saveAll(histories);
    }

    private int getQuestionLimitForToday(UserConfig config) {
        if (config == null) {
            return DefaultConfigConstants.DEFAULT_QUESTION_LIMIT;
        }
        
        java.time.DayOfWeek dayOfWeek = LocalDate.now(ZoneId.of("Asia/Kolkata")).getDayOfWeek();
        boolean isWeekend = dayOfWeek == java.time.DayOfWeek.SATURDAY || dayOfWeek == java.time.DayOfWeek.SUNDAY;
        
        if (isWeekend && config.getQuestionCountPerWeekend() != null) {
            return config.getQuestionCountPerWeekend();
        }
        
        return config.getQuestionCountPerDay() != null ? config.getQuestionCountPerDay() : DefaultConfigConstants.DEFAULT_QUESTION_LIMIT;
    }

    public java.util.Map<String, List<Question>> getUserHistoryByTopic(User user) {
        List<UserQuestionHistory> history = historyRepository.findByUserIdAndSentDateAfter(user.getId(), LocalDate.of(2000, 1, 1));
        return history.stream()
                .map(UserQuestionHistory::getQuestion)
                .collect(Collectors.groupingBy(q -> q.getTopic() != null ? q.getTopic().getName() : "General"));
    }

    @org.springframework.transaction.annotation.Transactional
    public void resetTopicHistory(User user, String topicName) {
        List<UserQuestionHistory> history = historyRepository.findByUserIdAndSentDateAfter(user.getId(), LocalDate.of(2000, 1, 1));
        List<UserQuestionHistory> toDelete = history.stream()
                .filter(h -> {
                    String tName = h.getQuestion().getTopic() != null ? h.getQuestion().getTopic().getName() : "General";
                    return tName.equalsIgnoreCase(topicName);
                })
                .collect(Collectors.toList());
        historyRepository.deleteAll(toDelete);
    }
}
