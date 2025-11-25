package com.leets.backend.blog_manage.exception;

/**
 * 커스텀 예외
 * ErrorCode를 포함한 비즈니스 예외
 */
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}