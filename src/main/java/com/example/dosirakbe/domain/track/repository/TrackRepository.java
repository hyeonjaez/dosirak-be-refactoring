package com.example.dosirakbe.domain.track.repository;

import com.example.dosirakbe.domain.track.entity.Track;
import com.example.dosirakbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.track.repository<br>
 * fileName       : TrackRepository<br>
 * author         : Fiat_lux<br>
 * date           : 11/24/24<br>
 * description    : 트랙 엔티티에 대한 데이터 접근을 담당하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/24/24        Fiat_lux                최초 생성<br>
 */
public interface TrackRepository extends JpaRepository<Track, Long> {

    /**
     * 특정 사용자가 이동한 판매점 이름을 포함하는 트랙 기록을 생성 시각 내림차순으로 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}가 이동한 판매점 이름에 {@code storeName} 키워드를 포함하는 모든 {@link Track} 엔티티를
     * 생성 시각 내림차순으로 정렬하여 반환합니다. 검색은 대소문자를 구분하지 않습니다.
     * </p>
     *
     * @param user      트랙 기록을 조회할 사용자 {@link User} 엔티티
     * @param storeName 검색할 판매점 이름 키워드 {@link String}
     * @return 주어진 조건에 맞는 {@link Track} 엔티티 리스트
     */
    List<Track> findByUserAndStoreNameContainingIgnoreCaseOrderByCreatedAtDesc(User user, String storeName);

}
