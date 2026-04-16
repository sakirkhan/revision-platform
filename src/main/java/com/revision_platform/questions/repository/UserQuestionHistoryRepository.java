package com.revision_platform.questions.repository;

import com.revision_platform.questions.entity.UserQuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserQuestionHistoryRepository extends JpaRepository<UserQuestionHistory, Long> {

    // Find all questions sent to a user after a certain date
    // Useful for filtering out recently sent questions
    List<UserQuestionHistory> findByUserIdAndSentDateAfter(Long userId, LocalDate date);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = { "question", "question.topic" })
    List<UserQuestionHistory> findByUserIdAndSentDate(Long userId, LocalDate date);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = { "question", "question.topic" })
    org.springframework.data.domain.Page<UserQuestionHistory> findAllByUserIdOrderBySentDateDesc(Long userId,
            org.springframework.data.domain.Pageable pageable);
}
