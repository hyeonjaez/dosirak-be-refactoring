package com.example.dosirakbe.domain.green_commit.event;

import com.example.dosirakbe.domain.activity_log.service.ActivityLogService;
import com.example.dosirakbe.domain.user_activity.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.dosirakbe.domain.green_commit.event<br>
 * fileName       : GreenCommitEventListener<br>
 * author         : Fiat_lux<br>
 * date           : 11/03/24<br>
 * description    : 그린 커밋 활동 이벤트를 처리하는 이벤트 리스너 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/03/24        Fiat_lux                최초 생성<br>
 */
@Component
@RequiredArgsConstructor
public class GreenCommitEventListener {

    /**
     * 활동 로그와 관련된 비즈니스 로직을 처리하는 서비스입니다.
     *
     * <p>
     * 이 필드는 {@link ActivityLogService}를 주입받아 활동 로그를 추가하는 기능을 제공합니다.
     * </p>
     */
    private final ActivityLogService activityLogService;

    /**
     * 사용자 활동과 관련된 비즈니스 로직을 처리하는 서비스입니다.
     *
     * <p>
     * 이 필드는 {@link UserActivityService}를 주입받아 사용자 활동을 생성하거나 증가시키는 기능을 제공합니다.
     * </p>
     */
    private final UserActivityService userActivityService;

    /**
     * {@link GreenCommitEvent} 이벤트가 발생했을 때 호출되는 메서드입니다.
     *
     * <p>
     * 이 메서드는 {@link GreenCommitEvent} 이벤트를 수신하여,
     * 해당 사용자의 활동 로그를 추가하고 사용자 활동을 생성 또는 증가시킵니다.
     * </p>
     *
     * @param event 처리할 {@link GreenCommitEvent} 이벤트 객체
     */
    @EventListener
    public void handleGreenCommitEvent(GreenCommitEvent event) {
        activityLogService.addActivityLog(event.getUserId(), event.getContentId(), event.getActivityType(), event.getDistance());
        userActivityService.createOrIncrementUserActivity(event.getUserId());
    }

}
