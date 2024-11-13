package com.example.dosirakbe.domain.track.service;

import com.example.dosirakbe.domain.activity_log.repository.ActivityLogRepository;
import com.example.dosirakbe.domain.track.dto.request.TrackMoveRequest;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user_activity.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.Track;

@RequiredArgsConstructor
@Service
@Transactional
public class TrackService {
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;
    private final ActivityLogRepository activityLogRepository;

    public void recordTrackDistance(Long userId, TrackMoveRequest trackMoveRequest) {
        User user = userRepository.findById(userId).orElseThrow();

    }
}
