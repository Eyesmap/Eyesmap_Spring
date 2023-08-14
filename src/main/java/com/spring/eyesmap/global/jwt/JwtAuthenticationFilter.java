package com.spring.eyesmap.global.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        log.info("[This is Verifying token!!]"); // 그냥 스트링 출력해주는 로
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // Redis에 해당 accessToken logout 여부를 확인
            String isLogout = (String) redisTemplate.opsForValue().get(token);
            log.info("isLogout= "+isLogout);
            // 로그아웃이 없는(되어 있지 않은) 경우 해당 토큰은 정상적으로 작동하기
            if (ObjectUtils.isEmpty(isLogout)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

}