package com.example.dosirakbe.domain.activity_log.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ActivityLogResponse {
    private LocalDateTime createdAt;
    private String activityMessage;
    private String createAtTime;
}
