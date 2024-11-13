package com.example.dosirakbe.domain.track.controller;

import com.example.dosirakbe.domain.track.dto.request.TrackMoveRequest;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/track")
public class TrackController {
    public static final BigDecimal GAP_DISTANCE = BigDecimal.ONE; //1km

    @PostMapping
    public void recordMovingDistance(@RequestBody @Valid TrackMoveRequest trackMoveRequest) {
        if (!checkGapDistance(trackMoveRequest)) {
            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
        }
    }


    public boolean checkGapDistance(TrackMoveRequest trackMoveRequest) {
        BigDecimal shortestDistance = trackMoveRequest.getShortestDistance();
        BigDecimal moveDistance = trackMoveRequest.getMoveDistance();

        if (shortestDistance.compareTo(moveDistance) > 0) {
            return shortestDistance.subtract(moveDistance).compareTo(GAP_DISTANCE) <= 0;
        } else {
            return moveDistance.subtract(shortestDistance).compareTo(GAP_DISTANCE) <= 0;
        }
    }
}
