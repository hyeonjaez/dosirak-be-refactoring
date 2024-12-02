package com.example.dosirakbe.domain.track.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TrackSearchResponse {
    private Long id;
    private String storeName;
    private String storeAddress;
    private BigDecimal distance;
    private LocalDateTime createdAt;
}
