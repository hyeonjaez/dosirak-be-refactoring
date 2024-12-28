package com.example.dosirakbe.domain.salestore.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.example.dosirakbe.domain.salestore.entity<br>
 * fileName       : SaleStore<br>
 * author         : yyujin1231<br>
 * date           : 11/08/24<br>
 * description    : 마감음식을 판매하는 가게 정보를 나타내는 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/08/24        yyujin1231                최초 생성<br>
 */

@Table(name = "salestores")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class SaleStore {

    /**
     * 마감 음식을 판매하는 가게의 고유 식별자입니다.
     *
     */

    @Id
    @Column(name = "sale_store_id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleStoreId;

    /**
     * 마감 음식을 판매하는 가게의 이름입니다.
     *
     */

    @Column(name = "sale_store_name", nullable = false)
    private String saleStoreName;

    /**
     * 마감 음식을 판매하는 가게의 이미지입니다.
     *
     * <p>
     * 이 필드는 마감 음식을 판매하는 가게의 이미지 URL을 나타냅니다.
     * </p>
     */

    @Column(name = "sale_store_img", nullable = false)
    private String saleStoreImg;

    /**
     * 마감 음식을 판매하는 가게의 주소입니다.
     *
     */

    @Column(name = "sale_store_address", nullable = false)
    private String saleStoreAddress;

    /**
     * 마감 음식을 판매하는 가게의 경도입니다.
     *
     * <p>
     * 이 필드는 마감 음식을 판매하는 가게의 X좌표(경도)를 나타냅니다.
     * </p>
     */

    @Column(name = "sale_map_x", nullable = false)
    private String saleMapX;

    /**
     * 마감 음식을 판매하는 가게의 위도입니다.
     *
     * <p>
     * 이 필드는 마감 음식을 판매하는 가게의 Y좌표(위도)를 나타냅니다.
     * </p>
     */

    @Column(name = "sale_map_y", nullable = false)
    private String saleMapY;

    /**
     * 마감 음식을 판매하는 가게의 운영시간입니다.
     *
     */

    @Column(name = "sale_operation_time", nullable = false)
    private String saleOperationTime;

    /**
     * 마감 음식을 판매하는 가게의 최대할인율입니다.
     *
     */

    @Column(name = "sale_discount", nullable = false)
    private String saleDiscount;

}