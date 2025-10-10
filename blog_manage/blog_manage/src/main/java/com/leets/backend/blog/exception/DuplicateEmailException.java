package com.leets.backend.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // HTTP 409 Conflict
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("이미 등록된 이메일입니다.");
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}
