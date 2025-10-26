package com.leets.backend.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentUpdateRequestDTO {
    @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
    private String content;

    public CommentUpdateRequestDTO() { }

    // getters
    public String getContent() { return content; }
}
