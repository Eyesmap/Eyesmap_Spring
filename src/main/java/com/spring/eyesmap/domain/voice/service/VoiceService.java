package com.spring.eyesmap.domain.voice.service;

import com.spring.eyesmap.domain.voice.dto.VoiceDto;
import com.spring.eyesmap.global.enumeration.ReportEnum;
import com.spring.eyesmap.global.exception.NotFoundEnumException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VoiceService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private String voiceFilePath = "/voice/";

    public VoiceDto.VoiceFileRequest fetchVoiceFile(String reportEnumSort) {
        ReportEnum.Sort sortValue = ReportEnum.parsing(reportEnumSort);

        if (sortValue == null){
            throw new NotFoundEnumException();
        }
        String url = bucket +
                ".s3." +
                region +
                ".amazonaws.com" +
                voiceFilePath +
                reportEnumSort +
                ".mp3";

        VoiceDto.VoiceFileRequest voiceFileRequest = new VoiceDto.VoiceFileRequest(url);
        return voiceFileRequest;
    }
}
