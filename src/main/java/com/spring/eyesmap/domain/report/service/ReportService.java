package com.spring.eyesmap.domain.report.service;

import com.spring.eyesmap.domain.report.dto.ReportDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    ReportDto.CreateReportResponse createReport(List<MultipartFile> multipartFile, ReportDto.CreateReportRequest createReportRequest) throws IOException;
}
