package com.spring.eyesmap.domain.voice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class VoiceDto {

    @Data
    @AllArgsConstructor
    public static class VoiceFileRequest {
        private String url;
    }
}
