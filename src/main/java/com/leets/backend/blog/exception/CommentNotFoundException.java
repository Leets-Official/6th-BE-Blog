package com.leets.backend.blog.exception;

// 404 Not Found 용 Exception
public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("댓글을 찾을 수 없습니다. (ID: " + id + ")");
    }
}