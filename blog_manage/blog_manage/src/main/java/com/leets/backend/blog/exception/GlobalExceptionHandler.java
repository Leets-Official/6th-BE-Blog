package com.leets.backend.blog.exception;

import com.leets.backend.blog.common.dto.ApiResponse;
import com.leets.backend.blog.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // UserNotFoundException 처리
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("USER_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(errorResponse));
    }

    // BadCredentialsException 처리
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException e) {
        ErrorResponse errorResponse = new ErrorResponse("BAD_CREDENTIALS", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(errorResponse));
    }

    // DuplicateEmailException 처리
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateEmail(DuplicateEmailException e) {
        ErrorResponse errorResponse = new ErrorResponse("DUPLICATE_EMAIL", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(errorResponse));
    }

    // DuplicateNicknameException 처리
    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateNickname(DuplicateNicknameException e) {
        ErrorResponse errorResponse = new ErrorResponse("DUPLICATE_NICKNAME", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(errorResponse));
    }

    // PostNotFoundException 처리
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handlePostNotFound(PostNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("POST_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(errorResponse));
    }

    // @Valid 유효성 검증 실패 시 발생하는 MethodArgumentNotValidException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException e
    ) {
        // 첫 번째 유효성 검증 실패 메시지를 가져옵니다.
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = (fieldError != null) ? fieldError.getDefaultMessage() : "입력값이 올바르지 않습니다.";

        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_INPUT",
                errorMessage
         );
        return ResponseEntity.badRequest().body(ApiResponse.error(errorResponse));
    }

    // 예상치 못한 모든 예외 처리 (디버깅을 위해 잠시 주석 처리)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception e) {
//        // 에러 로그를 콘솔에 출력합니다.
//        e.printStackTrace();
//
//        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiResponse.error(errorResponse));
//    }
}
