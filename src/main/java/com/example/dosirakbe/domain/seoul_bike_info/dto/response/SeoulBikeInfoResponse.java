package com.example.dosirakbe.domain.seoul_bike_info.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SeoulBikeInfoResponse {

    private String id;
    private String addressLevelOne;
    private String addressLevelTwo;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
