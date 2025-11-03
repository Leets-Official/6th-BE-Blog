package com.leets.backend.blog.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        var code = ex.getErrorCode();
        ErrorResponse error = new ErrorResponse(
                code.getStatus().value(),
                code.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, code.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                500,
                "서버 오류: " + ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.internalServerError().body(error);
    }
}
