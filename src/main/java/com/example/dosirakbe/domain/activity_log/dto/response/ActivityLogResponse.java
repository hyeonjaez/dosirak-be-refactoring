package com.example.dosirakbe.domain.activity_log.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.example.dosirakbe.domain.activity_log.dto.response<br>
 * fileName       : ActivityLogResponse<br>
 * author         : Fiat_lux<br>
 * date           : 11/03/24<br>
 * description    : 활동 로그 정보를 클라이언트에게 전달하기 위한 응답 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/03/24        Fiat_lux                최초 생성<br>
 * 12/23/24        Fiat_lux               필드에 json 어노테이션 추가<br>
 */
@Getter
@AllArgsConstructor
public class ActivityLogResponse {
    /**
     * 활동 로그가 생성된 날짜입니다.
     *
     * <p>
     * 이 필드는 활동 로그가 생성된 날짜와 시간을 {@link LocalDateTime} 형식으로 나타냅니다.
     * </p>
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * 활동 메시지입니다.
     *
     * <p>
     * 이 필드는 사용자의 활동을 설명하는 메시지를 포함합니다.
     * </p>
     */
    @JsonProperty("activity_message")
    private String activityMessage;

    /**
     * 활동 로그 생성 시각의 포맷된 문자열입니다.
     *
     * <p>
     * 이 필드는 {@link #createdAt} 필드를 "HH시 mm분" 형식의 문자열로 포맷한 값을 포함합니다.
     * </p>
     */
    @JsonProperty("create_at_time")
    private String createAtTime;

    /**
     * 활동 유형의 아이콘 이미지 URL 입니다.
     *
     * <p>
     * 이 필드는 활동 유형을 시각적으로 표현하기 위한 아이콘 이미지의 URL 을 포함합니다.
     * </p>
     */
    @JsonProperty("icon_image_url")
    private String iconImageUrl;
}
