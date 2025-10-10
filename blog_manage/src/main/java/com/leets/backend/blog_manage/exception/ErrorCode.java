package com.leets.backend.blog_manage.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 400 Bad Request
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력 값입니다."),

    // 403 Forbidden
    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}