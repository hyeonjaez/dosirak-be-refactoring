package com.example.dosirakbe.domain.track.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TrackSearchGetResponse {
    private Long id;
    private String search;
    private LocalDateTime createdAt;
}
