package com.example.dosirakbe.domain.activity_type.entity;

public enum Type {
    MULTI_USE_CONTAINER_PACKAGING("다회용기 포장 인증"),
    LOW_CARBON_MEANS_OF_TRANSPORTATION("저탄소 이동수단 인증");

    public final String message;

    Type(String message) {
        this.message = message;
    }

}
