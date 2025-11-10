package com.leets.backend.blog.exception;

// 404 Not Found 용 Exception
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("사용자를 찾을 수 없습니다. (ID: " + id + ")");
    }
}