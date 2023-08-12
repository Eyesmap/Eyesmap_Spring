package com.spring.eyesmap.domain.report.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.eyesmap.global.enumeration.Status;
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
        private Status status;
        private String location;

        public ReportListRequest(Sort sort){
            this.sort = sort;
        }
        public ReportListRequest(Status status){
            this.status = status;
        }
    }

    @Data
    public static class ReportListResponse{
        private String reportId;
        private Sort sort;
        private String contents;
        private Status status;
        private String title;
        private LocalDateTime reportDate;
        private String userId;
        private List<String> imageUrls;
    }

    @Getter
    @Builder
    public static class CreateReportResponse{
        private List<String> imageUrls;
    }
}
