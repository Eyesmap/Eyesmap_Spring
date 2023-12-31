package com.spring.eyesmap.domain.account.service;

import com.spring.eyesmap.domain.account.domain.Account;
import com.spring.eyesmap.domain.account.dto.AccountDto;
import com.spring.eyesmap.domain.account.repository.AccountRepository;
import com.spring.eyesmap.global.enumeration.Role;
import com.spring.eyesmap.global.exception.NotFoundAccountException;
import com.spring.eyesmap.global.jwt.JwtTokenProvider;
import com.spring.eyesmap.global.oauth.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LoginService {

    private static final String BEARER_TYPE = "Bearer";
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final String imageName = "basicimage1.jpeg";

    @Value("${kakao.admin-key}")
    private String adminKey;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidityInMilliseconds;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional
    public AccountDto.LoginResponseDto loginWithToken(Long providerId) {
        Account account = accountRepository.findById(providerId).orElse(null);

        // signUp
        if (account == null){
            account = signUp(providerId);
        }

        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(account.getUserId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisTemplate.opsForValue().set("RT:"+account.getUserId(),refreshToken,refreshTokenValidityInMilliseconds, TimeUnit.MILLISECONDS);

        return new AccountDto.LoginResponseDto(accessToken, refreshToken);
    }

    private Map<String, Object> getUserAttributesByToken(Long providerId){
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me?target_id_type=user_id&target_id=" + providerId)
                .header("Authorization", "KakaoAK "+ adminKey)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }


    @Transactional
    private Account signUp(Long providerId){
//        if(!provider.equals("kakao")){
//            throw new IllegalArgumentException("잘못된 접근입니다.");
//        }
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(providerId);
        log.info("entry value: ");
        for (Map.Entry<String, Object> entrySet:
             userAttributesByToken.entrySet()) {
            log.info(entrySet.getKey() + ": " + entrySet.getValue());
        }
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(userAttributesByToken);
        Long kakao_id = kakaoUserInfo.getId();
        // String name = kakaoUserInfo.getName();
        String nickName = kakaoUserInfo.getNickName();

        Account signInAccount = Account.builder()
                .userId(kakao_id)
                .nickname(nickName)
                .role(Role.ROLE_USER)
                .profileImageUrl("https://" + bucket +
                        ".s3." +
                        region +
                        ".amazonaws.com/account/profile/image/" +
                        imageName)
                .imageName("account/profile/image/" + imageName)
                .voiceOnOff(Boolean.TRUE)
                .build();

        accountRepository.save(signInAccount);
        return signInAccount;
    }

    // get userinfo to use SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    @Transactional
    public void logout(String authorization){
        // 로그아웃 하고 싶은 토큰이 유효한 지 먼저 검증하기
        if (!jwtTokenProvider.validateToken(authorization)){
            throw new IllegalArgumentException("로그아웃 : 유효하지 않은 토큰입니다.");
        }
        // Access Token에서 User email을 가져온다
        Authentication authentication = jwtTokenProvider.getAuthentication(authorization);
        log.info("authentication= "+authentication.getName());

        // Redis에서 해당 User email로 저장된 Refresh Token 이 있는지 여부를 확인 후에 있을 경우 삭제를 한다.
        if (redisTemplate.opsForValue().get("RT:"+authentication.getName())!=null){
            // Refresh Token을 삭제
            redisTemplate.delete("RT:"+authentication.getName());
        }
        // 해당 Access Token 유효시간을 가지고 와서 BlackList에 저장하기
        Long expiration = jwtTokenProvider.getExpiration(authorization);
        redisTemplate.opsForValue().set(authorization,"logout",expiration,TimeUnit.MILLISECONDS);

    }
}
