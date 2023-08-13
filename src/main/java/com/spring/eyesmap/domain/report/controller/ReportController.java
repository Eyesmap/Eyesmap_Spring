package com.spring.eyesmap.domain.report.controller;

import com.spring.eyesmap.domain.report.dto.ReportDto;
import com.spring.eyesmap.domain.report.service.ReportService;
import com.spring.eyesmap.global.enumeration.ImageSort;
import com.spring.eyesmap.global.enumeration.ReportEnum;
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

    @PostMapping("/damage")
    public BaseResponse<ReportDto.CreateReportResponse> createReportDamaged(@RequestPart("images") List<MultipartFile> images, @RequestPart ReportDto.CreateReportRequest createReportRequest) throws IOException {
        final ReportEnum.ReportedStatus reportedStatusDamaged = ReportEnum.ReportedStatus.DAMAGE;
        final ImageSort imageSortDamaged = ImageSort.Damaged;
        return new BaseResponse<>(reportService.createReport(images, createReportRequest, reportedStatusDamaged, imageSortDamaged));
    }

    @PostMapping("/restoration")
    public BaseResponse<ReportDto.CreateReportResponse> createReportRestored(@RequestPart("images") List<MultipartFile> images, @RequestPart ReportDto.CreateReportRequest createReportRequest) throws IOException {
        ReportEnum.ReportedStatus reportedStatusRestored = ReportEnum.ReportedStatus.RESTORE;
        final ImageSort imageSortRestored = ImageSort.Restored;

        return new BaseResponse<>(reportService.createReport(images, createReportRequest, reportedStatusRestored, imageSortRestored));
    }
}
