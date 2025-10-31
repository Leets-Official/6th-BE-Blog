package com.leets.backend.blog_manage.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentUpdateRequest {
    @NotBlank(message = "댓글 내용을 입력해주세요")
    private String content;

    public String getContent() {
        return content;
    }

    // public void setContent(String content) { this.content = content; }
}