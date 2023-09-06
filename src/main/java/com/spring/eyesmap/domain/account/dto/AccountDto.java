package com.spring.eyesmap.domain.account.dto;

import com.spring.eyesmap.global.enumeration.ReportEnum;
import lombok.*;

import java.time.LocalDateTime;
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



    @Getter
    public static class FetchReportListRequestDto {
        private Double userGpsX;
        private Double userGpsY;
    }

    @Getter
    public static class FetchDangerousCntListRequestDto {
        private Double userGpsX;
        private Double userGpsY;
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

    @Getter
    @AllArgsConstructor
    public static class MyPageList{

        private String reportId;
        private List<String> imageName;
        private Double gpsX;
        private Double gpsY;
        private ReportEnum.Sort sort;
        private ReportEnum.DamagedStatus damagedStatus;
        private Integer dangerousCnt;
        private String address;
        private LocalDateTime reportDate;
        private boolean dangerBtnClicked;
        private Double distance;
        private String title;

    }

    @Data
    @Getter
    public static class RankingResponseDto {

        private List<RankingListTop3> rankingListTop3;
        private List<OtherRankingList> otherRankingList;
        @Builder
        public RankingResponseDto(List<RankingListTop3> rankingListTop3, List<OtherRankingList> otherRankingList){
            this.rankingListTop3 = rankingListTop3;
            this.otherRankingList = otherRankingList;
        }
    }

    @Data
    @AllArgsConstructor
    public static class RankingListTop3 {
        private Integer rank;
        private Long userId;
        private String nickname;
        private String profileImageUrl;
        private Long reportCnt;
        private String medalImage;
    }

    @Data
    @AllArgsConstructor
    public static class OtherRankingList {
        private Integer rank;
        private Long userId;
        private String nickname;
        private String profileImageUrl;
        private Long reportCnt;
    }

    @Data
    @AllArgsConstructor
    public static class FetchAccountResponseDto {
        private String nickname;
        private String profileImageUrl;
        private String imageName;
    }

    @Data
    @Getter
    public static class UpdateProfileImageReqeuestDto {
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    public static class MyReportResponseDto {
        private String nickname;
        private String profileImageUrl;
        private Integer ReportCnt;
    }
}
