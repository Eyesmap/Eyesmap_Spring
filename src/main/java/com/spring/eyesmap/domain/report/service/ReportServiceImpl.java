package com.spring.eyesmap.domain.report.service;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.image.domain.Image;
import com.spring.eyesmap.domain.image.repository.ImageRepository;
import com.spring.eyesmap.domain.image.service.S3UploaderService;
import com.spring.eyesmap.domain.report.domain.Location;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.dto.ReportDto;
import com.spring.eyesmap.domain.report.repository.LocationRepository;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.global.enumeration.ImageSort;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import com.spring.eyesmap.global.exception.CustomException;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final AccountRepository accountRepository;
    private final ReportRepository reportRepository;
    private final S3UploaderService s3UploaderService;
    private final LocationRepository locationRepository;
    private final ImageRepository imageRepository;
    public BaseResponse<Report> getReport(String reportId){ // 상세
        Report report = reportRepository.findById(reportId).orElseThrow(()-> new CustomException());
        return new BaseResponse<>(report);
    }

//    public BaseResponse<Report> getReportList(){
//
//    }

    @Override
    public ReportDto.CreateReportResponse createReport(List<MultipartFile> multipartFiles, ReportDto.CreateReportRequest createReportRequest, ReportEnum.ReportedStatus reportedStatus, ImageSort imageSort) throws IOException {
        String dirNm = "report/"+reportedStatus + "/"+ createReportRequest.getSort()+"/"+createReportRequest.getDamagedStatus();
        System.out.println(dirNm);

        Location location = Location.builder()
                .address(createReportRequest.getAddress())
                .gpsX(createReportRequest.getGpsX())
                .gpsY(createReportRequest.getGpsX())
                .build();
        locationRepository.save(location);
        System.out.println(createReportRequest.getAccountId());
        Account account = accountRepository.findById(createReportRequest.getAccountId()).get();
        //.orElseThrow(
//                () -> new CustomException()
//        );

        Report report = Report.builder()
                        .contents(createReportRequest.getContents())
                        .damagedStatus(createReportRequest.getDamagedStatus())
                        .reportedStatus(reportedStatus)
                        .location(location)
                        .title(createReportRequest.getTitle())
                        .sort(createReportRequest.getSort())
                        .account(account)
                        .damagedStatus(createReportRequest.getDamagedStatus())
                .build();

        reportRepository.save(report);

        List<String> imageUrls = s3UploaderService.upload(multipartFiles, dirNm);
        List<Image> uploadedImage = imageUrls.stream()
                .map(imageUrl -> Image.builder()
                        .url(imageUrl)
                        .imageSort(imageSort)
                        .report(report)
                        .build()).collect(Collectors.toList());
        imageRepository.saveAll(uploadedImage);

        return ReportDto.CreateReportResponse.builder()
                .location(location)
                .report(report)
                .imageUrls(imageUrls)
                .accountId(account.getId())
                .build();
    }

}
