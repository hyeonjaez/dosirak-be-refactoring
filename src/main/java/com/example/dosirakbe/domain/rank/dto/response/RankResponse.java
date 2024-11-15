package com.example.dosirakbe.domain.rank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankResponse {

    private Long userId;
    private String profileImg;
    private Integer rank;
    private String nickname;
    private Integer reward;
}
