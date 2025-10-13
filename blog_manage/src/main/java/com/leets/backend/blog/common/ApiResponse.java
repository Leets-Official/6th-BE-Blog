package com.leets.backend.blog.common;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    public ApiResponse() {} // 기본 생성자 (직렬화 시 필요)

    // 성공 응답
    public static <T> ApiResponse<T> onSuccess(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = status.value();
        response.message = message;
        response.data = data;
        return response;
    }

    // 실패 응답
    public static <T> ApiResponse<T> onFailure(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = status.value();
        response.message = message;
        response.data = data;
        return response;
    }

    // Getter 추가 (JSON 직렬화용)
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
