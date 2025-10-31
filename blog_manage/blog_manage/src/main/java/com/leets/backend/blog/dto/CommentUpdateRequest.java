package com.leets.backend.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentUpdateRequest {

    @NotBlank
    private String content;

    public CommentUpdateRequest() {
    }

    public CommentUpdateRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
