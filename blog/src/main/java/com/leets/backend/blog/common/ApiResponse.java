package com.leets.backend.blog.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

public class ApiResponse<T> {

    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public ApiResponse() {
        this.status = 0;
        this.message = null;
        this.data = null;
    }

    private ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 성공 응답 - 응답 데이터 미포함
    public static <T> ApiResponse<T> onSuccess(HttpStatus status, String message) {
        return new ApiResponse<>(status.value(), message, null);
    }

    // 성공 응답 - 응답 데이터 포함
    public static <T> ApiResponse<T> onSuccess(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status.value(), message, data);
    }

    // 실패 응답
    public static <T> ApiResponse<T> onFailure(HttpStatus status, String message) {
        return new ApiResponse<>(status.value(), message, null);
    }

    // getters
    public int getStatus() { return status; }

    public String getMessage() { return message; }

    public T getData() { return data; }
}
