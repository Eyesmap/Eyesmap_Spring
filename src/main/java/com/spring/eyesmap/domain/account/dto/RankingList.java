package com.spring.eyesmap.domain.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingList {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private Long reportCnt;

    public RankingList(Long userId, String nickname, String profileImageUrl, Long reportCnt) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.reportCnt = reportCnt;
    }
}
