package com.revision_platform.questions.repository;

import com.revision_platform.questions.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Find questions in a specific source list
    // Find questions in a specific source list (comma separated)
    @Query("SELECT q FROM Question q WHERE q.sourceLists LIKE CONCAT('%', :sourceList, '%')")
    List<Question> findBySourceList(String sourceList);
}
