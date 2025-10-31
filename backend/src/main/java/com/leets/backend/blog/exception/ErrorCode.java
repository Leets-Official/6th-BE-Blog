package com.leets.backend.blog.exception;

/**
 * 서비스에서 사용되는 에러 코드와 HTTP 상태 매핑 정의
 */
public enum ErrorCode {
    USER_NOT_FOUND(404, "USER_NOT_FOUND"),
    POST_NOT_FOUND(404, "POST_NOT_FOUND"),
    COMMENT_NOT_FOUND(404, "COMMENT_NOT_FOUND"),
    COMMENT_PERMISSION_DENIED(403, "COMMENT_PERMISSION_DENIED"),
    VALIDATION_ERROR(400, "VALIDATION_ERROR"),
    INTERNAL_ERROR(500, "INTERNAL_ERROR");

    private final int status;
    private final String code;
    ErrorCode(int status, String code) { this.status = status; this.code = code; }
    public int status() { return status; }
    public String code() { return code; }
}
