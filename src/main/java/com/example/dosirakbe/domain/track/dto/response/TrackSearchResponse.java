package com.example.dosirakbe.domain.track.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName    : com.example.dosirakbe.domain.track.dto.response<br>
 * fileName       : TrackSearchResponse<br>
 * author         : Fiat_lux<br>
 * date           : 11/24/24<br>
 * description    : 트랙 검색 결과를 응답하기 위한 DTO(Data Transfer Object) 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/24/24        Fiat_lux                최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class TrackSearchResponse {

    /**
     * 트랙의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 트랙을 유일하게 식별하기 위한 ID를 나타냅니다.
     * </p>
     */
    private Long id;

    /**
     * 이동한 판매점의 이름입니다.
     *
     * <p>
     * 이 필드는 사용자가 이동한 판매점의 이름을 나타내며, 사용자의 이동 기록을 식별하는 데 사용됩니다.
     * </p>
     */
    private String storeName;

    /**
     * 이동한 판매점의 주소입니다.
     *
     * <p>
     * 이 필드는 사용자가 이동한 판매점의 상세 주소를 나타내며, 위치 기반 서비스를 지원하는 데 사용됩니다.
     * </p>
     */
    private String storeAddress;

    /**
     * 이동한 거리입니다.
     *
     * <p>
     * 이 필드는 사용자가 이동한 거리를 킬로미터(KM) 단위로 나타내며, 소수점 이하 두 자리까지의 정밀도를 가집니다.
     * </p>
     */
    private BigDecimal distance;

    /**
     * 트랙이 기록된 시각입니다.
     *
     * <p>
     * 이 필드는 트랙이 생성된 날짜와 시간을 {@link LocalDateTime} 형식으로 나타냅니다.
     * </p>
     */
    private LocalDateTime createdAt;
}
