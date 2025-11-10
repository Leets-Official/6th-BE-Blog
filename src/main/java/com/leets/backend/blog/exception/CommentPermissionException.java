package com.leets.backend.blog.exception;

// 403 Forbidden 용 Exception
public class CommentPermissionException extends RuntimeException {

    // 기존 생성자 (Long, Long)
    public CommentPermissionException(Long commentId, Long userId) {
        super("댓글에 대한 권한이 없습니다. (Comment ID: " + commentId + ", User ID: " + (userId != null ? userId : "null") + ")");
    }

    // !! 에러 해결을 위해 이 생성자를 추가합니다 !!
    // (String)을 받는 생성자
    public CommentPermissionException(String message) {
        super(message);
    }
}