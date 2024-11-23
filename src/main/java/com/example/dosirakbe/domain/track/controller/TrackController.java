package com.example.dosirakbe.domain.track.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.track.dto.request.TrackMoveRequest;
import com.example.dosirakbe.domain.track.dto.response.TrackMoveResponse;
import com.example.dosirakbe.domain.track.dto.response.TrackSearchResponse;
import com.example.dosirakbe.domain.track.service.TrackService;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.StatusEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/track")
public class TrackController {
    public final TrackService trackService;
    public static final BigDecimal GAP_DISTANCE = BigDecimal.ONE; //1km

    @PostMapping
    public ResponseEntity<ApiResult<TrackMoveResponse>> recordMovingDistance(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                             @RequestBody @Valid TrackMoveRequest trackMoveRequest) {
        Long userId = customOAuth2User.getUserDTO().getUserId();

        if (!checkGapDistance(trackMoveRequest)) {
            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
        }

        TrackMoveResponse trackMoveResponse = trackService.recordTrackDistance(userId, trackMoveRequest);
        ApiResult<TrackMoveResponse> result = ApiResult.<TrackMoveResponse>builder()
                .status(StatusEnum.SUCCESS)
                .message("track move distance successful")
                .data(trackMoveResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<TrackSearchResponse>>> d(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                  @RequestParam(required = false) String search) {
        Long userId = customOAuth2User.getUserDTO().getUserId();

        List<TrackSearchResponse> trackList = trackService.getTrackList(userId, search);

        ApiResult<List<TrackSearchResponse>> result = ApiResult.<List<TrackSearchResponse>>builder()
                .status(StatusEnum.SUCCESS)
                .message("track log search successful")
                .data(trackList)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


    private boolean checkGapDistance(TrackMoveRequest trackMoveRequest) {
        BigDecimal shortestDistance = trackMoveRequest.getShortestDistance();
        BigDecimal moveDistance = trackMoveRequest.getMoveDistance();

        if (shortestDistance.compareTo(moveDistance) > 0) {
            return shortestDistance.subtract(moveDistance).compareTo(GAP_DISTANCE) <= 0;
        } else {
            return moveDistance.subtract(shortestDistance).compareTo(GAP_DISTANCE) <= 0;
        }
    }
}
