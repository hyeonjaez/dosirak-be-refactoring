package com.example.dosirakbe.domain.store.dto.response;

import com.example.dosirakbe.domain.menu.dto.response.MenuResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.store.dto.response<br>
 * fileName       : StoreDetailResponse<br>
 * author         : yyujin1231<br>
 * date           : 10/25/24<br>
 * description    : 가게 상세 정보를 클라이언트에게 전달하기 위한 응답 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        yyujin1231                최초 생성<br>
 */


@AllArgsConstructor
@Getter
@Builder
public class StoreDetailResponse {

    /**
     * 가게의 고유 식별자입니다.
     */

    private Long storeId;

    /**
     * 가게의 이름입니다.
     */

    private String storeName;

    /**
     * 가게의 카테고리입니다.
     */

    private String storeCategory;

    /**
     * 가게의 이미지입니다.
     *
     * <p>
     * 이 필드는 가게의 이미지 URL을 나타냅니다.
     * </p>
     */

    private String storeImg;

    /**
     * 가게의 경도입니다.
     *
     * <p>
     * 이 필드는 가게의 X좌표(경도)를 나타냅니다.
     * </p>
     */

    private double mapX;

    /**
     * 가게의 위도입니다.
     *
     * <p>
     * 이 필드는 가게의 Y좌표(위도)를 나타냅니다.
     * </p>
     */

    private double mapY;

    /**
     * 가게의 운영시간입니다.
     *
     */

    private String operationTime;

    /**
     * 가게의 전화번호입니다.
     *
     */
    private String telNumber;

    /**
     * 가게의 다회용기 혜택여부 입니다.
     *
     */

    private String ifValid;

    /**
     * 가게의 리워드 혜택여부 입니다.
     *
     */

    private String ifReward;

    /**
     * 가게의 메뉴 목록입니다
     *
     * <p>
     * 이 필드는 가게에 속한 메뉴의 정보들을 나타냅니다.
     * </p>
     */

    private List<MenuResponse> menus;
}
