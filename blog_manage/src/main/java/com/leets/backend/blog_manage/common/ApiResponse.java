package com.leets.backend.blog_manage.common;

/**
 * API 응답 공통 포맷
 * @param <T> 응답 데이터 타입
 */
public class ApiResponse<T> {

    private final String status;
    private final String message;
    private final T data;

    private ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /** 성공 응답 생성 */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    /** 에러 응답 생성 */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }

    // --- Getters ---
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}