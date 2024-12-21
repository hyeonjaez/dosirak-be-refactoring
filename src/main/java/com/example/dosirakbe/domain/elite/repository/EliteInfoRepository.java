package com.example.dosirakbe.domain.elite.repository;

import com.example.dosirakbe.domain.elite.entity.EliteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.elite.repository<br>
 * fileName       : EliteInfoRepository<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : EliteInfo 엔티티와 관련된 데이터베이스 접근 메서드를 정의한 Repository 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
public interface EliteInfoRepository extends JpaRepository<EliteInfo, Long> {

    /**
     * 특정 사용자 ID로 EliteInfo를 조회합니다.<br>
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 EliteInfo를 감싸는 Optional 객체
     */
    Optional<EliteInfo> findByUserId(Long userId);
}
