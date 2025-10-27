package com.leets.backend.blog_manage.dto;

import com.leets.backend.blog_manage.entity.Post;
import java.time.LocalDateTime;

public class PostDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String authorNickname;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostDetailResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorNickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorNickname() { return authorNickname; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}