package com.spring.eyesmap.domain.report.controller;

import com.spring.eyesmap.domain.report.dto.ReportDto;
import com.spring.eyesmap.domain.report.service.ReportService;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/create")
    public BaseResponse<ReportDto.CreateReportResponse> createReport(@RequestPart("images") List<MultipartFile> images, @RequestPart ReportDto.CreateReportRequest createReportRequest) throws IOException {
        return new BaseResponse<>(reportService.createReport(images, createReportRequest));
    }
}
