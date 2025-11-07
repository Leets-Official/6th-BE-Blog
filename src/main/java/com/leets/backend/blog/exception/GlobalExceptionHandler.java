package com.leets.backend.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

// @RestControllerAdvice : 모든 @RestController에서 발생하는 예외를 처리
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 Not Found 예외 그룹을 처리하는 핸들러
    // (하나의 핸들러가 여러 예외를 처리할 수 있습니다)
    @ExceptionHandler({
            PostNotFoundException.class,
            CommentNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex, WebRequest request) {

        // 님의 ErrorResponse 형식(status, message)에 맞춰서 응답 생성
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), // 404
                ex.getMessage() // 예외 클래스에서 설정한 메시지
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // 404
    }

    // 403 Forbidden 예외를 처리하는 핸들러
    @ExceptionHandler(CommentPermissionException.class)
    public ResponseEntity<ErrorResponse> handlePermissionException(CommentPermissionException ex, WebRequest request) {

        // 님의 ErrorResponse 형식(status, message)에 맞춰서 응답 생성
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(), // 403
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN); // 403
    }

    // (선택) 그 외 모든 500 Internal Server Error를 처리하는 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        // 중요: 실제 운영 환경에서는 로그를 남겨야 합니다.
        ex.printStackTrace(); // 혹은 로거 사용

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                "서버 내부 오류가 발생했습니다."
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }
}