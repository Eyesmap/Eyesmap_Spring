package com.spring.eyesmap.domain.report.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class DataAnalysisDto {
    @Getter
    @Builder
    public static class DangerousTop10LocationListResponse{
        private List<DangerousLocationResponse> top3Location;
        private List<DangerousLocationResponse> theOthers;
    }
    @Getter
    public static class DangerousLocationResponse{
        private Integer rank;
        private Integer guNum;
        private Long reportCount;

        public DangerousLocationResponse(Integer rank, Integer gu, Long reportCount) {
            this.rank = rank;
            this.guNum = gu;
            this.reportCount = reportCount;
        }
    }
}
