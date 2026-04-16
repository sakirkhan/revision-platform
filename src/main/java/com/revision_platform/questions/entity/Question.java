package com.revision_platform.questions.entity;

import com.revision_platform.common.enums.Difficulty;
import com.revision_platform.common.entity.Topic;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title; // Unique identifier or simple title

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50)")
    private Difficulty difficulty;

    @ManyToOne(optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    // e.g., "neetcode_150,striver_sde_sheet" - comma separated string
    @Column(name = "source_lists")
    private String sourceLists;

    @Column(name = "neetcode_url")
    private String neetcodeUrl;
    
    @Column(columnDefinition = "TEXT")
    private String description;
}
