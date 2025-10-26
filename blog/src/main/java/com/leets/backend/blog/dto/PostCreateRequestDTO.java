package com.leets.backend.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class PostCreateRequestDTO {

    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    private String title;

    @NotBlank(message = "글 내용은 비워둘 수 없습니다.")
    private String content;

    public PostCreateRequestDTO() {}

    // getters
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
