package com.leets.backend.blog.exception;

/**
 * 전역 핸들러에서 일관된 에러 응답을 만들기 위한 추상 클래스입니다.
 */
public abstract class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    protected AppException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    public ErrorCode errorCode() { return errorCode; }
}
