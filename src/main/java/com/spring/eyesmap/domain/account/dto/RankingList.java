package com.spring.eyesmap.domain.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingList {
    private Long userId;
    private String nickname;
    private Long reportCnt;

    public RankingList(Long userId, String nickname, Long reportCnt) {
        this.userId = userId;
        this.nickname = nickname;
        this.reportCnt = reportCnt;
    }
}
