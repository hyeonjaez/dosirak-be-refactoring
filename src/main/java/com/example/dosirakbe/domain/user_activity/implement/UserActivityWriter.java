package com.example.dosirakbe.domain.user_activity.implement;

import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import com.example.dosirakbe.domain.user_activity.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserActivityWriter {
    private final UserActivityRepository userActivityRepository;

    public UserActivity createUserActivity(UserActivity userActivity) {
        return userActivityRepository.save(userActivity);
    }

    public void addCommitUserActivity(UserActivity userActivity) {
        userActivity.addCommitCount();
        createUserActivity(userActivity);
    }
}
