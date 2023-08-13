//package com.spring.eyesmap.global.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@EnableWebSecurity
//@Configuration
//public class SecurityConfig {
//
//    @Autowired
//    private AccountDetailsService accountDetailsService;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        return http.csrf().disable()
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/api/voice/**").hasRole("ADMIN")
//                        .requestMatchers("/api/report/**").authenticated()
//                        .anyRequest().permitAll())
//                .build();
//    }
//
//}
