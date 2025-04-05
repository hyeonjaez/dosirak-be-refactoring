package com.example.dosirakbe.domain.activity_log.entity;

import com.example.dosirakbe.global.util.ObjectsUtil;
import lombok.Getter;

import java.math.BigDecimal;

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
@Getter
public enum ActivityType {
    /**
     * 다회용기 포장 인증 활동 유형입니다.
     *
     * <p>
     * 이 활동 유형은 사용자가 다회용기를 사용하여 포장 인증을 받았을 때 기록됩니다.
     * </p>
     */
    MULTI_USE_CONTAINER_PACKAGING("다회용기 포장 인증", "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_dosirak.png") {
        @Override
        public boolean requiresDistance() {
            return false;
        }

        @Override
        public String generateMessage(BigDecimal distance) {
            return getMessage();
        }
    },

    /**
     * 저탄소 이동수단 인증 활동 유형입니다.
     *
     * <p>
     * 이 활동 유형은 사용자가 저탄소 이동수단을 이용하여 인증을 받았을 때 기록됩니다.
     * </p>
     */
    LOW_CARBON_MEANS_OF_TRANSPORTATION("저탄소 이동수단 인증", "https://dosirakbucket.s3.ap-northeast-2.amazonaws.com/green_commit/commit_foot.png") {
        @Override
        public boolean requiresDistance() {
            return true;
        }

        @Override
        public String generateMessage(BigDecimal distance) {
            ObjectsUtil.checkAllNotNull(distance);
            return String.format("%s - %.2f km", getMessage(), distance);
        }
    };

    /**
     * 활동 메시지입니다.
     *
     * <p>
     * 이 필드는 활동 유형에 대한 설명 메시지를 포함합니다.
     * </p>
     */
    private final String message;

    /**
     * 활동 유형의 아이콘 이미지 URL 입니다.
     *
     * <p>
     * 이 필드는 활동 유형을 시각적으로 표현하기 위한 아이콘 이미지의 URL 을 포함합니다.
     * </p>
     */
    private final String iconImageUrl;

    /**
     * {@link ActivityType} 열거형의 생성자입니다.
     *
     * <p>
     * 각 활동 유형에 대한 메시지와 아이콘 이미지 URL을 초기화합니다.
     * </p>
     *
     * @param message      활동 유형에 대한 설명 메시지
     * @param iconImageUrl 활동 유형의 아이콘 이미지 URL
     */
    ActivityType(String message, String iconImageUrl) {
        this.message = message;
        this.iconImageUrl = iconImageUrl;
    }

    /**
     * 활동 유형에 따라 거리 정보가 필요한지 여부를 반환합니다.
     *
     * <p>
     * 예를 들어, 저탄소 이동수단 인증은 이동 거리 정보를 필요로 하지만,
     * 다회용기 포장 인증은 필요하지 않습니다.
     * </p>
     *
     * @return true 일 경우 거리 정보가 필요함을 의미합니다.
     */
    public abstract boolean requiresDistance();

    /**
     * 활동 유형과 주어진 거리 정보를 바탕으로 활동 메시지를 생성합니다.
     *
     * <p>
     * 거리 정보가 필요한 활동 유형일 경우, 메시지에 거리(km)를 포함하여 반환합니다.
     * 거리 정보가 필요하지 않은 유형은 기본 메시지만 반환합니다.
     * </p>
     *
     * @param distance 활동 중 이동한 거리 (null일 수 있음)
     * @return 생성된 활동 메시지 문자열
     */
    public abstract String generateMessage(BigDecimal distance);

    public String generateMessage() {
        return generateMessage(null);
    }

}
