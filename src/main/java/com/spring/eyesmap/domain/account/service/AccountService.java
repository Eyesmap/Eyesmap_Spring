package com.spring.eyesmap.domain.account.service;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.account.dto.AccountDto;
import com.spring.eyesmap.domain.account.dto.RankingList;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.domain.image.domain.Image;
import com.spring.eyesmap.domain.image.repository.ImageRepository;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.domain.ReportDangerousCnt;
import com.spring.eyesmap.domain.report.repository.ReportDangerourCntRepository;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import com.spring.eyesmap.global.exception.NotFoundAccountException;
import com.spring.eyesmap.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ReportRepository reportRepository;
    private final ImageRepository imageRepository;
    private final ReportDangerourCntRepository reportDangerourCntRepository;

    @Transactional
    public AccountDto.ReportListResponseDto fetchReportList() {
        // get user
        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundAccountException());
        log.info("accountId= "+ account.getUserId());
        // get report (writer)
        List<Report> reportList = reportRepository.findByAccount(account);
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
        List<RankingList> rankingList = accountRepository.findTop10Ranking();

        return AccountDto.RankingResponseDto.builder()
                .rankingList(rankingList)
                .build();
    }
}
