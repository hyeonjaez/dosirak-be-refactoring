package com.example.dosirakbe.domain.rank.service;

import com.example.dosirakbe.domain.rank.dto.response.RankResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.rank.service<br>
 * fileName       : RankService<br>
 * author         : yyujin1231<br>
 * date           : 12/20/24<br>
 * description    : 랭킹 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 12/20/24        yyujin1231                최초 생성<br>
 */


@Service
@RequiredArgsConstructor
public class RankService {

    @Autowired
    private UserRepository userRepository;


    /**
     * 매일 자정에 랭킹 정보를 업데이트합니다.
     *
     * <p>
     * 이 메서드는 24시간에 한 번 실행되며, 캐시된 모든 랭킹 데이터를 제거한 후 새로운 랭킹 정보를 갱신합니다.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * *")
    @CacheEvict(value = "ranking", allEntries = true)
    public void updateRank() {
        getRankedUsers();
    }


    /**
     * 모든 사용자들의 랭킹 정보를 반환합니다.
     *
     * <p>
     * 이 메서드는 사용자 데이터를 리워드 따라 내림차순으로 정렬한 후, 랭킹 정보를 생성하여 반환합니다.
     * 결과는 캐시에 저장됩니다.
     * </p>
     *
     * @return 리워드를 기준으로 정렬된 {@link RankResponse} 목록
     */

    @Cacheable(value = "ranking")
    public List<RankResponse> getRankedUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "reward"));
        List<RankResponse> rankedUsers = new ArrayList<>();

        int rank = 0;

        int prevReward = -1;

        int equivalentUser = 0;

        for (User user : users) {
            if (user.getNickName() != null && user.getNickName().startsWith("(알 수 없음)")) {
                continue;
            }

            if (user.getReward() != prevReward) {
                rank += equivalentUser + 1;
                equivalentUser = 0;
            } else {
                equivalentUser++;
            }

            rankedUsers.add(new RankResponse(
                    user.getUserId(),
                    user.getProfileImg(),
                    rank,
                    user.getNickName(),
                    user.getReward()
            ));

            prevReward = user.getReward();
        }

        return rankedUsers;
    }

    /**
     * 특정 사용자의 랭킹 정보를 반환합니다.
     *
     * <p>
     * 이 메서드는 사용자 ID를 기반으로 랭킹 정보를 검색하거나, 랭킹 정보가 없을 경우 새 랭킹 정보를 생성하여 반환합니다.
     * </p>
     *
     * @param userId 랭킹 정보를 조회할 사용자의 고유 식별자
     * @return 사용자의 랭킹 정보를 포함하는 {@link RankResponse}
     * @throws ApiException 사용자 정보가 존재하지 않을 경우 예외 발생
     */

    public RankResponse getRankByUserId(Long userId) {

        List<RankResponse> rankedUsers = getRankedUsers();

        return rankedUsers.stream()
                .filter(rankResponse -> rankResponse.getUserId().equals(userId))
                .findFirst()
                .orElseGet(() -> {

                    int allRank = rankedUsers.isEmpty() ? 0 : rankedUsers.get(rankedUsers.size() - 1).getRank();
                    int rank = allRank + 1;

                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

                    return new RankResponse(
                            user.getUserId(),
                            user.getProfileImg(),
                            rank,
                            user.getNickName(),
                            user.getReward()
                    );
                });
    }

}
