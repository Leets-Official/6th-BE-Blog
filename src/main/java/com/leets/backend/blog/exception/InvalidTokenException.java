package com.leets.backend.blog.exception;

// HTTP 401 Unauthorized 용 (토큰 재발급 시 토큰 유효하지 않음)
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}