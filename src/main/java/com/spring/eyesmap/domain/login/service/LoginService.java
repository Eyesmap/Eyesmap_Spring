package com.spring.eyesmap.domain.login.service;

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
public class LoginService {

    @Value("${kakao.oauth2.login.restapi.key}")
    private String apiKey;

    @Value("${kakao.oauth2.login.redirect_uri}")
    private String redirectUri;

    public void login(String code) {
        // request accesstoken
        ResponseEntity<String> response = getAccessToken(code);

        // get accesstoken
        String tokenJson = response.getBody();
        JSONObject rjson = new JSONObject(tokenJson);
        String accessToken = rjson.getString("access_token");

        // get accountInfo
        ResponseEntity<String> accountInfo = getAccountInfo(accessToken);

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
        log.info("response = " + response);
        return response;
    }

    public ResponseEntity<String> getAccountInfo(String code){
        return null;
    }
}
