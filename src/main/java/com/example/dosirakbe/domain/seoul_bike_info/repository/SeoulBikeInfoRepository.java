package com.example.dosirakbe.domain.seoul_bike_info.repository;

import com.example.dosirakbe.domain.seoul_bike_info.entity.SeoulBikeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SeoulBikeInfoRepository extends JpaRepository<SeoulBikeInfo, String> {

    @Query(value = "SELECT *, " +
            "(6371 * ACOS(COS(RADIANS(:latitude)) " +
            "* COS(RADIANS(s.`위도`)) " +
            "* COS(RADIANS(s.`경도`) - RADIANS(:longitude)) " +
            "+ SIN(RADIANS(:latitude)) " +
            "* SIN(RADIANS(s.`위도`)))) AS distance " +
            "FROM seoul_bike_info s " +
            "HAVING distance <= 0.5 " +
            "ORDER BY distance", nativeQuery = true)
    List<SeoulBikeInfo> findAllWithinDistance(@Param("latitude") BigDecimal latitude,
                                              @Param("longitude") BigDecimal longitude);
}
