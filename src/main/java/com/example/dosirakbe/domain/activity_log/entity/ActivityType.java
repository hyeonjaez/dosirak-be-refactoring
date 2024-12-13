package com.example.dosirakbe.domain.activity_log.entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * packageName    : com.example.dosirakbe.domain.activity_log.entity<br>
 * fileName       : ActivityType<br>
 * author         : Fiat_lux<br>
 * date           : 11/03/24<br>
 * description    : 활동 유형을 정의하는 열거형(enum) 클래스입니다. 각 활동 유형은 메시지와 아이콘 이미지 URL을 포함합니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/03/24        Fiat_lux                최초 생성<br>
 */
public enum ActivityType {
    /**
     * 다회용기 포장 인증 활동 유형입니다.
     *
     * <p>
     * 이 활동 유형은 사용자가 다회용기를 사용하여 포장 인증을 받았을 때 기록됩니다.
     * </p>
     */
    MULTI_USE_CONTAINER_PACKAGING("다회용기 포장 인증", "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_dosirak.png"),

    /**
     * 저탄소 이동수단 인증 활동 유형입니다.
     *
     * <p>
     * 이 활동 유형은 사용자가 저탄소 이동수단을 이용하여 인증을 받았을 때 기록됩니다.
     * </p>
     */
    LOW_CARBON_MEANS_OF_TRANSPORTATION("저탄소 이동수단 인증", "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_foot.png");

    /**
     * 활동 메시지입니다.
     *
     * <p>
     * 이 필드는 활동 유형에 대한 설명 메시지를 포함합니다.
     * </p>
     */
    public final String message;

    /**
     * 활동 유형의 아이콘 이미지 URL입니다.
     *
     * <p>
     * 이 필드는 활동 유형을 시각적으로 표현하기 위한 아이콘 이미지의 URL을 포함합니다.
     * </p>
     */
    public final String iconImageUrl;

    /**
     * {@link ActivityType} 열거형의 생성자입니다.
     *
     * <p>
     * 각 활동 유형에 대한 메시지와 아이콘 이미지 URL을 초기화합니다.
     * </p>
     *
     * @param message       활동 유형에 대한 설명 메시지
     * @param iconImageUrl  활동 유형의 아이콘 이미지 URL
     */
    ActivityType(String message, String iconImageUrl) {
        this.message = message;
        this.iconImageUrl = iconImageUrl;
    }

    /**
     * 활동 로그의 메시지를 생성합니다.
     *
     * <p>
     * 이 메서드는 활동 유형과 이동 거리를 기반으로 활동 메시지를 생성합니다.
     * {@link ActivityType#LOW_CARBON_MEANS_OF_TRANSPORTATION}인 경우, 이동 거리를 포함한 메시지를 반환합니다.
     * 그 외의 경우에는 기본 메시지를 반환합니다.
     * </p>
     *
     * @param distance 활동 중 이동한 거리 (선택 사항)
     * @return 생성된 활동 메시지 문자열
     */
    public String generateMessage(BigDecimal distance) {
        if (this == LOW_CARBON_MEANS_OF_TRANSPORTATION && Objects.nonNull(distance)) {
            return String.format("%s - %.2f km", message, distance);
        }
        return message;
    }

}
