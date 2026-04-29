package com.revision_platform.users.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "user_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;

    private LocalTime notificationTime; // e.g., 09:00

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "unsubscribe_reason")
    private String unsubscribeReason;

    // Comma-separated or JSON string of topic names
    @Column(name = "preferred_topics")
    private String preferredTopics;

    // Comma-separated or JSON string of difficulty names
    @Column(name = "preferred_difficulties")
    private String preferredDifficulties;

    private Integer questionCountPerDay;

    @Column(name = "question_count_per_weekend")
    private Integer questionCountPerWeekend;

    // e.g., "neetcode_150", "striver_sde_sheet"
    private String preferredList;

    private Integer revisionDurationDays; // e.g., 30 days to finish the list
}
