package com.leets.backend.blog_manage.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 댓글 생성 요청 DTO
 */
public class CommentCreateRequest {
    @NotBlank(message = "댓글 내용을 입력해주세요")
    private String content;

    public String getContent() {
        return content;
    }

    // Setter 추가 가능
    // public void setContent(String content) { this.content = content; }
}