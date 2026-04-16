package com.revision_platform.users.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class UserRegistrationRequest {
    private String email;
    private String name;

    // Initial Config
    private LocalTime notificationTime;
    private List<String> preferredTopics;
    private List<String> preferredDifficulties;
    private Integer questionCountPerDay;
    private String preferredList;
    private Integer revisionDurationDays;
}
