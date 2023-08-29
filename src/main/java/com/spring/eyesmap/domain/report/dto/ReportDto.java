package com.spring.eyesmap.domain.report.dto;

import com.spring.eyesmap.domain.report.domain.Location;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReportDto {

    @Data
    public static class ReportListResponse{
        private String reportId;
        private Double gpsX;
        private Double gpsY;

        public ReportListResponse(Report report){
            this.reportId = report.getReportId();
            this.gpsX = report.getLocation().getGpsX();
            this.gpsY = report.getLocation().getGpsY();
        }
    }

    @Getter
    public static class ReportMarkRequest{
        private String reportId;
        private Double userGpsX;
        private Double userGpsY;
    }

    @Getter
    public static class ReportMarkResponse{
        private String reportId;
        private ReportEnum.Sort sort;
        private ReportEnum.DamagedStatus damagedStatus;
        private String title;
        private List<String> imageUrls;
        private Integer dangerousCnt;
        private Double distance;
        @Builder
        public ReportMarkResponse(Report report, Double distance, List<String> imageUrls){
            this.reportId = report.getReportId();
            this.sort = report.getSort();
            this.damagedStatus = report.getDamagedStatus();
            this.title = report.getTitle();
            this.sort = report.getSort();
            this.dangerousCnt = report.getDangerousCnt();
            this.distance = distance;
            this.imageUrls = imageUrls;
        }
    }

    @Getter
    @Builder
    public static class CreateReportRequest{
        private String address;
        private Double gpsX;
        private Double gpsY;

        private String title;
        private String contents;
        @Enumerated(EnumType.STRING)
        private ReportEnum.DamagedStatus damagedStatus;
        @Enumerated(EnumType.STRING)
        private ReportEnum.Sort sort;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRestoreReportRequest{
        private String reportId;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportDangerousCntRequest{
        private String reportId;
    }

    @Getter
    public static class CreateReportResponse{
        private String address;
        private Double gpsX;
        private Double gpsY;
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
        private String contents;
        private LocalDateTime reportDate;
        @Builder
        public ReportResponse(Location location, Report report){
            this.address = location.getAddress();
            this.contents = report.getContents();
            this.reportDate = report.getReportDate();
        }
    }
    @Getter
    @RequiredArgsConstructor
    public static class DeleteReportRequest{
        private String reportId;
        @Enumerated(EnumType.STRING)
        private ReportEnum.DeleteReason deleteReason;
    }
}
