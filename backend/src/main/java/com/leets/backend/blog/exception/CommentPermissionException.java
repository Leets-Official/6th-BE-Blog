package com.leets.backend.blog.exception;

/**
 * 댓글에 대한 권한이 없을 때 던지는 예외입니다.
 */
public class CommentPermissionException extends AppException {
    public CommentPermissionException(Long userId, Long commentId) {
        super(ErrorCode.COMMENT_PERMISSION_DENIED,
                "userId=" + userId + " has no permission on commentId=" + commentId);
    }
}
