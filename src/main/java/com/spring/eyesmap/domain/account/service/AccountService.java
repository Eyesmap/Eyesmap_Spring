package com.spring.eyesmap.domain.account.service;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.account.dto.AccountDto;
import com.spring.eyesmap.domain.account.dto.RankingList;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.domain.image.domain.Image;
import com.spring.eyesmap.domain.image.dto.ImageDto;
import com.spring.eyesmap.domain.image.repository.ImageRepository;
import com.spring.eyesmap.domain.image.service.S3UploaderService;
import com.spring.eyesmap.domain.report.domain.Location;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.domain.ReportDangerousCnt;
import com.spring.eyesmap.domain.report.repository.LocationRepository;
import com.spring.eyesmap.domain.report.repository.ReportDangerourCntRepository;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import com.spring.eyesmap.global.enumeration.VoiceOnOff;
import com.spring.eyesmap.global.exception.NotFoundAccountException;
import com.spring.eyesmap.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ReportRepository reportRepository;
    private final ImageRepository imageRepository;
    private final ReportDangerourCntRepository reportDangerourCntRepository;
    private final S3UploaderService s3UploaderService;
    private final String imageBasicUrl = "account/profile/image/";
    private final String medalImageBasicUrl = "ranking/medal/";
    private final String basicImageName = "basicimage.jpeg";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${kakao.admin-key}")
    private String adminKey;

    //    private String reportId; // report
