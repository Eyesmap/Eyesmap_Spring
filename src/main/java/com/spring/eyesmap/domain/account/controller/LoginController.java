package com.spring.eyesmap.domain.account.controller;

import com.spring.eyesmap.domain.account.dto.LoginResponseDto;
import com.spring.eyesmap.domain.account.dto.LogoutResponseDto;
import com.spring.eyesmap.domain.account.service.LoginService;
import com.spring.eyesmap.domain.account.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login/oauth/{providerid}")
    public ResponseEntity<LoginResponseDto> login(@PathVariable("providerid") Long providerId) {
        // login process
        LoginResponseDto loginResponseDto = loginService.loginWithToken(providerId);
        return ResponseEntity.ok().body(loginResponseDto);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestHeader(value = "Authorization") String authorization){
        loginService.logout(authorization);
        return ResponseEntity.ok().body(new LogoutResponseDto(new ResponseDto("로그아웃 성공")));
    }
}
