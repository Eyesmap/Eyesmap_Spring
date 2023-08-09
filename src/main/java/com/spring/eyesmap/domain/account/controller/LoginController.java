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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;

    // redirect from kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code
    @GetMapping("/oauth2/kakao")
    public ResponseEntity<LoginResponseDto> login(@RequestParam("code") String code, HttpSession httpSession) {
        ResponseDto responseDto;

        // failed login(no code)
        if (code == null){
            throw new LoginFailedException();
        }

        // login process
        log.info("code = ", code);
        loginService.login(code, httpSession);

        responseDto = new ResponseDto("로그인 성공");
        return ResponseEntity.ok().body(new LoginResponseDto(responseDto));
    }

    @GetMapping("/api/logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpSession httpSession){
        // get userinfo(another version)
        // @AuthenticationPrincipal AccountDetails accountDetails
        ResponseDto responseDto;

        loginService.logout(httpSession);

        responseDto = new ResponseDto("로그아웃 성공");
        return ResponseEntity.ok().body(new LogoutResponseDto(responseDto));
    }
}
