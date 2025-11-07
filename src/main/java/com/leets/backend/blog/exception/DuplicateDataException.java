package com.leets.backend.blog.exception;

// HTTP 400 Bad Request 용 (회원가입 시 이메일/닉네임 중복)
public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException(String message) {
        super(message);
    }
}