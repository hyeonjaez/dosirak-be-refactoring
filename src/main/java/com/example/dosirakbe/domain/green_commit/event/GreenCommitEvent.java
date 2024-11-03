package com.example.dosirakbe.domain.green_commit.event;

import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GreenCommitEvent extends ApplicationEvent {
    private final Long userId;
    private final Long contentId;
    private final ActivityType activityType;

    public GreenCommitEvent(Object source, Long userId, Long contentId, ActivityType activityType) {
        super(source);
        this.userId = userId;
        this.contentId = contentId;
        this.activityType = activityType;
    }
}
