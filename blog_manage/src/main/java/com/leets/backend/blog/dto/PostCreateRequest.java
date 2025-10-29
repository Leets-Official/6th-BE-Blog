package com.leets.backend.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class PostCreateRequest {
    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    private String title;
    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;

    public PostCreateRequest() {}

    public PostCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
