package com.spring.eyesmap.domain.report.service;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.image.domain.Image;
import com.spring.eyesmap.domain.image.dto.ImageDto;
import com.spring.eyesmap.domain.image.repository.ImageRepository;
import com.spring.eyesmap.domain.image.service.S3UploaderService;
import com.spring.eyesmap.domain.report.domain.Location;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.domain.ReportDeletion;
import com.spring.eyesmap.domain.report.dto.ReportDto;
import com.spring.eyesmap.domain.report.repository.LocationRepository;
import com.spring.eyesmap.domain.report.repository.ReportDeletionRepository;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.global.enumeration.DistrictNum;
import com.spring.eyesmap.global.enumeration.ImageSort;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import com.spring.eyesmap.global.exception.CustomException;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final AccountRepository accountRepository;
    private final ReportRepository reportRepository;
    private final S3UploaderService s3UploaderService;
    private final LocationRepository locationRepository;
    private final ImageRepository imageRepository;
    private final ReportDeletionRepository reportDeletionRepository;

    private final Integer REPORT_REQUEST_NUM = 3; //

    @Override
    public ReportDto.CreateReportResponse createReport(List<MultipartFile> multipartFiles, ReportDto.CreateReportRequest createReportRequest, ReportEnum.ReportedStatus reportedStatus, ImageSort imageSort) throws IOException {
        String dirNm = "report/"+reportedStatus + "/"+ createReportRequest.getSort()+"/"+createReportRequest.getDamagedStatus();
        System.out.println(dirNm);
        Location location;
        String address = createReportRequest.getAddress();
        if(reportedStatus == ReportEnum.ReportedStatus.DAMAGE){
             location = Location.builder() //중복 허용 불가
                    .address(address)
                    .gpsX(createReportRequest.getGpsX())
                    .gpsY(createReportRequest.getGpsX())
                    .build();
            locationRepository.save(location);
        }else{
            location = locationRepository.findByAddress(address).orElseThrow(() -> new CustomException());
        }
        System.out.println(createReportRequest.getAccountId());
        Account account = accountRepository.findByUserId(createReportRequest.getAccountId())
        .orElseThrow(
                () -> new CustomException() //로그인이랑 합치고 변경
        );


        Report report = Report.builder()
                        .contents(createReportRequest.getContents())
                        .damagedStatus(createReportRequest.getDamagedStatus())
                        .reportedStatus(reportedStatus)
                        .location(location)
                        .title(createReportRequest.getTitle())
                        .sort(createReportRequest.getSort())
                        .account(account)
                        .gu(getGu(address))
                        .damagedStatus(createReportRequest.getDamagedStatus())
                .build();

        reportRepository.save(report);
        List<String> imgUrls = new ArrayList<>();
        List<ImageDto.S3UploadResponse> imagesResponse = s3UploaderService.upload(multipartFiles, dirNm);
        List<Image> uploadedImage = imagesResponse.stream()
                .map(imageResponse -> {
                    imgUrls.add(imageResponse.getImgUrl());
                    return Image.builder()
                        .url(imageResponse.getImgUrl())
                            .imgNm(imageResponse.getImgFileNm())
                        .imageSort(imageSort)
                        .report(report)
                        .build();}).collect(Collectors.toList());
        imageRepository.saveAll(uploadedImage);

        return ReportDto.CreateReportResponse.builder()
                .location(location)
                .report(report)
                .imageUrls(imgUrls)
                .accountId(account.getUserId())
                .build();
    }
    private Integer getGu(String address){
        String pattern = "서울(?:시|특별시)? ?(\\S+)구\\b";

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(address);

        if (matcher.find()) {
            String gu = matcher.group(1);
            System.out.println(gu);
            return DistrictNum.nameOf(gu).getNum();
        }
        throw new CustomException();
    }

    @Override
    public ReportDto.ReportResponse getReport(String reportId){ // 상세
        Report report = reportRepository.findById(reportId).orElseThrow(()-> new CustomException());
        List<String> imageUrls = imageRepository.findAllByReportReportId(reportId).stream()
                .map((image) -> image.getUrl()).toList();

        return ReportDto.ReportResponse.builder()
                .report(report)
                .location(report.getLocation())
                .imageUrls(imageUrls)
                .build();
    }
//    @Override
//    public ReportDto.ReportListResponse getReportList(ReportDto.ReportListRequest reportListRequest){
//
//    }

    @Override
    @Transactional
    public void deleteReport(ReportDto.DeleteReportRequest deleteReportRequest){
        Report report = reportRepository.findById(deleteReportRequest.getReportId()).orElseThrow(() -> new CustomException());
        //ReportDeletionRepository 조합이 unique해야해
        report.updateDeleteRequestNum();
        String reportId = report.getReportId();
        reportDeletionRepository.save(new ReportDeletion(report, deleteReportRequest.getUserId()));

        if(report.getDeleteRequestNum() == REPORT_REQUEST_NUM){
            imageRepository.findAllByReportReportId(reportId).stream()
                    .map((image) -> {

                        try {
                            s3UploaderService.deleteFile(image.getImgNm());//s3에서 삭제
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        imageRepository.deleteById(image.getId());
                        return null;
                    })
                    .toList();
            reportDeletionRepository.deleteAllByReportReportId(reportId);//신고 삭제 요청 db 삭제
            reportRepository.deleteById(reportId);//신고 db에서 삭제
            locationRepository.deleteById(report.getLocation().getId());
        }
    }

}
