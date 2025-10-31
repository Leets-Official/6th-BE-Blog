package com.leets.backend.blog.exception;

/**
 * 대상 댓글(commentId)를 찾지 못했을 때 던지는 예외입니다.
 */
public class CommentNotFoundException extends AppException {
    public CommentNotFoundException(Long commentId) {
        super(ErrorCode.COMMENT_NOT_FOUND, "commentId=" + commentId + " not found");
    }
}
