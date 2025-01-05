package com.example.dosirakbe.domain.rank.service;

import com.example.dosirakbe.domain.rank.dto.response.RankResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RankServiceTest {

    @InjectMocks
    private RankService rankService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(Long userId, String nickName, String profileImg, Integer reward) {
        User user = new User();
        user.setUserId(userId);
        user.setNickName(nickName);
        user.setProfileImg(profileImg);
        user.setReward(reward);
        return user;
    }

    @Test
    @DisplayName("getRankedUsers - 성공")
    void getRankedUsers_Success() {

        User mockUser1 = createUser(1L, "nickName1", "UserProfile1.png", 100);
        User mockUser2 = createUser(2L, "nickName2", "UserProfile2.png", 90);

        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2);


        when(userRepository.findAll(Sort.by(Sort.Direction.DESC, "reward"))).thenReturn(mockUsers);

        List<RankResponse> result = rankService.getRankedUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getRank());
        assertEquals("nickName1", result.get(0).getNickName());
    }

    @Test
    @DisplayName("getRankByUserId - 성공")
    void getRankByUserId_Success() {

        List<RankResponse> mockRanks = Arrays.asList(
                new RankResponse(1L, "userProfile1.png", 1, "nickName1", 100),
                new RankResponse(2L, "userProfile2.png", 2, "nickName2", 90)
        );

        User mockUser = createUser(1L, "nickName1", "userProfile1.png", 100);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));


        RankResponse result = rankService.getRankByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.getRank());
        assertEquals("nickName1", result.getNickName());
    }

    @Test
    @DisplayName("getRankByUserId - 실패 : 유저 not found")
    void getRankByUserId_Failure_UserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> rankService.getRankByUserId(1L));

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());
    }

}