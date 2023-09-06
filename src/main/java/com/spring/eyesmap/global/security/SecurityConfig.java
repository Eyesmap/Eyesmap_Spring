package com.spring.eyesmap.global.security;

import com.spring.eyesmap.global.jwt.JwtAuthenticationFilter;
import com.spring.eyesmap.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Autowired
    private AccountDetailsService accountDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .httpBasic().disable() // rest api이므로 기본설정 미사용
                .csrf().disable()
                .formLogin().disable()
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/account/**").authenticated()
                        .requestMatchers("/api/logout").authenticated()
                        .requestMatchers("/logout").authenticated()
                        .requestMatchers("/api/report/create/**").authenticated()
                        .requestMatchers("/api/report/delete").authenticated()
                        .requestMatchers("/api/report/dangerouscnt").authenticated()
                        .requestMatchers("/api/voice/onoff").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
