package com.leets.backend.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // HTTP 404 Not Found
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
