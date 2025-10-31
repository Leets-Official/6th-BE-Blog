package com.leets.backend.blog.exception;

/**
 * 대상 게시글(postId)를 찾지 못했을 때 던지는 예외입니다.
 */
public class PostNotFoundException extends AppException {
    public PostNotFoundException(Long postId) {
        super(ErrorCode.POST_NOT_FOUND, "postId=" + postId + " not found");
    }
}