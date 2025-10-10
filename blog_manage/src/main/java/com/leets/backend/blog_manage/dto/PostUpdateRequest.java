package com.leets.backend.blog_manage.dto;

import jakarta.validation.constraints.NotBlank;

public class PostUpdateRequest {
    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "본문 내용을 입력해주세요")
    private String content;

    public String getTitle() { return title; }
    public String getContent() { return content; }
}