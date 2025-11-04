package com.leets.backend.blog.comment.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {
    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;

    public String getContent() {
        return content;
    }
}
