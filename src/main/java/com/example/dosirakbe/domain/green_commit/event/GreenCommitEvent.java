package com.example.dosirakbe.domain.green_commit.event;

import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

/**
 * packageName    : com.example.dosirakbe.domain.green_commit.event<br>
 * fileName       : GreenCommitEvent<br>
 * author         : Fiat_lux<br>
 * date           : 11/03/24<br>
 * description    : 그린 커밋 활동과 관련된 이벤트를 정의하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/03/24        Fiat_lux                최초 생성<br>
 */
@Getter
public class GreenCommitEvent extends ApplicationEvent {

    /**
     * 이벤트를 발생시킨 사용자의 고유 ID 입니다.
     *
     * <p>
     * 이 필드는 그린 커밋 활동을 수행한 사용자의 고유 식별자를 나타냅니다.
     * </p>
     */
    private final Long userId;

    /**
     * 이벤트와 관련된 콘텐츠의 고유 ID입니다.
     *
     * <p>
     * 이 필드는 그린 커밋 활동이 관련된 특정 콘텐츠의 고유 식별자를 나타냅니다.
     * </p>
     */
    private final Long contentId;

    /**
     * 활동의 유형을 나타냅니다.
     *
     * <p>
     * 이 필드는 {@link ActivityType} 열거형을 사용하여 그린 커밋 활동의 종류를 정의합니다.
     * </p>
     */
    private final ActivityType activityType;

    /**
     * 활동 중 이동한 거리입니다.
     *
     * <p>
     * 이 필드는 사용자가 활동을 수행하면서 이동한 거리를 나타내며, {@link BigDecimal} 형식으로 저장됩니다.
     * {@link ActivityType#LOW_CARBON_MEANS_OF_TRANSPORTATION} 활동 유형과 관련이 있습니다.
     * </p>
     */
    private BigDecimal distance;

    /**
     * 그린 커밋 활동 이벤트를 생성하는 생성자입니다.
     *
     * <p>
     * 이 생성자는 사용자 ID, 콘텐츠 ID, 활동 유형을 초기화하고 이벤트 소스를 설정합니다.
     * 이동 거리는 포함되지 않습니다.
     * </p>
     *
     * @param source       이벤트 소스 객체
     * @param userId       활동을 수행한 사용자 ID
     * @param contentId    활동과 관련된 콘텐츠 ID
     * @param activityType 활동의 유형 {@link ActivityType}
     */
    public GreenCommitEvent(Object source, Long userId, Long contentId, ActivityType activityType) {
        super(source);
        this.userId = userId;
        this.contentId = contentId;
        this.activityType = activityType;
    }

    /**
     * 그린 커밋 활동 이벤트를 생성하는 생성자입니다.
     *
     * <p>
     * 이 생성자는 사용자 ID, 콘텐츠 ID, 활동 유형, 이동 거리를 초기화하고 이벤트 소스를 설정합니다.
     * 이동 거리는 {@link ActivityType#LOW_CARBON_MEANS_OF_TRANSPORTATION} 활동 유형과 관련이 있습니다.
     * </p>
     *
     * @param source       이벤트 소스 객체
     * @param userId       활동을 수행한 사용자 ID
     * @param contentId    활동과 관련된 콘텐츠 ID
     * @param activityType 활동의 유형 {@link ActivityType}
     * @param distance     활동 중 이동한 거리
     */
    public GreenCommitEvent(Object source, Long userId, Long contentId, ActivityType activityType, BigDecimal distance) {
        super(source);
        this.userId = userId;
        this.contentId = contentId;
        this.activityType = activityType;
        this.distance = distance;
    }
}
