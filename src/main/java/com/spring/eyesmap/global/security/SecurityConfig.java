package com.spring.eyesmap.global.security;

import com.spring.eyesmap.global.jwt.JwtAuthenticationFilter;
import com.spring.eyesmap.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Autowired
    private AccountDetailsService accountDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .httpBasic().disable() // rest api이므로 기본설정 미사용
                .csrf().disable()
                .formLogin().disable()
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/voice/**").hasRole("ADMIN")
                        .requestMatchers("/api/report/**").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
