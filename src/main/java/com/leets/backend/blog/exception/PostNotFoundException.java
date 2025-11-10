package com.leets.backend.blog.exception;

// 404 Not Found 용 Exception
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("게시글을 찾을 수 없습니다. (ID: " + id + ")");
    }
}
