package com.example.dosirakbe.domain.user_activity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserActivityResponse {

    private LocalDate createdAt;
    private Integer commitCount;

}
