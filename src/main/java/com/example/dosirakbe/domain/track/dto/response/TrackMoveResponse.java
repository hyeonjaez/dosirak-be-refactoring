package com.example.dosirakbe.domain.track.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * packageName    : com.example.dosirakbe.domain.track.dto.response<br>
 * fileName       : TrackMoveResponse<br>
 * author         : Fiat_lux<br>
 * date           : 11/14/24<br>
 * description    : 사용자의 이동 거리 기록을 응답하기 위한 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/14/24        Fiat_lux                최초 생성<br>
 * 12/23/24        Fiat_lux               필드에 json 어노테이션 추가<br>
 */
@Getter
@AllArgsConstructor
public class TrackMoveResponse {
    /**
     * 사용자가 이동한 트랙 거리입니다.
     *
     * <p>
     * 이 필드는 사용자가 실제로 이동한 트랙 거리를 나타내며, 소수점 이하 두 자리까지의 정밀도를 가집니다.
     * </p>
     */
    @JsonProperty("move_track_distance")
    private BigDecimal moveTrackDistance;
}
