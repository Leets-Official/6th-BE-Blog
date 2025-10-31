package com.leets.backend.blog.exception;

public class CommentAccessDeniedException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "댓글에 접근할 권한이 없습니다.";

    public CommentAccessDeniedException() {
        super(DEFAULT_MESSAGE);
    }
}
