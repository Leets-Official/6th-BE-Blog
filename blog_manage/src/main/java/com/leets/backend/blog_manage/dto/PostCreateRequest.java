package com.leets.backend.blog_manage.dto;

import com.leets.backend.blog_manage.entity.Post;
import com.leets.backend.blog_manage.entity.User;
import jakarta.validation.constraints.NotBlank;

public class PostCreateRequest {
    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "본문 내용을 입력해주세요")
    private String content;

    public String getTitle() { return title; }
    public String getContent() { return content; }

    public Post toEntity(User user) {
        return new Post(this.title, this.content, user);
    }
}