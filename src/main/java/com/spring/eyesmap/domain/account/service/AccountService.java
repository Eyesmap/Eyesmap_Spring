package com.spring.eyesmap.domain.account.service;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.account.dto.AccountDto;
import com.spring.eyesmap.domain.account.dto.RankingList;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.domain.image.domain.Image;
import com.spring.eyesmap.domain.image.dto.ImageDto;
import com.spring.eyesmap.domain.image.repository.ImageRepository;
import com.spring.eyesmap.domain.image.service.S3UploaderService;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.domain.ReportDangerousCnt;
import com.spring.eyesmap.domain.report.repository.ReportDangerourCntRepository;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import com.spring.eyesmap.global.exception.NotFoundAccountException;
import com.spring.eyesmap.global.exception.NotFoundDangerousCntException;
import com.spring.eyesmap.global.exception.NotFoundReportException;
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

    @Transactional
    public AccountDto.ReportListResponseDto fetchReportList() {
        // get user
        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundAccountException());
        log.info("accountId= "+ account.getUserId());
        // get report (writer)
        List<Report> reportList = reportRepository.findByAccount(account);
        if (reportList.isEmpty()){
            throw new NotFoundReportException();
        }
        log.info("reportListId= "+ reportList.get(0).getReportId());

        List<AccountDto.MyPageList> responseReportLists = new ArrayList<>();
        for (Report report:
             reportList) {
            List<Image> imageList = imageRepository.findByReport(report);
            log.info("imageListId= "+ imageList.get(0).getId());
            responseReportLists.add(new AccountDto.MyPageList(report.getReportId(), imageList.get(0).getUrl()));
        }

        return AccountDto.ReportListResponseDto.builder()
                .reportList(responseReportLists)
                .build();
    }

    @Transactional
    public AccountDto.DangerousCntListResponseDto fetchDangerousCntList() {
        // get user
        Long userId = null;
        userId = SecurityUtil.getCurrentAccountId();
        if(userId == null){
            throw new NotFoundAccountException();
        }
        // get report (thumps up)
        List<ReportDangerousCnt> reportList = reportDangerourCntRepository.findByUserId(userId);
        if (reportList.isEmpty()){
            throw new NotFoundDangerousCntException();
        }
        log.info("reportListId= "+ reportList.get(0).getReport().getReportId());

        List<AccountDto.MyPageList> responseReportLists = new ArrayList<>();
        for (ReportDangerousCnt report:
                reportList) {
            List<Image> imageList = imageRepository.findByReport(report.getReport());
            log.info("imageListId= "+ imageList.get(0).getId());
            responseReportLists.add(new AccountDto.MyPageList(report.getReport().getReportId(), imageList.get(0).getUrl()));
        }
        return AccountDto.DangerousCntListResponseDto.builder()
                .reportList(responseReportLists)
                .build();
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
                String medalImageUrl = "https://" +bucket +
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
    public void updateProfileImage(MultipartFile image) throws IOException {
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

        // delete old image
        s3UploaderService.deleteFile(account.getImageName());
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

        AccountDto.FetchAccountResponseDto fetchAccountResponseDto = new AccountDto.FetchAccountResponseDto(
                account.getNickname(),
                account.getProfileImageUrl(),
                account.getImageName());

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
}
