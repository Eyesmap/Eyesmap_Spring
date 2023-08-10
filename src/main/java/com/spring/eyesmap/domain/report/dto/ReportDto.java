package com.spring.eyesmap.domain.report.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class ReportDto {
    @Data
    @Builder
    public static class ReportListRequestFromAPI{
        String key;
        String telno;
        String startDate;
        String endDate;
        String startNum;
        String endNum;
    }

    @Data
    public static class ReportListResponseFromAPI{
        private String citizenid;
        private String reportDt;
        private String contents;
        private String openflag;
        private String passflag;
        private String citizengroupTypeCd;
        private String citizengroupNm;
        private String dx;
        private String dy;
        private String mobiledx;
        private String mobiledy;
    }
}
