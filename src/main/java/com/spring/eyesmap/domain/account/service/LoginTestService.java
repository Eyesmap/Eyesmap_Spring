package com.spring.eyesmap.domain.account.service;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.global.enumeration.Role;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginTestService {
    @Value("${kakao.oauth2.login.restapi.key}")
    private String apiKey;

    @Value("${kakao.oauth2.login.redirect_uri}")
    private String redirectUri;

    private final AccountRepository accountRepository;

    public void login(String code, HttpSession httpSession) {
        // request accesstoken
        ResponseEntity<String> response = getAccessToken(code);

        // get accesstoken
        String tokenJson = response.getBody();
        JSONObject tokenJsonObject = new JSONObject(tokenJson);
        String accessToken = tokenJsonObject.getString("access_token");
        log.info("-3. accessToken = " + accessToken);
        // request accountInfo
        ResponseEntity<String> accountInfo = getAccountInfo(accessToken);

        // get accountInfo
        String accountJson = accountInfo.getBody();
        JSONObject accountInfoJsonObject = new JSONObject(accountJson);
        log.info("-2. accountInfoJsonObject = " + accountInfoJsonObject);
        Long id = accountInfoJsonObject.getLong("id");
        String nickname = accountInfoJsonObject.getJSONObject("properties").getString("nickname");
        log.info("-1. id= "+ id+ nickname);

        // check duplication id in database
        Account kakaoAccount = accountRepository.findById(id).orElse(null);
        log.info("0. kakaoAccount= "+ kakaoAccount);
        // if not duplicate, sign up
        if(kakaoAccount == null){
            kakaoAccount = Account.builder()
                    .userId(id)
                    .nickname(nickname)
                    .role(Role.ROLE_USER)
                    .build();
            accountRepository.save(kakaoAccount);
        }

        // store token in session for logout
        httpSession.setAttribute("access_token", accessToken);

        // login
//        AccountDetails accountDetails = new AccountDetails(kakaoAccount);
//        log.info("1. accountDetails= "+ accountDetails.getAccount().getNickname());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(accountDetails, null, accountDetails.getAuthorities());
//        log.info("2. authentication= "+ authentication.getPrincipal());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        AccountDetails z = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("3. nickname= "+z.getAccount().getNickname());
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

    public ResponseEntity<String> getAccountInfo(String accessToken){
        // 1. header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 2. put header
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);

        // request http
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        log.info("accountInfoByToken = " + response);
        return response;
    }

    public void logout(HttpSession httpSession) {

        // for test
//        AccountDetails accountDetails = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        Account account = accountDetails.getAccount();
//        log.info("logout nickname= "+account.getNickname());


        // 1. header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + httpSession.getAttribute("access_token"));

        // 2. put header
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);

        // request http
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        log.info("logoutAccountInfo = " + response);

        // remove access_token and info in SecurityContext
        httpSession.invalidate();
        // SecurityContextHolder.clearContext();
    }
}
