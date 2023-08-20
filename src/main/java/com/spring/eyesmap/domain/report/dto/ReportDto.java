package com.spring.eyesmap.domain.report.dto;

import com.spring.eyesmap.domain.report.domain.Location;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReportDto {
    @Data
    public static class ReportListRequest{
        private String address;
    }

    @Data
    public static class ReportListResponse{
        private String reportId;
        private ReportEnum.Sort sort;
        private String contents;
        private ReportEnum.DamagedStatus damagedStatus;
        private String title;
        private LocalDateTime reportDate;
        private Long userId;
        private List<String> imageUrls;

        public ReportListResponse(Report report, Long userId, List<String> imageUrls){
            this.reportId = report.getReportId();
            this.sort = report.getSort();
            this.contents = report.getContents();
            this.damagedStatus = report.getDamagedStatus();
            this.reportDate = report.getReportDate();
            this.title = report.getTitle();
            this.sort = report.getSort();
            this.contents = report.getContents();
            this.userId = userId;
            this.imageUrls = imageUrls;
        }
    }

    @Getter
    @Builder
    public static class CreateReportRequest{
        private String address;
        private String gpsX;
        private String gpsY;

        private String title;
        private String contents;
        @Enumerated(EnumType.STRING)
        private ReportEnum.DamagedStatus damagedStatus;
        @Enumerated(EnumType.STRING)
        private ReportEnum.Sort sort;

        private Long accountId; // 이후 토큰으로 변경
    }

    @Getter
    public static class CreateReportResponse{
        private String address;
        private String gpsX;
        private String gpsY;
        private String title;
        private String contents;
        @Enumerated(EnumType.STRING)
        private ReportEnum.DamagedStatus damagedStatus;
        @Enumerated(EnumType.STRING)
        private ReportEnum.Sort sort;
        private List<String> imageUrls;
        private Long accountId;

        @Builder
        public CreateReportResponse(Location location, Report report, List<String> imageUrls, Long accountId){
            this.address = location.getAddress();
            this.gpsX = location.getGpsX();
            this.gpsY = location.getGpsY();

            this.title = report.getTitle();
            this.contents = report.getContents();
            this.damagedStatus = report.getDamagedStatus();
            this.sort = report.getSort();

            this.imageUrls = imageUrls;
            this.accountId = accountId;
        }
    }

    @Getter
    public static class ReportResponse{
        private String address;
        private String gpsX;
        private String gpsY;

        private String title;
        private String contents;
        @Enumerated(EnumType.STRING)
        private ReportEnum.DamagedStatus damagedStatus;
        @Enumerated(EnumType.STRING)
        private ReportEnum.Sort sort;
        private LocalDateTime reportDate;
        private Integer dangerousCnt;

        private List<String> imageUrls;
        @Builder
        public ReportResponse(Location location, Report report, List<String> imageUrls){
            this.address = location.getAddress();
            this.gpsX = location.getGpsX();
            this.gpsY = location.getGpsY();

            this.title = report.getTitle();
            this.contents = report.getContents();
            this.damagedStatus = report.getDamagedStatus();
            this.sort = report.getSort();
            this.reportDate = report.getReportDate();
            this.dangerousCnt = report.getDangerousCnt();

            this.imageUrls = imageUrls;
        }
    }
    @Getter
    @RequiredArgsConstructor
    public static class DeleteReportRequest{
        private Long userId;
        private String reportId;
        @Enumerated(EnumType.STRING)
        private ReportEnum.DeleteReason deleteReason;
    }
}
