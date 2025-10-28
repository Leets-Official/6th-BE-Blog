package com.leets.backend.blog.post.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용은 비어 있을 수 없습니다.")
    private String content;

    public String getTitle() { return title; }
    public String getContent() { return content; }
}
