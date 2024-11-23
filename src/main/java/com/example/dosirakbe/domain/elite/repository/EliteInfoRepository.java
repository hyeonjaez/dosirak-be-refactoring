package com.example.dosirakbe.domain.elite.repository;



import com.example.dosirakbe.domain.elite.entity.EliteInfo;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface EliteInfoRepository extends JpaRepository<EliteInfo, Long> {
    // 사용자 ID로 EliteInfo 조회
    Optional<EliteInfo> findByUserId(Long userId);

}
