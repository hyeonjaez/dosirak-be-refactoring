package com.example.dosirakbe.domain.store.repository;


import com.example.dosirakbe.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.store.repository<br>
 * fileName       : StoreRepository<br>
 * author         : yyujin1231<br>
 * date           : 10/25/24<br>
 * description    : 가게 데이터를 조회 및 검색하기 위한 JPA 레포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        yyujin1231                최초 생성<br>
 */


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * 키워드를 포함한 가게 이름으로 가게를 검색합니다.
     * @param keyword 검색할 키워드
     * @return 키워드를 포함한 가게 목록
     */

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:keyword%")
    List<Store> searchStoresByKeyword(@Param("keyword") String keyword);

    /**
     * 특정 카테고리에 속한 가게 목록을 조회합니다.
     * @param storeCategory 조회할 카테고리 이름
     * @return 해당 카테고리에 속한 가게 목록
     */

    List<Store> findByStoreCategory(String storeCategory);

    /**
     * 사용자의 위치를 기준으로 1km 반경 내의 가게를 조회합니다.
     * <p>
     * 이 쿼리는 하버사인 공식을 사용하여 거리를 계산하며,
     * 1km 반경 내의 가게를 거리 순으로 정렬하여 반환합니다.
     * </p>
     * @param mapX 사용자의 현재 X 좌표 (위도)
     * @param mapY 사용자의 현재 Y 좌표 (경도)
     * @return 1km 반경 내의 가게 목록
     */


    @Query("SELECT s, " +
            "(6371 * acos(cos(radians(:mapX)) * cos(radians(s.mapX)) " +
            "* cos(radians(s.mapY) - radians(:mapY)) + sin(radians(:mapX)) * sin(radians(s.mapX)))) " +
            "AS distance " +
            "FROM Store s " +
            "WHERE (6371 * acos(cos(radians(:mapX)) * cos(radians(s.mapX)) " +
            "* cos(radians(s.mapY) - radians(:mapY)) + sin(radians(:mapX)) * sin(radians(s.mapX)))) <= 1 " +
            "ORDER BY distance ASC")
    List<Store> findStoresIn1Km(@Param("mapX") double mapX, @Param("mapY") double mapY);

}
