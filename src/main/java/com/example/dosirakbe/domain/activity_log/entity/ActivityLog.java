package com.example.dosirakbe.domain.activity_log.entity;

import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


/**
 * packageName    : com.example.dosirakbe.domain.activity_log.entity<br>
 * fileName       : ActivityLog<br>
 * author         : Fiat_lux<br>
 * date           : 10/31/24<br>
 * description    : 활동 로그 엔티티 클래스입니다. 사용자의 활동 기록을 데이터베이스에 저장하고 관리합니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/31/24        Fiat_lux                최초 생성<br>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_logs")
public class ActivityLog extends BaseEntity {

    /**
     * 활동 로그의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 데이터베이스에서 자동 생성되는 고유한 ID를 나타냅니다.
     * </p>
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 활동 로그와 관련된 콘텐츠의 ID 입니다.
     *
     * <p>
     * 이 필드는 활동 로그가 특정 콘텐츠와 연관되어 있을 경우 해당 콘텐츠의 ID를 나타냅니다.
     * </p>
     */
    @Column(name = "content_id")
    private Long contentId;

    /**
     * 활동을 수행한 사용자입니다.
     *
     * <p>
     * 이 필드는 {@link User} 엔티티와의 다대일 관계를 나타내며, 활동을 수행한 사용자를 참조합니다.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 활동의 유형을 나타냅니다.
     *
     * <p>
     * 이 필드는 {@link ActivityType} 열거형을 사용하여 활동의 종류를 정의합니다.
     * </p>
     */
    @Column(name = "activity_type")
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    /**
     * 활동 중 이동한 거리입니다.
     *
     * <p>
     * 이 필드는 사용자가 활동을 수행하면서 이동한 거리를 나타내며, {@link BigDecimal} 형식으로 저장됩니다.
     * </p>
     */
    @Column(name = "distance")
    private BigDecimal distance;

    /**
     * 특정 콘텐츠와 관련된 활동 로그를 생성하는 생성자입니다.
     *
     * <p>
     * 이 생성자는 활동 로그의 콘텐츠 ID, 사용자, 활동 유형을 초기화합니다.
     * </p>
     *
     * @param contentId    활동 로그와 관련된 콘텐츠의 ID
     * @param user         활동을 수행한 사용자
     * @param activityType 활동의 유형
     */
    public ActivityLog(Long contentId, User user, ActivityType activityType) {
        this.contentId = contentId;
        this.user = user;
        this.activityType = activityType;
    }

    /**
     * 특정 콘텐츠와 관련된 활동 로그를 생성하는 생성자입니다.
     *
     * <p>
     * 이 생성자는 활동 로그의 콘텐츠 ID, 사용자, 활동 유형, 이동 거리를 초기화합니다.
     * </p>
     *
     * @param contentId    활동 로그와 관련된 콘텐츠의 ID
     * @param user         활동을 수행한 사용자
     * @param activityType 활동의 유형
     * @param distance     활동 중 이동한 거리
     */
    public ActivityLog(Long contentId, User user, ActivityType activityType, BigDecimal distance) {
        this.contentId = contentId;
        this.user = user;
        this.activityType = activityType;
        this.distance = distance;
    }
}
