package com.example.dosirakbe.domain.user_activity.helper;

import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import com.example.dosirakbe.domain.user_activity.repository.UserActivityRepository;
import com.example.dosirakbe.global.util.ObjectsUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserActivityWriter {
    private final UserActivityRepository userActivityRepository;

    public UserActivity createUserActivity(UserActivity userActivity) {
        ObjectsUtility.checkAllNotNull(userActivity);

        return userActivityRepository.save(userActivity);
    }

    public void addCommitUserActivity(UserActivity userActivity) {
        ObjectsUtility.checkAllNotNull(userActivity);

        userActivity.addCommitCount();
        createUserActivity(userActivity);
    }
}
