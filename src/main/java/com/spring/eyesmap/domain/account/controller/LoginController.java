package com.spring.eyesmap.domain.account.controller;

import com.spring.eyesmap.domain.account.dto.LoginResponseDto;
import com.spring.eyesmap.domain.account.dto.LogoutResponseDto;
import com.spring.eyesmap.domain.account.service.LoginService;
import com.spring.eyesmap.global.dto.ResponseDto;
import com.spring.eyesmap.global.exception.LoginFailedException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
