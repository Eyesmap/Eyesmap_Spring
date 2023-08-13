package com.spring.eyesmap.domain.report.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.eyesmap.domain.report.domain.Location;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class ReportDto {
    @Data
    public static class ReportListRequest{
        @JsonInclude(NON_NULL)
        private Sort sort;
        @JsonInclude(NON_NULL)
        private ReportEnum.DamagedStatus damagedStatus;
        private String location;

        public ReportListRequest(Sort sort){
            this.sort = sort;
        }
        public ReportListRequest(ReportEnum.DamagedStatus damagedStatus){
            this.damagedStatus = damagedStatus;
        }
    }

    @Data
    public static class ReportListResponse{
        private String reportId;
        private Sort sort;
        private String contents;
        private ReportEnum.DamagedStatus damagedStatus;
        private String title;
        private LocalDateTime reportDate;
        private String userId;
        private List<String> imageUrls;
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

        private String accountId; // 이후 토큰으로 변경
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

        private String accountId;


        @Builder
        public CreateReportResponse(Location location, Report report, List<String> imageUrls, String accountId){
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
}
