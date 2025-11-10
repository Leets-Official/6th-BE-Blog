package com.leets.backend.blog.util; // <-- 패키지 경로를 blog.util로 지정

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {

    private SecurityUtil() {
    }

    // 현재 인증된 사용자의 ID(Long)를 Optional로 반환합니다.
    public static Optional<Long> getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            // UserDetails 인터페이스를 사용하는 경우 (일반적)
            UserDetails userDetails = (UserDetails) principal;
            // Spring Security는 기본적으로 username(PK)을 String으로 관리합니다.
            // 이 프로젝트에서는 ID(Long)를 username으로 사용한다고 가정합니다.
            try {
                return Optional.of(Long.parseLong(userDetails.getUsername()));
            } catch (NumberFormatException e) {
                // username이 Long 타입이 아닌 경우 (예: 이메일)
                return Optional.empty();
            }
        } else if (principal instanceof String) {
            // principal이 String인 경우 (예: "anonymousUser")
            if ("anonymousUser".equals(principal)) {
                return Optional.empty();
            }
            // 그 외 String principal (ID일 경우)
            try {
                return Optional.of(Long.parseLong((String) principal));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}