package com.leets.backend.blog.exception;

// HTTP 401 Unauthorized 용 (로그인 시 자격 증명 실패)
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}