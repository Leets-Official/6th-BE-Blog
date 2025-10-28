package com.leets.backend.blog.common.exception;

import com.leets.backend.blog.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        log.error("Unhandled Exception: {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, null));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("Validation Error: {}", msg);
        return ApiResponse.fail(new CustomException(ErrorCode.BAD_REQUEST, msg));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handleNotFoundException(IllegalArgumentException e) {
        log.error("Resource Not Found: {}", e.getMessage());
        return ApiResponse.fail(new CustomException(ErrorCode.NOT_FOUND, e.getMessage()));
    }
}
