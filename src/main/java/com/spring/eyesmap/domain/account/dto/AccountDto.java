package com.spring.eyesmap.domain.account.dto;

import lombok.*;

import java.util.List;

public class AccountDto {


    @Data
    public static class LoginResponseDto {
        private String accessToken;
        private String refreshToken;

        public LoginResponseDto(String accessToken, String refreshToken){
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }


    @Data
    @Getter
    public static class ReportListResponseDto {
        private List<MyPageList> reportList;
        @Builder
        public ReportListResponseDto(List<MyPageList> reportList){
            this.reportList = reportList;
        }
    }

    @Data
    @Getter
    public static class DangerousCntListResponseDto {
        private List<MyPageList> reportList;
        @Builder
        public DangerousCntListResponseDto(List<MyPageList> reportList){
            this.reportList = reportList;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class MyPageList{

        private String reportId;
        private String imageUrl;
    }

    @Data
    @Getter
    public static class RankingResponseDto {
        private List<RankingList> rankingList;
        @Builder
        public RankingResponseDto(List<RankingList> rankingList){
            this.rankingList = rankingList;
        }
    }

}
