package com.spring.eyesmap.domain.report.service;

//import com.spring.eyesmap.domain.image.service.S3UploaderService;
import com.spring.eyesmap.domain.image.service.S3UploaderServiceImpl;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.global.exception.CustomException;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final AccountRepository accountRepository;
    private final ReportRepository reportRepository;
    private final S3UploaderServiceImpl s3UploaderService;

    public BaseResponse<Report> getReport(String reportId){ // 상세
        Report report = reportRepository.findById(reportId).orElseThrow(()-> new CustomException());
        return new BaseResponse<>(report);
    }

//    public BaseResponse<Report> getReportList(){
//
//    }

    @Override
    public void createReport(MultipartFile multipartFile, String dirNm) throws IOException {
        s3UploaderService.upload(multipartFile, dirNm);
    }

}
