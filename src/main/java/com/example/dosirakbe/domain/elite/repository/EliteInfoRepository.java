package com.example.dosirakbe.domain.elite.repository;


import com.example.dosirakbe.domain.elite.entity.EliteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EliteInfoRepository extends JpaRepository<EliteInfo, Long> {

    @Query("SELECT e FROM EliteInfo e WHERE e.userId = :userId")
    EliteInfo findByUserId(Long userId);

}
