package com.spring.eyesmap.domain.account.service;

import com.spring.eyesmap.domain.account.repository.Account;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.global.enumeration.Role;
import com.spring.eyesmap.global.security.AccountDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    @Value("${kakao.oauth2.login.restapi.key}")
    private String apiKey;

    @Value("${kakao.oauth2.login.redirect_uri}")
    private String redirectUri;

    private AccountRepository accountRepository;

    public void login(String code) {
        // request accesstoken
        ResponseEntity<String> response = getAccessToken(code);

        // get accesstoken
        String tokenJson = response.getBody();
        JSONObject tokenJsonObject = new JSONObject(tokenJson);
        String accessToken = tokenJsonObject.getString("access_token");

        // request accountInfo
        ResponseEntity<String> accountInfo = getAccountInfo(accessToken);

        // get accountInfo
        String userInfoJson = response.getBody();
        JSONObject userInfoJsonObject = new JSONObject(tokenJson);
        String id = userInfoJsonObject.getString("id");
        String nickname = userInfoJsonObject.getString("nickname");

        // check duplication id in database
        Account kakaoAccount = accountRepository.findById(id).orElse(null);

        // if not duplicate, sign up
        if(kakaoAccount == null){
            kakaoAccount = Account.builder()
                    .id(id)
                    .nickname(nickname)
                    .role(Role.ROLE_USER)
                    .build();
            accountRepository.save(kakaoAccount);
        }

        // login
        AccountDetails accountDetails = new AccountDetails(kakaoAccount);
        Authentication authentication = new UsernamePasswordAuthenticationToken(accountDetails, null, accountDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // load login account
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Accounr principal = (Accounr) authentication.getPrincipal();
    }

    public ResponseEntity<String> getAccessToken(String code){
        // 1. header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        // 2. body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", apiKey);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        // 3. header + body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);

        // request http
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        log.info("accessTokenResponse = " + response);
        return response;
    }

    public ResponseEntity<String> getAccountInfo(String code){
        // 1. header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + code);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 2. put header
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);

        // request http
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        log.info("accountInfoByToken = " + response);
        return response;
    }
}
