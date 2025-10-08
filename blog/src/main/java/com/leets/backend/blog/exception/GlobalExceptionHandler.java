package com.leets.backend.blog.exception;

import com.leets.backend.blog.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request (DTO 검증 실패)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(
                ApiResponse.onFailure(HttpStatus.BAD_REQUEST, "요청 데이터 유효성 검증 실패"),
                HttpStatus.BAD_REQUEST
        );
    }

    // 404 Not Found
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostNotFoundException(PostNotFoundException exception) {
        return new ResponseEntity<>(
                ApiResponse.onFailure(HttpStatus.NOT_FOUND, exception.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    // 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception exception) {
        return new ResponseEntity<>(
                ApiResponse.onFailure(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
