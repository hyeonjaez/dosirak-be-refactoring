package com.example.dosirakbe.domain.salestore.repository;

import com.example.dosirakbe.domain.salestore.entity.SaleStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.salestore.repository<br>
 * fileName       : SaleStoreRepository<br>
 * author         : yyujin1231<br>
 * date           : 11/08/24<br>
 * description    : 마감음식을 판매하는 가게 정보를 관리하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/08/24        yyujin1231                최초 생성<br>
 */

@Repository
public interface SaleStoreRepository extends JpaRepository<SaleStore, Long> {

    /**
     * 사용자가 입력한 주소를 포함하는 가게 목록을 조회합니다.
     * <p>
     * 입력된 주소 문자열을 포함하며, 할인율 순으로 정렬된 마감음식 가게 목록을 반환합니다.
     * </p>
     * @param saleStoreAddress 검색할 주소 문자열
     * @return 해당 주소를 포함하는 가게 목록
     */

    List<SaleStore> findBySaleStoreAddressContainingOrderBySaleDiscountDesc(String saleStoreAddress);

    /**
     * 특정 이름을 가진 가게를 조회합니다.
     *
     * @param saleStoreName 검색할 가게 이름
     * @return 해당 이름을 가진 가게 정보
     */

    Optional<SaleStore> findBySaleStoreName(String saleStoreName);
}
