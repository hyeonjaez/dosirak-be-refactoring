package com.example.dosirakbe.domain.user_activity.repository;

import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.user_activity.repository<br>
 * fileName       : UserActivityRepository<br>
 * author         : Fiat_lux<br>
 * date           : 11/02/24<br>
 * description    : 사용자 활동 기록에 대한 데이터 접근을 담당하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/02/24        Fiat_lux                최초 생성<br>
 */
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    /**
     * 특정 사용자의 지정된 기간 내의 활동 기록을 조회하여 생성일 기준 오름차순으로 정렬된 리스트를 반환합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}, {@code startDate}, {@code endDate}를 기반으로 해당 사용자의 활동 기록을
     * 조회하며, 생성일을 기준으로 오름차순으로 정렬하여 반환합니다.
     * </p>
     *
     * @param user      활동 기록을 조회할 {@link User} 엔티티 객체
     * @param startDate 조회 시작 날짜를 나타내는 {@link LocalDate} 객체
     * @param endDate   조회 종료 날짜를 나타내는 {@link LocalDate} 객체
     * @return 지정된 기간 내의 활동 기록을 포함하는 {@link List} 형태의 {@link UserActivity} 객체 리스트
     */
    List<UserActivity> findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(User user, LocalDate startDate, LocalDate endDate);

    /**
     * 특정 사용자의 특정 날짜에 대한 활동 기록을 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}와 {@code localDate}를 기반으로 해당 사용자의 특정 날짜에 대한 활동 기록을 조회합니다.
     * 활동 기록이 존재할 경우 {@link Optional}로 감싸서 반환하며, 존재하지 않을 경우 빈 {@link Optional}을 반환합니다.
     * </p>
     *
     * @param user      활동 기록을 조회할 {@link User} 엔티티 객체
     * @param localDate 조회할 활동 기록의 날짜를 나타내는 {@link LocalDate} 객체
     * @return 특정 날짜에 대한 활동 기록을 포함하는 {@link Optional} 형태의 {@link UserActivity} 객체
     */
    Optional<UserActivity> findByUserAndCreatedAt(User user, LocalDate localDate);

    /**
     * 특정 사용자의 모든 활동 기록을 삭제합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}에 속한 모든 {@link UserActivity} 기록을 데이터베이스에서 삭제합니다.
     * </p>
     *
     * @param user 삭제할 활동 기록을 소유한 {@link User} 엔티티 객체
     */
    void deleteByUser(User user);

    /**
     * 특정 사용자의 활동 기록 존재 여부를 확인합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}가 하나 이상의 {@link UserActivity} 기록을 가지고 있는지 여부를 확인하여
     * {@code true} 또는 {@code false}를 반환합니다.
     * </p>
     *
     * @param user 활동 기록의 존재 여부를 확인할 {@link User} 엔티티 객체
     * @return 주어진 사용자가 활동 기록을 가지고 있을 경우 {@code true}, 그렇지 않을 경우 {@code false}
     */
    boolean existsByUser(User user);
}