//    private List<String> imageName; // image
//    private Double gpsX; // location
//    private Double gpsY; // location
//    private ReportEnum.Sort sort; // report
//    private ReportEnum.DamagedStatus damagedStatus; // report
//    private Integer dangerousCnt; // report
//    private String address; // location
//    private LocalDateTime reportDate; // report
//    private boolean dangerBtnClicked;


    @Transactional
    public AccountDto.ReportListResponseDto fetchReportList(AccountDto.FetchReportListRequestDto fetchReportListRequestDto) {
        // get user
        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundAccountException());
        log.info("accountId= "+ account.getUserId());
        // get report (writer)
        List<Report> reportList = reportRepository.findByAccountAndReportedStatus(account, ReportEnum.ReportedStatus.DAMAGE);
        log.info("reportListId= "+ reportList.get(0).getReportId());

        List<AccountDto.MyPageList> responseReportLists = new ArrayList<>();
        for (Report report:
                reportList) {
            List<Image> imageList = imageRepository.findByReport(report);
            List<String> imageUrlList = new ArrayList<>();
            for (Image image:
                    imageList) {
                imageUrlList.add(image.getUrl());
            }
            double distance = distance(fetchReportListRequestDto.getUserGpsY(), fetchReportListRequestDto.getUserGpsX(), report.getLocation().getGpsY(), report.getLocation().getGpsX());
            boolean isDangerBtnClicked = userId!=null &&
                    reportDangerourCntRepository.existsByReportReportIdAndUserId(report.getReportId(), userId)
                    ?true:false;
            log.info("imageListId= "+ imageList.get(0).getId());
            responseReportLists.add(new AccountDto.MyPageList(report.getReportId(), imageUrlList,
                    report.getLocation().getGpsX(), report.getLocation().getGpsY(),
                    report.getSort(), report.getDamagedStatus(),
                    report.getReportDangerousNum(), report.getLocation().getAddress(),
                    report.getReportDate(), isDangerBtnClicked, distance, report.getTitle()));
        }

        return AccountDto.ReportListResponseDto.builder()
                .reportList(responseReportLists)
                .build();
    }

    @Transactional
    public AccountDto.DangerousCntListResponseDto fetchDangerousCntList(AccountDto.FetchDangerousCntListRequestDto fetchDangerousCntListRequestDto) {
        // get user
        Long userId = null;
        userId = SecurityUtil.getCurrentAccountId();
        if(userId == null){
            throw new NotFoundAccountException();
        }
        // get report (thumps up)
        List<ReportDangerousCnt> reportList = reportDangerourCntRepository.findByUserId(userId);
        log.info("reportListId= "+ reportList.get(0).getReport().getReportId());

        List<AccountDto.MyPageList> responseReportLists = new ArrayList<>();
        for (ReportDangerousCnt dangerousCnt:
                reportList) {
            List<Image> imageList = imageRepository.findByReport(dangerousCnt.getReport());
            List<String> imageUrlList = new ArrayList<>();
            for (Image image:
                    imageList) {
                imageUrlList.add(image.getUrl());
            }
            double distance = distance(fetchDangerousCntListRequestDto.getUserGpsY(), fetchDangerousCntListRequestDto.getUserGpsX(), dangerousCnt.getReport().getLocation().getGpsY(), dangerousCnt.getReport().getLocation().getGpsX());

            boolean isDangerBtnClicked = userId!=null &&
                    reportDangerourCntRepository.existsByReportReportIdAndUserId(dangerousCnt.getReport().getReportId(), userId)
                    ?true:false;

            log.info("imageListId= "+ imageList.get(0).getId());
            responseReportLists.add(new AccountDto.MyPageList(dangerousCnt.getReport().getReportId(), imageUrlList,
                    dangerousCnt.getReport().getLocation().getGpsX(), dangerousCnt.getReport().getLocation().getGpsY(),
                    dangerousCnt.getReport().getSort(), dangerousCnt.getReport().getDamagedStatus(),
                    dangerousCnt.getReport().getReportDangerousNum(), dangerousCnt.getReport().getLocation().getAddress(),
                    dangerousCnt.getReport().getReportDate(), isDangerBtnClicked, distance, dangerousCnt.getReport().getTitle()));
        }
        return AccountDto.DangerousCntListResponseDto.builder()
                .reportList(responseReportLists)
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



    @Transactional
    public AccountDto.RankingResponseDto fetchRankingList() {
        Integer rank = 1;
        Long previousValue = null;

        List<RankingList> rankingList = accountRepository.findTop10Ranking();
        List<AccountDto.RankingListTop3> rankingListTop3 = new ArrayList<>();
        List<AccountDto.OtherRankingList> otherRankingList = new ArrayList<>();
        for (RankingList r:
                rankingList) {

            if (rank <= 3){
                String medalImageUrl = "https://" + bucket +
                        ".s3." +
                        region +
                        ".amazonaws.com/" +
                        medalImageBasicUrl;
                if(rank == 1){
                    medalImageUrl += "gold.png";
                }
                else if(rank == 2) {
                    medalImageUrl += "silver.png";
                }
                else{
                    medalImageUrl += "bronze.png";
                }
                rankingListTop3.add(new AccountDto.RankingListTop3(rank, r.getUserId(), r.getNickname(), r.getProfileImageUrl(), r.getReportCnt(), medalImageUrl));
            }
            else{
                otherRankingList.add(new AccountDto.OtherRankingList(rank, r.getUserId(), r.getNickname(), r.getProfileImageUrl(), r.getReportCnt()));
            }

            rank+=1;
        }
        return AccountDto.RankingResponseDto.builder()
                .rankingListTop3(rankingListTop3)
                .otherRankingList(otherRankingList)
                .build();
    }

    @Transactional
    public void updateProfileImage(MultipartFile image, AccountDto.UpdateProfileImageReqeuestDto updateProfileImageReqeuestDto) throws IOException {
        // get user
        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundAccountException());
        log.info("accountId= "+ account.getUserId());

        log.info("basicUrlName= "+imageBasicUrl + basicImageName);

        // not basicImage
        if (!account.getImageName().equals(imageBasicUrl + basicImageName)){
            log.info("before deleteFile= "+ account.getImageName());
            // delete old image in s3
            s3UploaderService.deleteFile(account.getImageName());
            log.info("after deleteFile");
        }

        // store new image in s3
        List<MultipartFile> imageList = new ArrayList<>();
        imageList.add(image);
        List<ImageDto.S3UploadResponse> imagesResponse = s3UploaderService.upload(imageList, imageBasicUrl + userId.toString());

        account.updateNickname(updateProfileImageReqeuestDto.getNickname());
        account.updateImage(imagesResponse.get(0).getImgUrl(), imagesResponse.get(0).getImgFileNm());
        accountRepository.save(account);
    }

    @Transactional
    public void initProfileImage() throws IOException {
        // get user
        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundAccountException());
        log.info("accountId= "+ account.getUserId());

        if (!account.getProfileImageUrl().equals("https://" + bucket +
                ".s3." +
                region +
                ".amazonaws.com/" +
                imageBasicUrl +
                basicImageName)){
            s3UploaderService.deleteFile(account.getImageName());
        }
        // delete old image
        // update basic Image
        account.updateImage("https://" + bucket +
                ".s3." +
                region +
                ".amazonaws.com/" +
                imageBasicUrl +
                basicImageName, imageBasicUrl + basicImageName);
    }

    @Transactional
    public AccountDto.FetchAccountResponseDto fetchAccount() {
        // get user
        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundAccountException());
        log.info("accountId= " + account.getUserId());

        boolean onOffBtn;
        if(account.getVoiceOnOff().equals(VoiceOnOff.VOICE_ON)){
            onOffBtn = true;
        }else{
            onOffBtn = false;
        }

        AccountDto.FetchAccountResponseDto fetchAccountResponseDto = new AccountDto.FetchAccountResponseDto(
                account.getNickname(),
                account.getProfileImageUrl(),
                account.getImageName(),
                onOffBtn);

        return fetchAccountResponseDto;
    }

    @Transactional
    public void fetchAllAccount() {
        // 1. header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "KakaoAK "+ adminKey);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 2. put header
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);

        // request http
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/ids",
                HttpMethod.GET,
                httpEntity,
                String.class
        );
        log.info("all account info= " + response);
    }

    @Transactional
    public AccountDto.MyReportResponseDto fetchMyReport() {
        // get user
        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundAccountException());
        log.info("accountId= " + account.getUserId());

        List<Report> reportList = reportRepository.findByAccount(account);

        AccountDto.MyReportResponseDto myReportResponseDto = new AccountDto.MyReportResponseDto(account.getNickname(), account.getProfileImageUrl(), reportList.size());
        return myReportResponseDto;
    }
}

