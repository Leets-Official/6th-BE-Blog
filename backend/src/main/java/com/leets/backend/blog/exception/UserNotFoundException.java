package com.leets.backend.blog.exception;

/**
 * 대상 사용자(userId)를 찾지 못했을 때 던지는 예외입니다.
 */
public class UserNotFoundException extends AppException {
    public UserNotFoundException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND, "userId=" + userId + " not found");
    }
}