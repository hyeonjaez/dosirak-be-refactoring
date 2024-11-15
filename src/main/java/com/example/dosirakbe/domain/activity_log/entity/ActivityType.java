package com.example.dosirakbe.domain.activity_log.entity;

import java.math.BigDecimal;
import java.util.Objects;

public enum ActivityType {
    MULTI_USE_CONTAINER_PACKAGING("다회용기 포장 인증", "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_dosirak.png"),
    LOW_CARBON_MEANS_OF_TRANSPORTATION("저탄소 이동수단 인증", "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_foot.png");

    public final String message;
    public final String iconImageUrl;

    ActivityType(String message, String iconImageUrl) {
        this.message = message;
        this.iconImageUrl = iconImageUrl;
    }

    public String generateMessage(BigDecimal distance) {
        if (this == LOW_CARBON_MEANS_OF_TRANSPORTATION && Objects.nonNull(distance)) {
            return String.format("%s - %.2f km", message, distance);
        }
        return message;
    }

}
