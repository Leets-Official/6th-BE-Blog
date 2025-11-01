package com.leets.backend.blog_manage.exception;

import com.leets.backend.blog_manage.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException; // [추가]
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid 유효성 검사 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiResponse<Object> response = ApiResponse.error(errorMessage);
        return new ResponseEntity<>(response, ErrorCode.INVALID_INPUT_VALUE.getStatus());
    }

    // 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse<Object> response = ApiResponse.error(errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    // Spring Security의 AccessDeniedException 처리 (권한 없음)
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.NO_AUTHORIZATION;
        ApiResponse<Object> response = ApiResponse.error(errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    // 기타 처리되지 않은 예외
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        // 로그 기록
        // logger.error("Unhandled exception: ", ex);
        ApiResponse<Object> response = ApiResponse.error("서버 내부 오류가 발생했습니다.");
        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}