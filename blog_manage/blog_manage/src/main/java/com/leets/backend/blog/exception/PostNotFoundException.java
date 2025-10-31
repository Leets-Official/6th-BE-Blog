package com.leets.backend.blog.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("게시글을 찾을 수 없습니다.");
    }

    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(Long id) {
        super(id + "게시글을 찾을 수 없습니다. ");
    }
}
