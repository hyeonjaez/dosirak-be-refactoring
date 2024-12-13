package com.example.dosirakbe.domain.activity_log.repository;

import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import com.example.dosirakbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


/**
 * packageName    : com.example.dosirakbe.domain.activity_log.repository<br>
 * fileName       : ActivityLogRepository<br>
 * author         : Fiat_lux<br>
 * date           : 11/03/24<br>
 * description    : 활동 로그 엔티티에 대한 데이터 접근을 담당하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/03/24        Fiat_lux                최초 생성<br>
 */
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    /**
     * 특정 사용자와 지정된 기간 내에 생성된 모든 활동 로그를 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@link User}와 {@code start}, {@code end} 시각 사이에 생성된
     * 모든 {@link ActivityLog} 엔티티를 조회하여 생성 시각의 오름차순으로 정렬된 리스트를 반환합니다.
     * </p>
     *
     * @param user  조회할 활동 로그의 소유자 {@link User} 엔티티
     * @param start 조회 기간의 시작 시각 {@link LocalDateTime}
     * @param end   조회 기간의 종료 시각 {@link LocalDateTime}
     * @return 조회된 {@link ActivityLog} 엔티티의 리스트
     */
    List<ActivityLog> findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(User user, LocalDateTime start, LocalDateTime end);

    /**
     * 특정 사용자의 모든 활동 로그를 삭제합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@link User}와 관련된 모든 {@link ActivityLog} 엔티티를 삭제합니다.
     * </p>
     *
     * @param user 삭제할 활동 로그의 소유자 {@link User} 엔티티
     */
    void deleteByUser(User user);

    /**
     * 특정 사용자가 활동 로그를 가지고 있는지 여부를 확인합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@link User}와 관련된 {@link ActivityLog} 엔티티가 존재하는지 여부를 반환합니다.
     * </p>
     *
     * @param user 확인할 활동 로그의 소유자 {@link User} 엔티티
     * @return 사용자가 활동 로그를 가지고 있으면 {@code true}, 그렇지 않으면 {@code false}
     */
    boolean existsByUser(User user);
}
