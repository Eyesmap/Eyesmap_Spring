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
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/create/damage")
    public BaseResponse<ReportDto.CreateReportResponse> createReportDamaged(@RequestPart("images") List<MultipartFile> images, @RequestPart ReportDto.CreateReportRequest createReportRequest) throws IOException {
        final ReportEnum.ReportedStatus reportedStatusDamaged = ReportEnum.ReportedStatus.DAMAGE;
        final ImageSort imageSortDamaged = ImageSort.DAMAGED;
        return new BaseResponse<>(reportService.createReport(images, createReportRequest, reportedStatusDamaged, imageSortDamaged));
    }

    @PostMapping("/create/restoration")
    public BaseResponse<ReportDto.CreateReportResponse> createReportRestored(@RequestPart("images") List<MultipartFile> images, @RequestPart ReportDto.CreateRestoreReportRequest createRestoreReportRequest) throws IOException {
        ReportEnum.ReportedStatus reportedStatusRestored = ReportEnum.ReportedStatus.RESTORE;
        final ImageSort imageSortRestored = ImageSort.RESTORED;

        return new BaseResponse<>(reportService.createRestoreReport(images, createRestoreReportRequest, reportedStatusRestored, imageSortRestored));
    }
    @GetMapping("/fetch")//목록 조회
    public BaseResponse<List<ReportDto.ReportListResponse>> getReportList() {
        return new BaseResponse<>(reportService.getDamageReportList());
    }
    @PostMapping("/fetch/mark") //선택한 마크 표시된 신고 조회
    public BaseResponse<ReportDto.ReportMarkResponse> getMarkedReport(@RequestBody ReportDto.ReportMarkRequest reportMarkRequest) {
        return new BaseResponse<>(reportService.getMarkedReport(reportMarkRequest));
    }
    @GetMapping("/fetch/detail")//상세 조회
    public BaseResponse<ReportDto.ReportResponse> getReport(@RequestParam String reportId) {

        return new BaseResponse<>(reportService.getDetailReport(reportId));
    }

    @DeleteMapping("/delete")
    public BaseResponse<Void> deleteReport(@RequestBody ReportDto.DeleteReportRequest deleteReportRequest){
        reportService.deleteReport(deleteReportRequest);
        return new BaseResponse<>();
    }

    @PostMapping("/dangerouscnt")
    public BaseResponse<Void> createOrCancelReportDangeroutCnt(@RequestBody ReportDto.ReportDangerousCntRequest reportDangerousCntRequest){
        reportService.createOrCancelReportDangeroutCnt(reportDangerousCntRequest);
        return new BaseResponse<>();
    }
}
