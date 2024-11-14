package com.example.dosirakbe.domain.seoul_bike_info.controller;

import com.example.dosirakbe.domain.seoul_bike_info.dto.request.SeoulBikeInfoRequest;
import com.example.dosirakbe.domain.seoul_bike_info.dto.response.SeoulBikeInfoResponse;
import com.example.dosirakbe.domain.seoul_bike_info.service.SeoulBikeInfoService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seoul-bike-info")
public class SeoulBikeInfoController {
    private final SeoulBikeInfoService seoulBikeInfoService;

    @GetMapping
    public ResponseEntity<ApiResult<List<SeoulBikeInfoResponse>>> getSeoulBikeListByAroundMe(@Valid @RequestBody SeoulBikeInfoRequest seoulBikeInfoRequest) {
        List<SeoulBikeInfoResponse> seoulBikeListAroundMe = seoulBikeInfoService.getSeoulBikeListAroundMe(seoulBikeInfoRequest);

        ApiResult<List<SeoulBikeInfoResponse>> apiResult = ApiResult.<List<SeoulBikeInfoResponse>>builder()
                .status(StatusEnum.SUCCESS)
                .message("seoul bike list by around me retrieved successfully")
                .data(seoulBikeListAroundMe)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResult);

    }
}
