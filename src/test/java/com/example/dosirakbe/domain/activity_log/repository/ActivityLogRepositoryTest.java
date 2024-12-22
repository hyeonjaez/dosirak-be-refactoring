package com.example.dosirakbe.domain.activity_log.repository;

import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ActivityLogRepositoryTest {
    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUserAndCreatedAtBetweenOrderByCreatedAtAsc - 성공적으로 활동 로그를 조회")
    void findByUserAndCreatedAtBetweenOrderByCreatedAtAsc_Success() {
        User user = userRepository.save(new User(1L, "testName", "testUniqueName", "testNickName", "test@example.com", LocalDateTime.now(), LocalDateTime.now(), "testImg", true, 123, BigDecimal.TEN));
        ActivityLog log1 = new ActivityLog(1L, user, ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION, BigDecimal.ONE);
        ActivityLog log2 = new ActivityLog(2L, user, ActivityType.MULTI_USE_CONTAINER_PACKAGING);

        activityLogRepository.save(log1);
        activityLogRepository.save(log2);

        LocalDateTime start = LocalDateTime.now().minusHours(4);
        LocalDateTime end = LocalDateTime.now();
        List<ActivityLog> expected = List.of(log1, log2);
        List<ActivityLog> result = activityLogRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, start, end);

        assertThat(result).isNotEmpty();
        assertEquals(expected.size(), result.size());
        assertThat(result).containsExactly(log1, log2);


    }
}