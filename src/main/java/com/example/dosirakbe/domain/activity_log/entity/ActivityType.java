package com.example.dosirakbe.domain.activity_log.entity;

public enum ActivityType {
    MULTI_USE_CONTAINER_PACKAGING("다회용기 포장 인증"),
    LOW_CARBON_MEANS_OF_TRANSPORTATION("저탄소 이동수단 인증");

    public final String message;

    ActivityType(String message) {
        this.message = message;
    }

}
