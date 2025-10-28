package com.leets.backend.blog.post.exception;

import com.leets.backend.blog.common.exception.*;

public class PostNotFoundException extends CustomException {
    public PostNotFoundException(String message) {
        super(ErrorCode.POST_NOT_FOUND, message);
    }
}