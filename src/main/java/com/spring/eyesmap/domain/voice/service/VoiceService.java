package com.spring.eyesmap.domain.voice.service;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.domain.report.domain.Report;
import com.spring.eyesmap.domain.report.repository.ReportRepository;
import com.spring.eyesmap.domain.voice.dto.VoiceDto;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import com.spring.eyesmap.global.enumeration.VoiceOnOff;
import com.spring.eyesmap.global.exception.NotFoundAccountException;
import com.spring.eyesmap.global.exception.NotFoundEnumException;
import com.spring.eyesmap.global.exception.NotFoundReportException;
import com.spring.eyesmap.global.exception.VoiceOffException;
import com.spring.eyesmap.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoiceService {

    private final AccountRepository accountRepository;
    private final ReportRepository reportRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private String voiceFilePath = "/voice/";

    public VoiceDto.VoiceFileRequest fetchVoiceFile(String reportId) {
        // get user
        Long userId = SecurityUtil.getCurrentAccountId();
        log.info("userId= "+userId);
        if (userId != -1){ // 로그인 되어 있으면
            // 로그인 onoff 체크
            Account account = accountRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundAccountException());
            log.info("accountId= " + account.getUserId());
            // if voice off
            if (account.getVoiceOnOff() == VoiceOnOff.VOICE_OFF){
                throw new VoiceOffException();
            }
        }

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundReportException());

        if (report.getSort() == null){
            throw new NotFoundEnumException();
        }
        String reportSort = String.valueOf(report.getSort());
        String url = "https://" + bucket +
                ".s3." +
                region +
                ".amazonaws.com" +
                voiceFilePath +
                reportSort +
                ".mp3";

        VoiceDto.VoiceFileRequest voiceFileRequest = new VoiceDto.VoiceFileRequest(url);
        return voiceFileRequest;
    }

    @Transactional
    public void changeVoiceOnOff() {
        // get user
        Long userId = SecurityUtil.getCurrentAccountId();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundAccountException());
        log.info("accountId= " + account.getUserId());

        VoiceOnOff currentVoiceOnOff = account.getVoiceOnOff();

        if (currentVoiceOnOff == VoiceOnOff.VOICE_ON) {
            account.updateVoiceOnOff(VoiceOnOff.VOICE_OFF);
        }
        else{
            account.updateVoiceOnOff(VoiceOnOff.VOICE_ON);
        }
    }
}
