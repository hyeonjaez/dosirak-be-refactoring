package com.example.dosirakbe.domain.store.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : com.example.dosirakbe.domain.store.dto.response<br>
 * fileName       : StoreResponse<br>
 * author         : yyujin1231<br>
 * date           : 10/25/24<br>
 * description    : 가게 정보를 클라이언트에 전달하기 위한 응답 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        yyujin1231                최초 생성<br>
 */


@RequiredArgsConstructor
@Getter
public class StoreResponse {

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
     * 가게의 운영여부입니다.
     *
     * <p>
     * 이 필드는 가게의 운영시간을 기반으로 계산된 운영여부 True/False를 나타냅니다.
     * </p>
     */

    private boolean operating;

    /**
     * 가게 정보를 초기화하는 생성자입니다.
     * @param storeId 가게의 고유 식별자
     * @param storeName 가게의 이름
     * @param storeCategory 가게의 카테고리
     * @param storeImg 가게의 이미지 URL
     * @param ifValid 가게의 다회용기 혜택여부
     * @param ifReward 가게의 리워드 혜택여부
     * @param mapX 가게의 X좌표
     * @param mapY 가게의 Y좌표
     * @param operating 가게의 운영 여부 True/False
     */


    public StoreResponse(Long storeId, String storeName, String storeCategory, String storeImg, String ifValid, String ifReward, double mapX, double mapY, boolean operating) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeCategory = storeCategory;
        this.storeImg = storeImg;
        this.ifValid = ifValid ;
        this.ifReward = ifReward;
        this.mapX = mapX;
        this.mapY = mapY;
        this.operating = operating;
    }
}
