package com.leets.backend.blog.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // 게시글 관련
    POST_NOT_FOUND(1023, HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    NO_UPDATE(1002, HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다."),
    NO_DELETE(1003, HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다."),

    // 유저 관련
    USER_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public Integer getCode() { return code; }
    public HttpStatus getHttpStatus() { return httpStatus; }
    public String getMessage() { return message; }
}
