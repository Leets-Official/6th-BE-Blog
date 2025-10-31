package com.leets.backend.blog.exception;

/**
 * 표준 에러 응답을 위한 모델입니다.
 */
public class ApiError {
    private final String code;
    private final String message;
    public ApiError(String code, String message) { this.code = code; this.message = message; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
}
