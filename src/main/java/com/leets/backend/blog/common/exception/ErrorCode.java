package com.leets.backend.blog.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // 게시글 관련
    NO_UPDATE(1101, HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다."),
    NO_DELETE(1102, HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다."),
    POST_NOT_FOUND(1103, HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

    // 유저 관련
    USER_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    DUPLICATED_EMAIL(1002, HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATED_NICKNAME(1003, HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),

    // 인증 관련
    LOGIN_USER_NOT_FOUND(2001, HttpStatus.BAD_REQUEST, "등록되지 않은 이메일입니다."),
    LOGIN_INVALID_PASSWORD(2002, HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // 댓글 관련
    COMMENT_NOT_FOUND(3001, HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_COMMENT_ACCESS(3002, HttpStatus.FORBIDDEN, "댓글 수정/삭제 권한이 없습니다."),

    // 토큰 관련
    TOKEN_EXPIRED(2101, HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다."),
    INVALID_TOKEN(2102, HttpStatus.UNAUTHORIZED, "유효하지 않은 Access Token입니다."),
    TOKEN_MISSING(2103, HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),
    TOKEN_SIGNATURE_INVALID(2104, HttpStatus.UNAUTHORIZED, "토큰 서명이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(2105, HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    REFRESH_TOKEN_NOT_FOUND(2106, HttpStatus.NOT_FOUND, "Refresh Token을 찾을 수 없습니다."),
    REFRESH_TOKEN_MISMATCH(2107, HttpStatus.UNAUTHORIZED, "DB의 Refresh Token과 일치하지 않습니다."),

    // 소셜 로그인 관련
    KAKAO_TOKEN_REQUEST_FAILED(2201, HttpStatus.UNAUTHORIZED, "카카오 토큰 발급에 실패했습니다."),
    KAKAO_USERINFO_REQUEST_FAILED(2202, HttpStatus.UNAUTHORIZED, "카카오 사용자 정보 요청에 실패했습니다."),
    KAKAO_COMMUNICATION_ERROR(2203, HttpStatus.INTERNAL_SERVER_ERROR, "카카오 서버 통신 중 오류가 발생했습니다."),
    KAKAO_RESPONSE_NULL(2204, HttpStatus.UNAUTHORIZED, "카카오 서버로부터 응답이 없습니다."),
    KAKAO_EMAIL_SCOPE_REQUIRED(2205, HttpStatus.UNAUTHORIZED, "카카오 이메일 정보 접근 동의가 필요합니다.");

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
