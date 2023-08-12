package com.spring.eyesmap.domain.report.service;

import com.spring.eyesmap.domain.image.service.S3UploaderService;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.dto.ReportDto;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.global.exception.CustomException;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final AccountRepository accountRepository;
    private final ReportRepository reportRepository;
    private final S3UploaderService s3UploaderService;

    public BaseResponse<Report> getReport(String reportId){ // 상세
        Report report = reportRepository.findById(reportId).orElseThrow(()-> new CustomException());
        return new BaseResponse<>(report);
    }

//    public BaseResponse<Report> getReportList(){
//
//    }

    @Override
    public ReportDto.CreateReportResponse createReport(List<MultipartFile> multipartFiles, String dirNm) throws IOException {
        return ReportDto.CreateReportResponse.builder()
                .imageUrls(s3UploaderService.upload(multipartFiles, dirNm))
                .build();
    }

}
