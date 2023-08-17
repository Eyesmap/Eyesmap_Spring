package com.spring.eyesmap.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    // TokenProvider 를 주입받은 JwtFilter 를 Security Filter 앞에 추가
    @Override
    public void configure(HttpSecurity http) {
        JwtAuthenticationFilter customJwtFilter = new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate);
        http.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
