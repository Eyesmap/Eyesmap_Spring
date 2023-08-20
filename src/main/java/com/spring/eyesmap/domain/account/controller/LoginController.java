package com.spring.eyesmap.domain.account.controller;

import com.spring.eyesmap.domain.account.dto.AccountDto;
import com.spring.eyesmap.domain.account.service.LoginService;
import com.spring.eyesmap.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login/oauth/{providerid}")
    public BaseResponse<AccountDto.LoginResponseDto> login(@PathVariable("providerid") Long providerId) {
        // login process
        AccountDto.LoginResponseDto loginResponseDto = loginService.loginWithToken(providerId);
        return new BaseResponse<>(loginResponseDto);
    }

    @GetMapping("/api/logout")
    public BaseResponse<Void> logout(@RequestHeader(value = "Authorization") String authorization){
        loginService.logout(authorization);
        return new BaseResponse<>();
    }
}
