package com.leets.backend.blog.exception;

public class PostNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "게시물을 찾을 수 없습니다.";

    public PostNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public PostNotFoundException(Long postId) {
        super(DEFAULT_MESSAGE + " (ID: " + postId + ")");
    }
}