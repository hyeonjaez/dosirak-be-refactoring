package com.example.dosirakbe.domain.track.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * packageName    : com.example.dosirakbe.domain.track.dto.request<br>
 * fileName       : TrackMoveRequest<br>
 * author         : Fiat_lux<br>
 * date           : 11/14/24<br>
 * description    : 사용자의 이동 기록을 요청하기 위한 DTO(Data Transfer Object) 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/14/24        Fiat_lux                최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class TrackMoveRequest {

    /**
     * 사용자의 최단 이동 거리입니다.
     *
     * <p>
     * 이 필드는 사용자가 이동한 최단 거리를 나타내며, 반드시 {@code null}이 아니어야 합니다.
     * 소수점 이하 두 자리까지의 정밀도를 가지며, 정수 부분은 최대 10자리까지 허용됩니다.
     * </p>
     */
    @NotNull
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal shortestDistance;

    /**
     * 사용자의 실제 이동 거리입니다.
     *
     * <p>
     * 이 필드는 사용자가 실제로 이동한 거리를 나타내며, 반드시 {@code null}이 아니어야 합니다.
     * 소수점 이하 두 자리까지의 정밀도를 가지며, 정수 부분은 최대 10자리까지 허용됩니다.
     * </p>
     */
    @NotNull
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal moveDistance;

    /**
     * 사용자가 이동한 판매점의 이름입니다.
     *
     * <p>
     * 이 필드는 사용자가 이동한 판매점의 이름을 나타내며, 반드시 {@code null}이 아니어야 합니다.
     * </p>
     */
    @NotNull
    private String saleStoreName;
}
