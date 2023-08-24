package com.spring.eyesmap.domain.account.controller;

import com.spring.eyesmap.domain.account.service.LoginTestService;
import com.spring.eyesmap.global.exception.LoginFailedException;
import com.spring.eyesmap.global.response.BaseResponse;
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
public class LoginTestController {
    private final LoginTestService loginService;

    // redirect from kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code
    @GetMapping("/oauth2/kakao")
    public BaseResponse<Void> login(@RequestParam("code") String code, HttpSession httpSession) {
        // failed login(no code)
        if (code == null){
            throw new LoginFailedException();
        }

        // login process
        log.info("code = ", code);
        loginService.login(code, httpSession);

        return new BaseResponse<>();
    }

    @GetMapping("/logout")
    public BaseResponse<Void> logout(HttpSession httpSession){
        // get userinfo(another version)
        // @AuthenticationPrincipal AccountDetails accountDetails
        loginService.logout(httpSession);

        return new BaseResponse<>();
    }
}
