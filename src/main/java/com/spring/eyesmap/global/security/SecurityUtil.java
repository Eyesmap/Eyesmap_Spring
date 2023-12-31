package com.spring.eyesmap.global.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;

public class SecurityUtil {
    private SecurityUtil() {}

    // JwtCustomFilter 에서 SecurityContext 에 저장한 유저 정보 꺼냄
    public static Long getCurrentAccountId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }
        if (authentication.getName() == "anonymousUser") {
            return Long.parseLong(String.valueOf(-1));
        }
        return Long.parseLong(authentication.getName());
    }
    public static Long getCurrentAccountIdOrNotNull() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName().contentEquals("anonymousUser")) {
            return null;
        }

        return Long.parseLong(authentication.getName());
    }
}
