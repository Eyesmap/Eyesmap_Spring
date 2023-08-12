package com.spring.eyesmap.domain.report.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ReportService {
    void createReport(MultipartFile multipartFile, String dirNm) throws IOException;
}
