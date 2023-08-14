package com.spring.eyesmap.domain.account.service;

import com.spring.eyesmap.domain.account.dto.LoginResponseDto;
import com.spring.eyesmap.domain.account.repository.Account;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.global.dto.ResponseDto;
import com.spring.eyesmap.global.enumeration.Role;
import com.spring.eyesmap.global.exception.NotFoundAccountException;
import com.spring.eyesmap.global.jwt.JwtTokenProvider;
import com.spring.eyesmap.global.oauth.KakaoUserInfo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LoginService {

    private static final String BEARER_TYPE = "Bearer";
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${kakao.admin-key}")
    private String adminKey;

    @Transactional
    public LoginResponseDto loginWithToken(Long providerId) {
        Account account = accountRepository.findById(providerId).orElse(null);

        // signUp
        if (account == null){
            account = signUp(providerId);
        }

        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(account.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        ResponseDto responseDto = new ResponseDto("로그인 성공");
        return new LoginResponseDto(responseDto, accessToken, refreshToken);
    }

    private Map<String, Object> getUserAttributesByToken(Long providerId){
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me?target_id_type=user_id?target_id=" + providerId)
                .header("Authorization", "KakaoAK "+ adminKey)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }


    private Account signUp(Long providerId){
//        if(!provider.equals("kakao")){
//            throw new IllegalArgumentException("잘못된 접근입니다.");
//        }
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(providerId);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(userAttributesByToken);
        Long kakao_id = kakaoUserInfo.getId();
        // String name = kakaoUserInfo.getName();
        String nickName = kakaoUserInfo.getNickName();

        Account signInAccount = Account.builder()
                .id(kakao_id)
                .nickname(nickName)
                .role(Role.ROLE_USER)
                .build();

        accountRepository.save(signInAccount);
        return signInAccount;
    }

    // get userInfo to kakao
    public Account getUserInfo(Long providerId){
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(providerId);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(userAttributesByToken);
        Long kakao_id = kakaoUserInfo.getId();
        // String name = kakaoUserInfo.getName();
        String nickName = kakaoUserInfo.getNickName();

        return accountRepository.findById(kakao_id).orElseThrow(
                () -> new NotFoundAccountException());
    }
}