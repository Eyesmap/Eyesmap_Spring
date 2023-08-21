package com.spring.eyesmap.domain.report.service;

import com.spring.eyesmap.domain.report.dto.ReportDto;
import com.spring.eyesmap.global.enumeration.ImageSort;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    ReportDto.CreateReportResponse createReport(List<MultipartFile> multipartFiles, ReportDto.CreateReportRequest createReportRequest, ReportEnum.ReportedStatus reportedStatus, ImageSort imageSort) throws IOException;
    ReportDto.CreateReportResponse createRestoreReport(List<MultipartFile> multipartFiles, ReportDto.CreateRestoreReportRequest createRestoreReportRequest, ReportEnum.ReportedStatus reportedStatus, ImageSort imageSort) throws IOException;
    ReportDto.ReportResponse  getReport(String reportId);
    List<ReportDto.ReportListResponse> getDamageReportList(ReportDto.ReportListRequest reportListRequest);
    void deleteReport(ReportDto.DeleteReportRequest deleteReportRequest);
    void createOrCancelReportDangeroutCnt(ReportDto.ReportDangerousCntRequest reportDangerousCntRequest);
}
