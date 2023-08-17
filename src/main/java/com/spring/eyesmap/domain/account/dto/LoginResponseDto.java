package com.spring.eyesmap.domain.account.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private ResponseDto responseDto;
    private String accessToken;
    private String refreshToken;

    public LoginResponseDto(ResponseDto responseDto, String accessToken, String refreshToken){
        this.responseDto = responseDto;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

    }
}
