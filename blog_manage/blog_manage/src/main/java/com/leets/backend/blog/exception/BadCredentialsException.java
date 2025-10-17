package com.leets.backend.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // HTTP 401 Unauthorized
public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    public BadCredentialsException(String message) {
        super(message);
    }
}
