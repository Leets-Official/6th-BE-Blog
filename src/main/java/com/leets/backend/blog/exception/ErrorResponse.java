package com.leets.backend.blog.exception;

// 1. HttpStatus 임포트 추가!
import org.springframework.http.HttpStatus;

// 클라이언트에게 반환할 에러 응답 DTO
public class ErrorResponse {

    // 1. status 필드 추가
    private final int status;
    private final String message;

    // 2. (int, String) 생성자 추가
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // 3. (String) 생성자는 삭제하거나 그대로 두어도 됩니다.
    //    (혹시 다른 곳에서 쓸 수도 있으니 그대로 두는 것을 추천합니다.)
    public ErrorResponse(String message) {
        // (int, String) 생성자를 재사용하도록 수정
        this(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    // 4. status 필드의 Getter 추가
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}