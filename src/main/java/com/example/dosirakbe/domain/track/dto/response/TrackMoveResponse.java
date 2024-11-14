package com.example.dosirakbe.domain.track.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TrackMoveResponse {
    private BigDecimal moveTrackDistance;
}
