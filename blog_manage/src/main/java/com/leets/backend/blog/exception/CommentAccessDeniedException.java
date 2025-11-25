package com.leets.backend.blog.exception;

public class CommentAccessDeniedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "이 댓글에 대한 접근 권한이 없습니다. 작성자만 수정 또는 삭제할 수 있습니다.";

    public CommentAccessDeniedException() {
        super(DEFAULT_MESSAGE);
    }

    public CommentAccessDeniedException(String message) {
        super(message);
    }
}
