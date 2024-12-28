package com.example.dosirakbe.domain.zone_category.repository;

import com.example.dosirakbe.domain.zone_category.entity.ZoneCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.zone_category.repository<br>
 * fileName       : ZoneCategoryRepository<br>
 * author         : Fiat_lux<br>
 * date           : 12/27/24<br>
 * description    : 지역 카테고리에 대한 데이터 접근을 담당하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/23/24        Fiat_lux                최초 생성<br>
 */
public interface ZoneCategoryRepository extends JpaRepository<ZoneCategory, Long> {

    /**
     * 지역 카테고리 이름으로 특정 지역 카테고리를 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code name}을 기반으로 해당 이름의 {@link ZoneCategory} 엔티티를
     * 조회하며, 존재할 경우 {@link Optional}로 감싸서 반환합니다.
     * 존재하지 않을 경우 빈 {@link Optional}을 반환합니다.
     * </p>
     *
     * @param name 조회할 지역 카테고리의 이름
     * @return 주어진 이름에 해당하는 {@link ZoneCategory} 객체를 포함하는 {@link Optional}
     */
    Optional<ZoneCategory> findByName(String name);
}
