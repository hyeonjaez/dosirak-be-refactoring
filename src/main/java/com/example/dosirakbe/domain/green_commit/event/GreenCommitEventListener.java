package com.example.dosirakbe.domain.green_commit.event;

import com.example.dosirakbe.domain.activity_log.service.ActivityLogService;
import com.example.dosirakbe.domain.user_activity.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GreenCommitEventListener {
    private final ActivityLogService activityLogService;
    private final UserActivityService userActivityService;

    @EventListener
    public void handleGreenCommitEvent(GreenCommitEvent event) {
        activityLogService.addActivityLog(event.getUserId(), event.getContentId(), event.getActivityType());
        userActivityService.createOrIncrementUserActivity(event.getUserId());
    }

}
