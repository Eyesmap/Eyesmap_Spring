package com.spring.eyesmap.domain.report.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.global.enumeration.DistrictNum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class DataAnalysisDto {
    @Getter
    @Builder
    public static class DangerousTop10GuListResponse{
        private Long allReportsCnt;
        private String currentDateAndHour;
        private List<DangerousLocationResponse> top3Location;
        private List<DangerousLocationResponse> theOthers;
    }
    @Getter
    public static class DangerousLocationResponse{
        private Integer rank;
        @Enumerated(EnumType.ORDINAL)
        private Integer guNum;
        @Enumerated(EnumType.STRING)
        private String guName;
        private Long reportCount;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String medal;

        public DangerousLocationResponse(Integer rank, DistrictNum gu, Long reportCount) {
            this.rank = rank;
            this.guNum = gu.getNum();
            this.guName = gu.getName();
            this.reportCount = reportCount;
        }
        public void setMedal(String medal){
            this.medal = medal;
        }
    }

    @Getter
    @Builder
    public static class DangerousTop10ReportListResponse{
        private Long allReportsCnt;
        private String currentDateAndHour;
        private List<DangerousReportPerGuResponse> top3Report;
        private List<DangerousReportPerGuResponse> theOthers;
    }
    @Getter
    public static class DangerousReportPerGuResponse{
        private String reportId;
        private Integer rank;
        private Integer count;
        private String address;
        private String title;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String medal;

        @Builder
        public DangerousReportPerGuResponse(Report report, Integer rank) {
            this.reportId = report.getReportId();
            this.rank = rank;
            this.count = report.getReportDangerousNum();
            this.address = report.getLocation().getAddress();
            this.title = report.getTitle();
        }
        public void setMedal( String medal){
            this.medal = medal;
        }
    }
}
