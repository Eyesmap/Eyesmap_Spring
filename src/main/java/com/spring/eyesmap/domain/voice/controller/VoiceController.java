package com.spring.eyesmap.domain.voice.controller;

import com.spring.eyesmap.domain.voice.dto.VoiceDto;
import com.spring.eyesmap.domain.voice.service.VoiceService;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class VoiceController {

    private final VoiceService voiceService;

    @GetMapping("/api/voice/file/{reportsort}")
    public BaseResponse<VoiceDto.VoiceFileRequest> fetchVoiceFile(@PathVariable("reportsort") String reportEnumSort){
        VoiceDto.VoiceFileRequest voiceFileRequest = voiceService.fetchVoiceFile(reportEnumSort);
        return new BaseResponse<>(voiceFileRequest);
    }
}
