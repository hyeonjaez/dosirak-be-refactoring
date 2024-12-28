package com.example.dosirakbe.domain.zone_category.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.example.dosirakbe.domain.zone_category.entity<br>
 * fileName       : ZoneCategory<br>
 * author         : Fiat_lux<br>
 * date           : 12/27/24<br>
 * description    : 지역 카테고리를 나타내는 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/23/24        Fiat_lux                최초 생성<br>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor //기본생성자
@Table(name = "zone_category")
public class ZoneCategory {

    /**
     * 지역 카테고리의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 지역 카테고리를 유일하게 식별하기 위한 ID를 나타냅니다.
     * </p>
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 지역 카테고리의 이름입니다.
     *
     * <p>
     * 이 필드는 지역 카테고리의 이름을 나타내며, 일반적으로 텍스트 형식의 이름(예: 도시명, 지역명 등)을 저장합니다.
     * </p>
     */
    @Column
    private String name;

    /**
     * 부모 지역 카테고리입니다.
     *
     * <p>
     * 이 필드는 계층적 구조를 나타내기 위해 사용되며, 다른 {@link ZoneCategory} 엔티티와의 다대일(N:1) 관계를 나타냅니다.
     * 지연 로딩(LAZY) 전략을 사용하여 성능을 최적화합니다.
     * </p>
     */
    @JoinColumn(name = "parent_zone_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ZoneCategory parentZoneCategory;
}
