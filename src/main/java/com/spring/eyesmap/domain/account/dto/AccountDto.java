package com.spring.eyesmap.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AccountDto {


    @Data
    public static class LoginResponseDto {
        private String accessToken;
        private String refreshToken;

        public LoginResponseDto(String accessToken, String refreshToken){
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }


}
