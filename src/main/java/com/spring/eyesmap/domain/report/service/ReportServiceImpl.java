package com.spring.eyesmap.domain.report.service;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.image.domain.Image;
import com.spring.eyesmap.domain.image.dto.ImageDto;
import com.spring.eyesmap.domain.image.repository.ImageRepository;
import com.spring.eyesmap.domain.image.service.S3UploaderService;
import com.spring.eyesmap.domain.report.domain.Location;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.domain.ReportDangerousCnt;
import com.spring.eyesmap.domain.report.domain.ReportDeletion;
import com.spring.eyesmap.domain.report.dto.ReportDto;
import com.spring.eyesmap.domain.report.repository.LocationRepository;
import com.spring.eyesmap.domain.report.repository.ReportDangerourCntRepository;
import com.spring.eyesmap.domain.report.repository.ReportDeletionRepository;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.global.enumeration.DistrictNum;
import com.spring.eyesmap.global.enumeration.ImageSort;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import com.spring.eyesmap.global.exception.*;
import com.spring.eyesmap.global.security.SecurityUtil;
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
    private final ReportDangerourCntRepository reportDangerourCntRepository;

    private final Integer REPORT_REQUEST_NUM = 3; //

    @Override
    public ReportDto.CreateReportResponse createReport(List<MultipartFile> multipartFiles, ReportDto.CreateReportRequest createReportRequest, ReportEnum.ReportedStatus reportedStatus, ImageSort imageSort) throws IOException {
        String dirNm = "report/"+reportedStatus + "/"+ createReportRequest.getSort()+"/"+createReportRequest.getDamagedStatus();
        System.out.println(dirNm);
        Location location;
        Double gpsX = createReportRequest.getGpsX();
        Double gpsY = createReportRequest.getGpsY();
        String address = createReportRequest.getAddress();
        if (locationRepository.existsByGpsXAndGpsY(gpsX, gpsY)) {
            throw new AlreadyReportException();
        }

        location = Location.builder() //중복 허용 불가
                    .address(address)
                    .gpsX(gpsX)
                    .gpsY(gpsY)
                    .build();
            locationRepository.save(location);

        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findByUserId(userId)
        .orElseThrow(
                () -> new NotFoundAccountException()
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

        return saveReport(multipartFiles, report, location, account, dirNm, imageSort);
    }
    @Override
    public ReportDto.CreateReportResponse createRestoreReport(List<MultipartFile> multipartFiles, ReportDto.CreateRestoreReportRequest createRestoreReportRequest, ReportEnum.ReportedStatus reportedStatus, ImageSort imageSort) throws IOException {
        Report report = reportRepository.findById(createRestoreReportRequest.getReportId()).orElseThrow(() -> new NotFoundReportException());
        String dirNm = "report/" + reportedStatus + "/" + report.getSort() + "/" + report.getDamagedStatus();
        System.out.println(dirNm);

        Location location = report.getLocation();
        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new NotFoundAccountException()
                );
        Report restoredReport = Report.builder()
                .contents(report.getContents())
                .damagedStatus(report.getDamagedStatus())
                .reportedStatus(reportedStatus)
                .location(location)
                .title(report.getTitle())
                .sort(report.getSort())
                .account(account)
                .gu(getGu(location.getAddress()))
                .damagedStatus(report.getDamagedStatus())
                .build();

        return saveReport(multipartFiles, restoredReport, location, account, dirNm, imageSort);

    }

    private ReportDto.CreateReportResponse saveReport(List<MultipartFile> multipartFiles, Report report, Location location,
                                                     Account account, String dirNm, ImageSort imageSort) throws IOException {

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
        throw new NotFoundLocationException();
    }

    @Override
    public ReportDto.ReportMarkResponse getMarkedReport(ReportDto.ReportMarkRequest reportMarkRequest){ // 마크 표시된 신고 하나 조회
        Report report = reportRepository.findById(reportMarkRequest.getReportId()).orElseThrow(()-> new NotFoundReportException());
        List<String> imageUrls = imageRepository.findAllByReportReportId(reportMarkRequest.getReportId()).stream()
                .map((image) -> image.getUrl()).toList();

        //위도(y) 경도(x)로 거리계산
        double distance = distance(reportMarkRequest.getUserGpsY(), reportMarkRequest.getUserGpsX(), report.getLocation().getGpsY(), report.getLocation().getGpsX());
        System.out.println(distance);
        return ReportDto.ReportMarkResponse.builder()
                .report(report)
                .imageUrls(imageUrls)
                .distance(distance)
                .build();
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
    //lat: 위도, lon: 경도
        double theta = lon1 - lon2;
        double dist = Math.sin(degToRad(lat1)) * Math.sin(degToRad(lat2)) + Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) * Math.cos(degToRad(theta));

        dist = Math.acos(dist);
        dist = radToDeg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;

        return dist;
    }

    private static double degToRad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double radToDeg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Override
    public ReportDto.ReportResponse getDetailReport(String reportId){ // 상세
        Report report = reportRepository.findById(reportId).orElseThrow(()-> new NotFoundReportException());

        return ReportDto.ReportResponse.builder()
                .report(report)
                .location(report.getLocation())
                .build();
    }
    @Override
    public List<ReportDto.ReportListResponse> getDamageReportList(){
        final ReportEnum.ReportedStatus reportedStatus = ReportEnum.ReportedStatus.DAMAGE;

        List<ReportDto.ReportListResponse> reportListResponses = reportRepository.findAllByReportedStatus(reportedStatus).stream().map
                (report -> new ReportDto.ReportListResponse(report)).collect(Collectors.toList());
        return reportListResponses;
    }

    @Override
    @Transactional
    public void deleteReport(ReportDto.DeleteReportRequest deleteReportRequest){

        Report report = reportRepository.findById(deleteReportRequest.getReportId()).orElseThrow(() -> new NotFoundReportException());
        Long userId = SecurityUtil.getCurrentAccountId();
        if(!accountRepository.existsById(userId)){
            throw new NotFoundAccountException();
        }
        if(reportDeletionRepository.existsByReportReportIdAndUserId(report.getReportId(), userId)){
            throw new AlreadyDeletionReportException();
        }
        //ReportDeletionRepository 조합이 unique해야해
        report.updateDeleteRequestNum();
        String reportId = report.getReportId();
        reportDeletionRepository.save(new ReportDeletion(report, userId));

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
    @Override
    @Transactional
    public void createOrCancelReportDangeroutCnt(ReportDto.ReportDangerousCntRequest reportDangerousCntRequest){
        Long userId = SecurityUtil.getCurrentAccountId();

        if(!accountRepository.existsById(userId)){
            throw new NotFoundAccountException();
        }
        if (reportDangerourCntRepository.existsByReportReportIdAndUserId(reportDangerousCntRequest.getReportId(), userId)) {
            Report report = reportRepository.findById(reportDangerousCntRequest.getReportId())
                    .orElseThrow(()->new NotFoundReportException());
            ReportDangerousCnt reportDangerousCnt = reportDangerourCntRepository.findByReportReportIdAndUserId(reportDangerousCntRequest.getReportId(),userId)
                    .orElseThrow(() -> new NotFoundDangerousCntException());

            report.updateReportDangerousNum(report.getReportDangerousNum() - 1);
            reportDangerourCntRepository.delete(reportDangerousCnt);
        }else {
            Report report = reportRepository.findById(reportDangerousCntRequest.getReportId())
                    .orElseThrow(() -> new NotFoundReportException());
            ReportDangerousCnt reportDangerousCnt = new ReportDangerousCnt(report, userId);
            reportDangerourCntRepository.save(reportDangerousCnt);
            report.updateReportDangerousNum(report.getReportDangerousNum() + 1);
        }
    }

    //관리자 -> 신고 복구 들어오면 삭제(해당 복구 신고 들어온 거 다 삭제)

}
