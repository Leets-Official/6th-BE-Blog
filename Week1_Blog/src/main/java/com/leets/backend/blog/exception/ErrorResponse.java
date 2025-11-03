package com.leets.backend.blog.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    // HTTP 상태 코드
    private final  int status; // HTTP 상태 코드
    private final String message;
    // 요청 URL
    private final String path;
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
