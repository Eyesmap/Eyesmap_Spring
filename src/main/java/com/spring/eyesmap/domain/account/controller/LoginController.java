package com.spring.eyesmap.domain.account.controller;

import com.spring.eyesmap.domain.account.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String login(@RequestParam("code") String code) {
        // login process
        log.info("code = ", code);
        loginService.login(code);
        return "Success";
    }

}
