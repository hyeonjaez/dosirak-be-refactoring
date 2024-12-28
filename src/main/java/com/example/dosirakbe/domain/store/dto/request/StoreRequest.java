package com.example.dosirakbe.domain.store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.domain.store.dto.request<br>
 * fileName       : StoreRequest<br>
 * author         : yyujin1231<br>
 * date           : 10/25/24<br>
 * description    : 가게의 위치 정보를 처리하는 요청 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        yyujin1231                최초 생성<br>
 */


@Getter
public class StoreRequest {

    /**
     * 현재 사용자 위치의 X 좌표입니다.
     *
     * <p>
     * 사용자의 현재 위치의 X좌표를 나타냅니다.
     * </p>
     */

    @NotNull
    private double currentMapX;

    /**
     * 현재 사용자 위치의 Y 좌표입니다.
     *
     * <p>
     * 사용자의 현재 위치의 Y좌표를 나타냅니다.
     * </p>
     */

    @NotNull
    private double currentMapY;
}